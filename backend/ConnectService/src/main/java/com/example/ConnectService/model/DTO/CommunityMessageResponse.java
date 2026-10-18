package com.example.ConnectService.model.dto;

import java.time.LocalDateTime;

public class CommunityMessageResponse {
    
    private Long id;
    private Long senderUserId;
    private String content;
    private Long replyUserId;
    private LocalDateTime createdAt;

    public CommunityMessageResponse(){}

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getReplyUserId() {
        return replyUserId;
    }
    public Long getSenderUserId() {
        return senderUserId;
    }
    public void setReplyUserId(Long replyUserId) {
        this.replyUserId = replyUserId;
    }
    public void setSenderUserId(Long senderUserId) {
        this.senderUserId = senderUserId;
    }
}
