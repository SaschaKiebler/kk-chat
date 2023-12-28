package de.saschakiebler.model;


import java.util.List;

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
    }

    public Conversation(Long id, String name, LocalDateTime timestamp, List<Message> messages) {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
        this.messages = messages;

    }

    public Conversation(Long id, String name, LocalDateTime timestamp) {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
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
    

}
