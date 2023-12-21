package de.saschakiebler.service;

import java.util.List;
import java.util.stream.Collectors;

import de.saschakiebler.dto.ConversationDTO;
import de.saschakiebler.dto.MessageDTO;
import de.saschakiebler.enums.MessageRoles;
import de.saschakiebler.model.Conversation;
import de.saschakiebler.model.Message;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@Transactional
@ApplicationScoped
public class MessageService {
    

    public Message createMessage(String messageText, MessageRoles sender, Conversation conversation) {
        Message message = new Message(sender.getRole(), messageText, conversation);
        Message.persist(message);
        return message;
        
    }

    public Message updateMessage(Message message, String messageText) {
        Message.update(messageText, message);
        Message returnMessage = Message.findById(message.id);
        if (returnMessage == null) {
            throw new IllegalArgumentException("Message not found");
        }
        else {
            return returnMessage;
        }
    }

    public Boolean deleteMessage(Message message) {
        boolean messageDeleted = Message.deleteById(message.id);

        return messageDeleted;
    }

    // public List<Message> getAllMessagesFromConversation(Long conversationId) {
    //     Conversation conversation = Conversation.findById(conversationId);
    //     List<Message> messages = Message.list("conversation", conversation);
    //     return messages;
    // }

     public ConversationDTO getAllMessagesFromConversation(Long conversationId) {
        Conversation conversation = Conversation.findById(conversationId);

        // Convert to DTO
        ConversationDTO conversationDTO = new ConversationDTO();
        conversationDTO.setId(conversation.getId());
        
        List<MessageDTO> messageDTOs = conversation.getMessages()
            .stream()
            .map(this::convertToMessageDTO)
            .collect(Collectors.toList());
        
        conversationDTO.setMessages(messageDTOs);

        return conversationDTO;
    }

    private MessageDTO convertToMessageDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setText(message.getText());
        messageDTO.setSender(message.getSender());
        messageDTO.setTimestamp(message.getTimestamp());
        
        return messageDTO;
    }


}
