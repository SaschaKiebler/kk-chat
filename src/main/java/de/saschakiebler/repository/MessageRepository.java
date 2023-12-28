package de.saschakiebler.repository;

import de.saschakiebler.model.Message;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class MessageRepository {
    

    public Message createMessage(Message message) {
        Message.persist(message);
        return message;
    }

    public Message getMessageById(Long messageId) {
        Message message = Message.findById(messageId);
        return message;
    }

    public Message updateMessage(Message message) {
        Message.update("sender = ?1, text = ?2 where id = ?3", message.sender, message.text, message.id);
        Message returnMessage = Message.findById(message.id);
        if (returnMessage == null) {
            throw new IllegalArgumentException("Message not found");
        }
        else {
            return returnMessage;
        }
    }

    public void deleteMessage(Long messageId) {
        Message message = Message.findById(messageId);
        if (message == null) {
            throw new IllegalArgumentException("Message not found");
        }
        else {
            Message.deleteById(messageId);
        }
    }
}
