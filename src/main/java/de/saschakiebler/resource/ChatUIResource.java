package de.saschakiebler.resource;

import java.util.List;

import org.jboss.resteasy.reactive.RestStreamElementType;

import de.saschakiebler.enums.MessageRoles;
import de.saschakiebler.model.Conversation;
import de.saschakiebler.model.Message;
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
   


    // @GET
    // @Produces(MediaType.TEXT_HTML)
    // public TemplateInstance get() {
    //     List<Message> messages = Message.listAll();
    //     //reverse the list to get the newest messages first
    //     messages.sort((o1, o2) -> o2.id.compareTo(o1.id));
    //     return chat.data("messages", messages);
    // }

     @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getConversation(@QueryParam("conversationId") String conversationIdString) {
        Long conversationId = Long.parseLong(conversationIdString);
        List<Message> messages = messageService.getAllMessagesFromConversation(conversationId);
        List<Conversation> conversations = conversationService.getAllConversations();
        //reverse the list to get the newest messages first
        messages.sort((o1, o2) -> o2.id.compareTo(o1.id));
        return chat.data("messages", messages, "conversations", conversations);
    }


    @GET
    @Path("/streamAnswer")
    @RestStreamElementType(MediaType.TEXT_PLAIN)
    public void streamAnswer(@QueryParam("messageText") String messageText, 
                             @Context SseEventSink eventSink, 
                             @Context Sse sse){
        
        chatService.safeMessage(messageText, MessageRoles.USER);
        Multi<String> responseStream = chatService.streamAnswer(messageText);



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
    @Path("/allMessages/{conversationId}}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> getAllMessages(@QueryParam("conversationId") Long conversationId) {
        
        List<Message> messages = messageService.getAllMessagesFromConversation(conversationId);
        return messages;
    }

    

}

