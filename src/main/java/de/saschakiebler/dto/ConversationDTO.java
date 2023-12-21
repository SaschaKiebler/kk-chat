package de.saschakiebler.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ConversationDTO {
    private Long id;
    private List<MessageDTO> messages;
    private LocalDateTime timestamp;

    public ConversationDTO() {
    }

    public ConversationDTO(Long id, List<MessageDTO> messages, LocalDateTime timestamp) {
        this.id = id;
        this.messages = messages;
        this.timestamp = timestamp;
    }

    public ConversationDTO(Long id, List<MessageDTO> messages) {
        this.id = id;
        this.messages = messages;
        }

    public Long getId() {
        return this.id;
    }

    public List<MessageDTO> getMessages() {
        return this.messages;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessages(List<MessageDTO> messages) {
        this.messages = messages;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }



}
