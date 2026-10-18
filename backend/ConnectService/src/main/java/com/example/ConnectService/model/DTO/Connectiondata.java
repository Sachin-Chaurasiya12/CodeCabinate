package com.example.ConnectService.model.dto;

public class Connectiondata {
    
    private boolean canChat;
    private Long followingid;
    private Long followerid;

    public Connectiondata(boolean canChat,Long followingid,Long followerid){
        this.canChat = canChat;
        this.followerid = followerid;
        this.followingid = followingid;
    }
    public Connectiondata(){}

    public Long getFollowerid() {
        return followerid;
    }
    public void setCanChat(boolean canChat) {
        this.canChat = canChat;
    }
    public Long getFollowingid() {
        return followingid;
    }
    public void setFollowerid(Long followerid) {
        this.followerid = followerid;
    }
    public void setFollowingid(Long followingid) {
        this.followingid = followingid;
    }
    public boolean getCanChat() {
        return canChat;
    }
}
