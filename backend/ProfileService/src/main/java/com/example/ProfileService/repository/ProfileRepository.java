package com.example.ProfileService.repository;

import java.util.Optional;

import org.hibernate.annotations.Fetch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;

import com.example.ProfileService.model.Profile;

import jakarta.persistence.QueryHint;

public interface ProfileRepository extends JpaRepository<Profile, Long> ,JpaSpecificationExecutor<Profile>{

    Optional<Profile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    @Override 
    @QueryHints(@QueryHint(name = "org.hibernate.fetchSize", value = "15"))
    Page<Profile> findAll(Specification<Profile> spec,Pageable pageable);

}