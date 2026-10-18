package com.example.ConnectService.model.dto;

import java.time.LocalDateTime;

public class PrivateMessageResponse {

    private Long senderId;
    private Long recipientId;
    private String content;
    private LocalDateTime createdAt;

    public PrivateMessageResponse(){}
    public PrivateMessageResponse(
            Long senderId,
            Long recipientId,
            String content,
            LocalDateTime createdAt) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getSenderId() {
        return senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
}