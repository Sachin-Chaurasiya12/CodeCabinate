package com.example.ConnectService.model.dto;

public class FollowingRequest {

    private Long followingid;

    public FollowingRequest(){}
    public FollowingRequest(Long followingid){
        this.followingid = followingid;
    }

    public Long getFollowingid() {
        return followingid;
    }
    public void setFollowingid(Long followingid) {
	System.out.println("Setter received followingid: " + followingid);
        this.followingid = followingid;
    }
    
}