package de.saschakiebler.service;

import java.util.concurrent.ExecutorService;

import de.saschakiebler.enums.MessageRoles;
import de.saschakiebler.model.Message;
import dev.langchain4j.model.openai.OpenAiChatModel;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@Transactional
@ApplicationScoped
public class ChatService {

     @Inject
    ExecutorService executorService; // Inject an ExecutorService for asynchronous execution

    public Uni<Message> answerMessage(String messageText) {
        return Uni.createFrom().item(() -> {
            String apiKey = System.getenv("OPENAI_API_KEY");
            OpenAiChatModel model = OpenAiChatModel.withApiKey(apiKey);
            
            Message answer = safeMessage(model.generate(messageText), MessageRoles.ASSISTANT);
            return answer;
        }).runSubscriptionOn(executorService); // Run on a separate thread
    }


    public Message safeMessage(String messageText, MessageRoles sender) {
        Message message = new Message(sender.getRole(), messageText);
        Message.persist(message);
        return message;
    }
    
}
