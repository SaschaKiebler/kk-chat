package de.saschakiebler.service;

import java.util.List;

import de.saschakiebler.model.Conversation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@Transactional
@ApplicationScoped
public class ConversationService {
    
    @Inject
    MessageService messageService;

   
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
        return conversations;
    }


}