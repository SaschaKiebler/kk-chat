package de.saschakiebler.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import de.saschakiebler.dto.MessageDTO;
import de.saschakiebler.enums.MessageRoles;
import de.saschakiebler.model.Conversation;
import de.saschakiebler.model.Message;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.MultiEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;


import dev.langchain4j.model.output.Response;

import static dev.langchain4j.data.message.UserMessage.userMessage;
import static dev.langchain4j.data.message.AiMessage.aiMessage;
import static dev.langchain4j.data.message.SystemMessage.systemMessage;


@Transactional
@ApplicationScoped
public class ChatUIService {

     @Inject
    ExecutorService executorService; 

    @Inject
    MessageService messageService;

    @Inject
    ConversationService conversationService;


    public Message safeMessageInConversation(String messageText, MessageRoles sender, Conversation conversation) {
        Message message = new Message(sender.getRole(), messageText, conversation);
        Message.persist(message);
        return message;
        
    }


   public Multi<String> streamAnswer(String messageText, Long conversationId) {
    // String modelName = "gpt-3.5-turbo-1106";    
    String modelName = "gpt-4-1106-preview";
    List<MessageDTO> memory = conversationService.getChatMemory(conversationId);
    ChatMemory chatMemory = TokenWindowChatMemory.withMaxTokens(4069, new OpenAiTokenizer(modelName));

    chatMemory.add(systemMessage("Du bist ein Experte für das Thema Digitalisierung des Gesundheitswesens in Deutschland. Du bist sehr hilfsbereit und antwortest immer freundlich."));

    for (MessageDTO message : memory) {
        if (message.getSender().equals(MessageRoles.ASSISTANT.getRole())) {
            chatMemory.add(aiMessage(message.getText()));
        } else {
            chatMemory.add(userMessage(message.getText()));
        }
    }
    chatMemory.add(userMessage(messageText));

    return Multi.createFrom().emitter(emitter -> {
        initiateApiCall(chatMemory, emitter, 0, modelName, conversationId); // Start with 0 retries
    });
}

private void initiateApiCall(ChatMemory chatMemory, MultiEmitter<? super String> emitter, int retryCount, String modelName, Long conversationId) {

    int MAX_RETRIES = 3;

    try {
        StreamingChatLanguageModel model = 
            OpenAiStreamingChatModel.builder()
                                    .apiKey(System.getenv("OPENAI_API_KEY"))
                                    .modelName(modelName)
                                    .build();

        model.generate(chatMemory.messages(), new StreamingResponseHandler<AiMessage>() {
            @Override
            public void onNext(String token) {
                emitter.emit(token);
            }

            @Override
            public void onComplete(Response<AiMessage> response) {
                Uni.createFrom().item(() -> {
                        messageService.createMessage(response.content().text(), MessageRoles.ASSISTANT, Conversation.findById(conversationId));
                        return "finished";
                    }).runSubscriptionOn(executorService)
                    .subscribe().with(item -> emitter.complete());
            }

            @Override
            public void onError(Throwable error) {
                if (retryCount < MAX_RETRIES) {
                    System.out.println("Retrying due to error: " + error.getMessage());
                    initiateApiCall(chatMemory, emitter, retryCount + 1, modelName, conversationId); // Retry
                } else {
                    System.out.println("Max retries reached. Error: " + error.getMessage());
                    emitter.fail(error);
                }
            }
        });
    } catch (Exception e) {
        System.out.println("Error setting up OpenAI API model: " + e.getMessage());
        emitter.fail(e);
    }
}

    
}
