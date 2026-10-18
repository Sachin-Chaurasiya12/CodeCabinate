package com.example.ConnectService.model.dto;

public class PrivateMessageRequest {

    private Long recipientId;
    private String content;

    public PrivateMessageRequest(){}
    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}