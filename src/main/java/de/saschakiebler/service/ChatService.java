package de.saschakiebler.service;

import java.util.List;
import java.util.concurrent.ExecutorService;

import de.saschakiebler.enums.MessageRoles;
import de.saschakiebler.model.Message;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;


import dev.langchain4j.model.output.Response;

import static dev.langchain4j.data.message.UserMessage.userMessage;
import static java.util.Arrays.asList;


@Transactional
@ApplicationScoped
public class ChatService {

     @Inject
    ExecutorService executorService; 

    public Message safeMessage(String messageText, MessageRoles sender) {
        Message message = new Message(sender.getRole(), messageText);
        Message.persist(message);
        return message;
        
    }


    public Multi<String> streamAnswer(String messageText) {
        // Create the Multi emitter
        Multi<String> responseStream = Multi.createFrom().emitter(emitter -> {
            // Create the OpenAI Streaming model
            StreamingChatLanguageModel model = OpenAiStreamingChatModel.withApiKey(System.getenv("OPENAI_API_KEY"));
            
            List<ChatMessage> messages = asList(
                    userMessage(messageText)
            );

            // Start generating the response and stream each token
            model.generate(messages, new StreamingResponseHandler<AiMessage>() {
                @Override
                public void onNext(String token) {
                    // Emit each token to the Multi stream
                    emitter.emit(token);
                }

                @Override
                public void onComplete(Response<AiMessage> response) {
                    // Complete the stream
                    Uni.createFrom().item(() -> {
                        safeMessage(response.content().text(), MessageRoles.ASSISTANT);
                        return null;
                    }).runSubscriptionOn(executorService)
                    .subscribe().with(item -> emitter.complete());
                }

                @Override
                public void onError(Throwable error) {
                    // Pass the error to the stream
                    emitter.fail(error);
                }
            });
        });


        // Return the Multi stream
        return responseStream;
    }
    
}
