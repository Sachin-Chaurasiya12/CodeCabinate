package com.example.ConnectService.service.Interface;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.ConnectService.model.dto.Connectiondata;
import com.example.ConnectService.model.dto.SearchProfile;

@Service 
public interface IConnectService {
    
    Page<SearchProfile> getSearchProfile(int size,int page,String searchterm);

    Connectiondata connectprofiles(Long userid,Long followingid);

    List<SearchProfile> getFollowerandfollowinglist(Long userid);
        
}
