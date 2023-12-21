package de.saschakiebler.dto;

import java.time.LocalDateTime;

import de.saschakiebler.enums.MessageRoles;

public class MessageDTO {
    private Long id;
    private String text;
    private String sender;
    private LocalDateTime timestamp;

    public MessageDTO() {
    }

    public MessageDTO(Long id, String text, String sender, LocalDateTime timestamp) {
        this.id = id;
        this.text = text;
        this.sender = sender;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public String getSender() {
        return this.sender;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }



}
