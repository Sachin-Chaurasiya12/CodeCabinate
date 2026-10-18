package com.example.ProfileService.common;

import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import com.example.ProfileService.model.Profile;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class SearchSpecification implements Specification<Profile> {

    private final String searchterm;

    public SearchSpecification(String searchterm){
        this.searchterm = searchterm;
    }
    @Override
    public Predicate toPredicate(Root<Profile> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        String pattern = "%"+ searchterm.toLowerCase() + "%";

        return cb.or(
                cb.like(cb.lower(root.get("username")), pattern),
                cb.like(cb.lower(root.get("fullname")), pattern)
            );

    }


    
}
