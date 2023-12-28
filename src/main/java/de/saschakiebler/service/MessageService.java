package de.saschakiebler.service;

import de.saschakiebler.dto.MessageDTO;
import de.saschakiebler.enums.MessageRoles;
import de.saschakiebler.model.Conversation;
import de.saschakiebler.model.Message;
import de.saschakiebler.repository.MessageRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@Transactional
@ApplicationScoped
public class MessageService {
    
    @Inject
    MessageRepository messageRepository;

    public Message createMessage(String messageText, MessageRoles sender, Conversation conversation) {
        Message message = new Message(sender.getRole(), messageText, conversation);
        return messageRepository.createMessage(message);
    }

    public Message updateMessage(Message message, String messageText) {
        message.setText(messageText);
        return messageRepository.updateMessage(message);
    }

    public Boolean deleteMessage(Message message) {
        messageRepository.deleteMessage(message.getId());
        return true;
    }


    

    public MessageDTO convertToMessageDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setText(message.getText());
        messageDTO.setSender(message.getSender());
        messageDTO.setTimestamp(message.getTimestamp());
        
        
        return messageDTO;
    }


}
