package de.saschakiebler.service;

import static dev.langchain4j.data.message.UserMessage.userMessage;
import static dev.langchain4j.data.message.AiMessage.aiMessage;

import java.util.List;
import java.util.stream.Collectors;

import de.saschakiebler.dto.ConversationDTO;
import de.saschakiebler.dto.MessageDTO;
import de.saschakiebler.model.Conversation;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@Transactional
@ApplicationScoped
public class ConversationService {
    
    @Inject
    MessageService messageService;


    public Conversation getConversation(Long conversationId) {
        Conversation conversation = Conversation.findById(conversationId);
        return conversation;
    }

    
   
    public Conversation createConversation() {
        Conversation conversation = new Conversation();
        Conversation.persist(conversation);
        return conversation;
        
    }



    public Conversation updateConversation(Conversation conversation, String name) {
        Conversation.update("name = ?1 where id = ?2", name, conversation.id);
        Conversation returnConversation = Conversation.findById(conversation.id);
        if (returnConversation == null) {
            throw new IllegalArgumentException("Conversation not found");
        }
        else {
            return returnConversation;
        }
    }



    public List<Conversation> getAllConversations() {
        List<Conversation> conversations = Conversation.listAll();
        conversations.sort((o1, o2) -> o2.id.compareTo(o1.id));
        return conversations;
    }



    public Conversation getConversation(String conversationIdString) {
        Conversation conversation;
        if (conversationIdString == null || conversationIdString.equals("") || conversationIdString.equals("undefined")) {
            conversation = createConversation();
             
        }
        else {
            Long conversationId = Long.parseLong(conversationIdString);
            conversation = getConversation(conversationId);
            if (conversation == null) {
                conversation = createConversation();
            }
    }
    return conversation;
    }



     public ConversationDTO getConversationDTO(Long conversationId) {
        Conversation conversation = Conversation.findById(conversationId);

        ConversationDTO conversationDTO = convertToConversationDTO(conversation);

        return conversationDTO;
    }



    public ConversationDTO convertToConversationDTO(Conversation conversation) {
        ConversationDTO conversationDTO = new ConversationDTO();
        conversationDTO.setId(conversation.getId());

        if(conversation.getMessages() == null) {
            conversationDTO.setMessages(List.of());
        }
        else{
        List<MessageDTO> messageDTOs = conversation.getMessages()
            .stream()
            .map(messageService::convertToMessageDTO)
            .collect(Collectors.toList());

        messageDTOs.sort((o1, o2) -> o2.getTimestamp().compareTo(o1.getTimestamp()));
        
        conversationDTO.setMessages(messageDTOs);
        }

        conversationDTO.setName(conversation.getName());
        return conversationDTO;
    }


    public List<MessageDTO> getChatMemory(Long conversationId) {
        Conversation conversation = Conversation.findById(conversationId);
        List<MessageDTO> messageDTOs = convertToConversationDTO(conversation).getMessages();
        messageDTOs.sort((o1, o2) -> o2.getTimestamp().compareTo(o1.getTimestamp()));
        return messageDTOs;
    }

    public String generateConversationName(Long conversationId) {
        ChatLanguageModel model = OpenAiChatModel.builder()
                                    .apiKey(System.getenv("OPENAI_API_KEY"))
                                    .modelName("gpt-3.5-turbo-1106")
                                    .build();

        List<ChatMessage> messages = getChatMemory(conversationId)
                                        .stream()
                                        .map(message -> {
                                            if(message.getSender().equals("assistant")){
                                                return aiMessage(message.getText());
                                            }else{
                                                return userMessage(message.getText());
                                            }
                                        })
                                        .collect(Collectors.toList());
        
        messages.add(userMessage("give me a title for this conversation without any other text"));

        String conversationName = model.generate(messages).content().text();

        Conversation conversation = getConversation(String.valueOf(conversationId));
        updateConversation(conversation, conversationName);

        return conversationName;
    }
}
