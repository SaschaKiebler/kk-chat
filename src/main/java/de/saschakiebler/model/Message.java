package de.saschakiebler.model;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

/**
 * This class represents a message in the database.
 * A message has a sender, a text, a timestamp, and a conversation.
 * The conversation is a reference to the conversation in which the message was sent.
 * A message can have different senders, which are represented by the MessageRoles enum.
 */
@Entity
public class Message extends PanacheEntity {

    /**
     * The sender of the message.
     */
    @Column(length = 40)
    public String sender;

    /**
     * The text content of the message.
     */
    @Column(length = 4000)
    public String text;

    /**
     * The timestamp when the message was sent.
     */
    @Column
    public LocalDateTime timestamp;

    /**
     * The conversation in which the message was sent.
     */
    @ManyToOne
    public Conversation conversation;

    /**
     * Default constructor.
     */
    public Message() {
    
    }

    /**
     * Constructor with sender and text parameters.
     * 
     * @param sender The sender of the message.
     * @param text The text content of the message.
     */
    public Message(String sender, String text) {
        this.sender = sender;
        this.text = text;
        this.timestamp = LocalDateTime.now(); 
    }

    /**
     * Constructor with sender, text, and conversation parameters.
     * 
     * @param sender The sender of the message.
     * @param text The text content of the message.
     * @param conversation The conversation in which the message was sent.
     */
    public Message(String sender, String text, Conversation conversation) {
        this.sender = sender;
        this.text = text;
        this.timestamp = LocalDateTime.now(); 
        this.conversation = conversation;
    }

    /**
     * Get the sender of the message.
     * 
     * @return The sender of the message.
     */
    public String getSender() {
        return sender;
    }

    /**
     * Get the text content of the message.
     * 
     * @return The text content of the message.
     */
    public String getText() {
        return text;
    }

    /**
     * Get the timestamp when the message was sent.
     * 
     * @return The timestamp when the message was sent.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Get the conversation in which the message was sent.
     * 
     * @return The conversation in which the message was sent.
     */
    public Conversation getConversation() {
        return conversation;
    }

    /**
     * Set the sender of the message.
     * 
     * @param sender The sender of the message.
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Set the text content of the message.
     * 
     * @param text The text content of the message.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Set the timestamp when the message was sent.
     * 
     * @param timestamp The timestamp when the message was sent.
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Set the conversation in which the message was sent.
     * 
     * @param conversation The conversation in which the message was sent.
     */
    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
}

