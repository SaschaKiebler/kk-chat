package de.saschakiebler.resource;

import java.util.Collections;
import java.util.List;

import org.jboss.resteasy.reactive.RestStreamElementType;

import de.saschakiebler.dto.ConversationDTO;
import de.saschakiebler.dto.MessageDTO;
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

    @Inject Template chatWebsocket;
    
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
        Conversation conversation;
        if (conversationIdString == null || conversationIdString.equals("") || conversationIdString.equals("undefined")) {
            conversation = conversationService.createConversation();
             List<MessageDTO> messages = Collections.emptyList();
            List<Conversation> conversations = conversationService.getAllConversations();
            return chat.data("messages", messages, "conversations", conversations, "conversationId", conversation.id);
        }
        else {
            Long conversationId = Long.parseLong(conversationIdString);
            conversation = conversationService.getConversation(conversationId);
            if (conversation == null) {
                conversation = conversationService.createConversation();
            }
        
        ConversationDTO conversationDTO = messageService.getAllMessagesFromConversation(conversation.id);
        List<Conversation> conversations = conversationService.getAllConversations();
        //reverse the list to get the newest messages first
        List<MessageDTO> messages = conversationDTO.getMessages();
        messages.sort((o1, o2) -> o2.getTimestamp().compareTo(o1.getTimestamp()));
        return chat.data("messages", messages, "conversations", conversations, "conversationId", conversation.id);
        }
    }


    @GET
    @Path("/streamAnswer")
    @RestStreamElementType(MediaType.TEXT_PLAIN)
    public void streamAnswer(@QueryParam("messageText") String messageText, 
                             @Context SseEventSink eventSink, 
                             @Context Sse sse,
                             @QueryParam("conversationId") String conversationId) {
        
        messageService.createMessage(messageText, MessageRoles.USER, conversationService.getConversation(Long.parseLong(conversationId)));
        Multi<String> responseStream = chatService.streamAnswer(messageText, Long.parseLong(conversationId));



        responseStream.subscribe().with(
            item -> {
                eventSink.send(sse.newEvent(item));
            },
            failure -> {
                eventSink.close();
            },
            () -> {
                eventSink.close();
            }
        );
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
        ConversationDTO conversationDTO = messageService.getAllMessagesFromConversation(conversationId);
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
    @Path("/websocketUI")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getWebsocketUI() {
        return chatWebsocket.data("conversationId", conversationService.createConversation().id);
    }


}

