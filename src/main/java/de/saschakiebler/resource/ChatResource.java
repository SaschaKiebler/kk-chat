package de.saschakiebler.resource;

import java.util.List;

import de.saschakiebler.enums.MessageRoles;
import de.saschakiebler.model.Message;
import de.saschakiebler.service.ChatService;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;

import java.net.URI;

@Path("/chat")
@Resource
@Transactional
public class ChatResource {
    @Inject Template chat;
    
    @Inject ChatService chatService;
   


    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {
        List<Message> messages = Message.listAll();
        //reverse the list to get the newest messages first
        messages.sort((o1, o2) -> o2.id.compareTo(o1.id));
        return chat.data("messages", messages);
    }

    /*
    @POST
    @Path("/send-message")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Uni<Response> sendMessage(@FormParam("message") String messageText) {
        Message message = chatService.safeMessage(messageText, MessageRoles.USER);

        return chatService.answerMessage(messageText)
            .onItem().transform(answer -> {
                if (message.isPersistent()) {
                    return Response.seeOther(URI.create("/chat")).build();
                } else {
                    return Response.serverError().build();
                }
            });
    }
*/

    @GET
    @Path("/streamAnswer")
    @Produces(MediaType.SERVER_SENT_EVENTS)
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

    

}

