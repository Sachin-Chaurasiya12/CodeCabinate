package com.example.ConnectService.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ConnectService.model.Community;

import jakarta.persistence.QueryHint;

@Repository
public interface CommuntiyRepo extends JpaRepository<Community,Long> {

    @QueryHints({
        @QueryHint(name = "org.hibernate.readOnly", value = "true")
    })
    Page<Community> findByIsActiveTrue(Pageable pageable);

    Optional<Community> findBySenderUserId(Long userId);

    
}
