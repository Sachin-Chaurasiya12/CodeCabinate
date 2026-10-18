package com.example.ProfileService.service.Interface;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.ProfileService.model.Dto.ProfileResponsedto;
import com.example.ProfileService.model.Dto.SearchProfile;

public interface IProfileReadService {

    ProfileResponsedto getProfile(Long userId);
    Page<SearchProfile> getallprofile(Pageable pageable,String searchTerm);
    SearchProfile getConnectProfile(Long userid);
    
}