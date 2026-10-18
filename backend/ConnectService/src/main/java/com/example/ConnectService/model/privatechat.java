package com.example.ConnectService.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "private_chats" )
public class privatechat {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long fromWhom;
    private Long toWhom;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Boolean isActive = true;

    public privatechat(){}
    public privatechat(Long fromWhom, Long toWhom, String message){
        this.fromWhom = fromWhom;
        this.toWhom = toWhom;
        this.message = message;
    }

    @PrePersist 
    public void PrePersist(){
        createdAt = LocalDateTime.now();
    }

    public Long getFromWhom() {
        return fromWhom;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setFromWhom(Long fromWhom) {
        this.fromWhom = fromWhom;
    }
    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }
    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
    public Long getToWhom() {
        return toWhom;
    }
    public void setToWhom(Long toWhom) {
        this.toWhom = toWhom;
    }

    @Override 
    public String toString(){
        return "privatechat{from = " + fromWhom + 
        "to = "+ toWhom + 
        " message = " + 
        message +"}";
    }
}
