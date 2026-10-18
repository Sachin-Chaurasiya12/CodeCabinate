package com.example.ConnectService.model;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity 
@Table(
    name = "connection", 
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"follower_id","following_id"}
    )

)
public class ConnectChat {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "follower_id")
    private Long followerid;

    @Column(name = "following_id")
    private Long followingid;

    @Column(name = "status")
    private Statusvalue status;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    public ConnectChat(){}
    public ConnectChat(Long followingid, Statusvalue status,Long followerid, 
        LocalDateTime created_at){
        this.created_at = created_at;
        this.followingid = followingid;
        this.status = status;
        this.followerid = followerid;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }
    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
    public Long getFollowingid() {
        return followingid;
    }
    public void setFollowingid(Long followingid) {
        this.followingid = followingid;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Statusvalue getStatus() {
        return status;
    }
    public void setStatus(Statusvalue status) {
        this.status = status;
    }
   
    public Long getFollowerid() {
        return followerid;
    }
    public void setFollowerid(Long followerid) {
        this.followerid = followerid;
    }

}
