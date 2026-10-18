package com.example.ConnectService.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import com.example.ConnectService.model.ConnectChat;

import jakarta.persistence.LockModeType;

@Repository 
public interface ConnectionRepo extends JpaRepository<ConnectChat,Long>{

    boolean existsByFolloweridAndFollowingid(Long userid,Long followingid);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<ConnectChat> findByFolloweridOrFollowingid(Long followerid,Long followingid);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<ConnectChat> findByFollowerid(Long userid);
    
}
