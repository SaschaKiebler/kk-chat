package de.saschakiebler.resource;
import java.util.List;

import org.jboss.resteasy.reactive.RestStreamElementType;

import de.saschakiebler.dto.ConversationDTO;
import de.saschakiebler.enums.MessageRoles;
import de.saschakiebler.model.Conversation;
import de.saschakiebler.service.ChatUIService;
import de.saschakiebler.service.ConversationService;
import de.saschakiebler.service.MessageService;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.mutiny.Multi;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;


/**
 * Represents a resource for handling chat-related operations in the UI.
 * This class is responsible for providing HTML templates and streaming chat responses.
 */
@Path("/chat")
@Resource
@Transactional
public class ChatUIResource {
    @Inject Template chat;
    
    @Inject ChatUIService chatService;

    @Inject MessageService messageService;

    @Inject ConversationService conversationService;

    public ChatUIResource(Template chat, ChatUIService chatService, ConversationService conversationService, MessageService messageService) {
        this.chat = chat;
        this.chatService = chatService;
        this.conversationService = conversationService;
        this.messageService = messageService;
    }

    public ChatUIResource() {
    }
   

     @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getConversation(@QueryParam("conversationId") String conversationIdString) {
        Conversation conversation = conversationService.getConversation(conversationIdString);
        
        List<Conversation> conversations = conversationService.getAllConversations();

        return chat.data( "conversations", conversations, "conversationId", conversation.id);
        
    }


    @GET
    @Path("/streamAnswer")
    @RestStreamElementType(MediaType.TEXT_PLAIN)
    public void streamAnswer(@QueryParam("messageText") String messageText, 
                             @Context SseEventSink eventSink, 
                             @Context Sse sse,
                             @QueryParam("conversationId") String conversationId) {
        
        try {
            messageService.createMessage(messageText, MessageRoles.USER, conversationService.getConversation(Long.parseLong(conversationId)));
            Multi<String> responseStream = chatService.streamAnswer(messageText, Long.parseLong(conversationId));
    
            responseStream.subscribe().with(
                item -> eventSink.send(sse.newEvent(item)),
                failure -> {
                    System.out.println("Error in streaming response: " + failure.getMessage());
                    eventSink.close();
                },
                () -> eventSink.close()
            );
        } catch (Exception e) {
            System.out.println("Error in streamAnswer endpoint: " + e.getMessage());
            eventSink.close();
        }
    }
    


    @GET
    @Path("/allMessages")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMessages(@QueryParam("conversationId") String conversationIdString) {
        if (
            Conversation.findById(Long.parseLong(conversationIdString)) == null ||
            conversationIdString == null || 
            conversationIdString.equals("") || 
            conversationIdString.equals("undefined")) {
            return Response.status(404).build();
            
        }
        Long conversationId = Long.parseLong(conversationIdString);
        ConversationDTO conversationDTO = conversationService.getConversationDTO(conversationId);
        return Response.ok(conversationDTO).build();
    }


    @GET
    @Path("/createConversation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createConversation() {
        Conversation conversation = conversationService.createConversation();
        return Response.ok(conversation).build();
    }
    
    @GET
    @Path("/generateConversationName")
    @Produces(MediaType.TEXT_PLAIN)
    public Response generateConversationName(@QueryParam("conversationId") String conversationIdString) {
        if (
            Conversation.findById(Long.parseLong(conversationIdString)) == null ||
            conversationIdString == null || 
            conversationIdString.equals("") || 
            conversationIdString.equals("undefined")) {
            return Response.status(404).build();
            
        }
        Long conversationId = Long.parseLong(conversationIdString);
        String conversationName = conversationService.generateConversationName(conversationId);
        return Response.ok(conversationName).build();
    }
    

}

