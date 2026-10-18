package com.example.ConnectService.model.dto;

public class SendMessageRequest {
    private String content;
    private Long replyToMessageId;    

    public SendMessageRequest(){}

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Long getReplyToMessageId() {
        return replyToMessageId;
    }
    public void setReplyToMessageId(Long replyToMessageId) {
        this.replyToMessageId = replyToMessageId;
    }
}
