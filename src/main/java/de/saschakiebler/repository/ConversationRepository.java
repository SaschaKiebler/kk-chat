package de.saschakiebler.repository;

import java.util.List;
import de.saschakiebler.model.Conversation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;


@Transactional
@ApplicationScoped
public class ConversationRepository {
    

    public Conversation createConversation(Conversation conversation) {
        Conversation.persist(conversation);
        return conversation;
    }

    public Conversation getConversationById(Long conversationId) {
        Conversation conversation = Conversation.findById(conversationId);
        return conversation;
    }

    public List<Conversation> getAllConversations() {
        List<Conversation> conversations = Conversation.listAll();
        return conversations;
    }

    public Conversation updateConversation(Conversation conversation) {
        Conversation.update("name = ?1 where id = ?2", conversation.name, conversation.id);
        Conversation returnConversation = Conversation.findById(conversation.id);
        if (returnConversation == null) {
            throw new IllegalArgumentException("Conversation not found");
        }
        else {
            return returnConversation;
        }
    }

    public void deleteConversation(Long conversationId) {
        Conversation conversation = Conversation.findById(conversationId);
        if (conversation == null) {
            throw new IllegalArgumentException("Conversation not found");
        }
        else {
            Conversation.deleteById(conversationId);
        }
    }


}
