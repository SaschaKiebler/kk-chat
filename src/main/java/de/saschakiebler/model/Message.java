package de.saschakiebler.model;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Message extends PanacheEntity{

    @Column(length = 40)
    public String sender;

    @Column(length = 255)
    public String text;

    @Column
    public LocalDateTime timestamp;

    // Constructors
    public Message() {
        // Empty constructor
    }

    public Message(String sender, String text) {
        this.sender = sender;
        this.text = text;
        this.timestamp = LocalDateTime.now(); // Automatically set the timestamp when the message is created
    }

    // Getters
    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

