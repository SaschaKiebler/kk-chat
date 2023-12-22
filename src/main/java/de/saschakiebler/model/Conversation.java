package de.saschakiebler.model;


import java.util.List;
import java.util.Objects;
import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

/**
 * Represents a conversation in the chat application.
 * A conversation has a list of messages, a timestamp, and a name.
 */
@Entity
public class Conversation extends PanacheEntity {
    

@OneToMany(mappedBy = "conversation")
    public List<Message> messages;

    @Column
    public LocalDateTime timestamp;

    @Column
    public String name;

    public Conversation() {
        this.timestamp = LocalDateTime.now();
        this.name = "new Conversation";
        this.messages = List.of();
    }

    public Conversation(String name) {
        this.name = name;
        this.timestamp = LocalDateTime.now();
        this.messages = List.of();
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public void removeMessage(Message message) {
        this.messages.remove(message);
    }

    public Long getId() {
        return this.id;
    }

    public List<Message> getMessages() {
        return this.messages;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public String getName() {
        return this.name;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Conversation)) {
            return false;
        }
        Conversation conversation = (Conversation) o;
        return id == conversation.id && messages.equals(conversation.messages) && timestamp.equals(conversation.timestamp) && name.equals(conversation.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, messages, timestamp, name);
    }
    

}
