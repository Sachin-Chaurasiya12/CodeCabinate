package com.example.ProfileService.model.Dto;


public class SearchProfile {
    
    private Long userId;
    private String username;
    private String fullname;
    private String avatarpublicid;
    private String avatarsecureurl;
    
    public SearchProfile(){}

    public SearchProfile(String username2, String fullname2, String avatarPublicId2, String avatarSecureUrl2,
            Long userId2) {
        this.username = username2;
        this.fullname = fullname2;
        this.avatarpublicid = avatarPublicId2;
        this.avatarsecureurl = avatarSecureUrl2;
        this.userId = userId2;
    }

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
