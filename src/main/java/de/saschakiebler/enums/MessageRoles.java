package de.saschakiebler.enums;

public enum MessageRoles {
    USER("User"),
    ASSISTANT("Assistant");

    private String role;

    MessageRoles(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
