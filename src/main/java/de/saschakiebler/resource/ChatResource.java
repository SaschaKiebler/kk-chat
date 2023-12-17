package de.saschakiebler.resource;

import java.util.List;
import java.util.concurrent.ExecutorService;

import de.saschakiebler.model.Message;
import dev.langchain4j.model.openai.OpenAiChatModel;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


import java.net.URI;

@Path("/chat")
@Resource
@Transactional
public class ChatResource {
    @Inject Template chat;
    
    
    @Inject
    ExecutorService executorService; // Inject an ExecutorService for asynchronous execution


    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {
        List<Message> messages = Message.listAll();
        //reverse the list to get the newest messages first
        messages.sort((o1, o2) -> o2.id.compareTo(o1.id));
        return chat.data("messages", messages);
    }

    @POST
    @Path("/send-message")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Uni<Response> sendMessage(@FormParam("message") String messageText) {
        Message message = new Message("User", messageText);
        Message.persist(message);

        return answerMessage(messageText)
            .onItem().transform(answer -> {
                if (message.isPersistent()) {
                    return Response.seeOther(URI.create("/chat")).build();
                } else {
                    return Response.serverError().build();
                }
            });
    }

    public Uni<Message> answerMessage(String messageText) {
        return Uni.createFrom().item(() -> {
            String apiKey = System.getenv("OPENAI_API_KEY");
            OpenAiChatModel model = OpenAiChatModel.withApiKey(apiKey);
            
            Message answer = new Message("AI", model.generate(messageText));
            Message.persist(answer);
            return answer;
        }).runSubscriptionOn(executorService); // Run on a separate thread
    }

}

