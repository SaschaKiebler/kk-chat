package de.saschakiebler.service;

import java.util.List;
import java.util.stream.Collectors;

import de.saschakiebler.dto.ConversationDTO;
import de.saschakiebler.dto.MessageDTO;
import de.saschakiebler.model.Conversation;
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
        Conversation.update(name, conversation);
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

}
