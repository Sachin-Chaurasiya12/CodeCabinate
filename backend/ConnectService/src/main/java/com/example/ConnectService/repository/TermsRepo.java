package com.example.ConnectService.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ConnectService.model.Terms;

@Repository 
public interface  TermsRepo extends JpaRepository<Terms,Long>{

    Optional<Terms> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
