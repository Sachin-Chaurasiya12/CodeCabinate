package com.example.ConnectService.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ConnectService.model.privatechat;

@Repository 
public interface PrivateChatRepo extends JpaRepository<privatechat,Long>{
    
    Page<privatechat> findByFromWhomAndToWhomOrToWhomAndFromWhomOrderByCreatedAtAsc(
        Long senderid,
        Long recipentid,
        Long senderid2,
        Long recipentid2,
        Pageable pageable
    );
}
