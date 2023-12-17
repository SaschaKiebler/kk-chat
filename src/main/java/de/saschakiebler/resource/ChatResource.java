package de.saschakiebler.resource;

import java.util.List;

import de.saschakiebler.model.Message;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
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


    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {
        List<Message> messages = Message.listAll();
        return chat.data("messages", messages);
    }

    @POST
    @Path("/send-message")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response sendMessage(@FormParam("message") String messageText) {
        // Add message to your messages list or database

        Message message = new Message("User", messageText);
        Message.persist(message);

        if(message.isPersistent())
        return Response.seeOther(URI.create("/chat")).build();
        else
        return Response.serverError().build();
    }
}

