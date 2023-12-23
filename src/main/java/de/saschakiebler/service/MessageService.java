package de.saschakiebler.service;

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


    

    public MessageDTO convertToMessageDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setText(message.getText());
        messageDTO.setSender(message.getSender());
        messageDTO.setTimestamp(message.getTimestamp());
        
        return messageDTO;
    }


}
