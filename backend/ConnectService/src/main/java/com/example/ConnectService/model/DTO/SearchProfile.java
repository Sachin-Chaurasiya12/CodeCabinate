package com.example.ConnectService.model.dto;

public class SearchProfile {
    
    private Long userId;
    private String username;
    private String fullname;
    private String avatarpublicid;
    private String avatarsecureurl;
    
    public SearchProfile(){}

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getAvatarpublicid() {
        return avatarpublicid;
    }
    public void setAvatarpublicid(String avatarpublicid) {
        this.avatarpublicid = avatarpublicid;
    }
    public String getAvatarsecureurl() {
        return avatarsecureurl;
    }
    public void setAvatarsecureurl(String avatarsecureurl) {
        this.avatarsecureurl = avatarsecureurl;
    }
    public String getFullname() {
        return fullname;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
