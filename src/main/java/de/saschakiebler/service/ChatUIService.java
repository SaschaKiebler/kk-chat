package de.saschakiebler.service;

import java.util.List;
import java.util.concurrent.ExecutorService;

import de.saschakiebler.enums.MessageRoles;
import de.saschakiebler.model.Conversation;
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
public class ChatUIService {

     @Inject
    ExecutorService executorService; 

    @Inject
    MessageService messageService;


    public Message safeMessageInConversation(String messageText, MessageRoles sender, Conversation conversation) {
        Message message = new Message(sender.getRole(), messageText, conversation);
        Message.persist(message);
        return message;
        
    }


    public Multi<String> streamAnswer(String messageText, Long conversationId) {
        Multi<String> responseStream = Multi.createFrom().emitter(emitter -> {
            StreamingChatLanguageModel model = 
            OpenAiStreamingChatModel.builder()
                                    .apiKey(System.getenv("OPENAI_API_KEY"))
                                    .modelName("gpt-4-1106-preview")
                                    .build();

            List<ChatMessage> messages = asList(
                    userMessage(messageText)
            );

            model.generate(messages, new StreamingResponseHandler<AiMessage>() {
                @Override
                public void onNext(String token) {
                    emitter.emit(token);
                }

                @Override
                public void onComplete(Response<AiMessage> response) {
                    Uni.createFrom().item(() -> {
                        messageService.createMessage(response.content().text(), MessageRoles.ASSISTANT, Conversation.findById(conversationId));
                        return null;
                    }).runSubscriptionOn(executorService)
                    .subscribe().with(item -> emitter.complete());
                }

                @Override
                public void onError(Throwable error) {
                    emitter.fail(error);
                }
            });
        });


        return responseStream;
    }
    
}
