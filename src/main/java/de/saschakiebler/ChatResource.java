package de.saschakiebler;

import java.util.ArrayList;
import java.util.List;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
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
public class ChatResource {
    @Inject Template chat;

    private List<Message> messages = new ArrayList<>();

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {
        return chat.data("messages", messages);
    }

    @POST
    @Path("/send-message")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response sendMessage(@FormParam("message") String messageText) {
        // Add message to your messages list or database
        messages.add(new Message("User", messageText)); // Assuming 'User' as the sender
        return Response.seeOther(URI.create("/chat")).build();
    }
}

