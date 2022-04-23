package com.example.voiceassistant.model;

public class chatMessage {
    String message;
    Boolean isUser;

    public chatMessage(String message, Boolean isUser) {
        this.message = message;
        this.isUser = isUser;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getIsUser() {
        return isUser;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
