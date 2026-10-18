package com.example.ConnectService.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ConnectService.model.dto.Connectiondata;
import com.example.ConnectService.model.dto.FollowingRequest;
import com.example.ConnectService.model.dto.SearchProfile;
import com.example.ConnectService.service.Interface.IConnectService;


@RestController 
@RequestMapping("/connect")
public class ConnectController {

    private IConnectService service;

    public ConnectController(IConnectService service){
        this.service = service;
    }
    
    @GetMapping("/userprofile")
    public Page<SearchProfile> searchProfile(int page,int size,String searchterm){

        Page<SearchProfile> profile = service.getSearchProfile(size, page, searchterm);

        return profile;
    }


    @PostMapping("/followuser")
    public Connectiondata connectprofiles(
        Authentication authentication,
        @RequestBody FollowingRequest request
    ) {
        
        Long userid = (Long)authentication.getPrincipal();
        Connectiondata data = service.connectprofiles(userid, request.getFollowingid());

        return data;
    }

    @GetMapping("/displaychat")
    public List<SearchProfile> getFollowerandfollowinglist(Authentication authentication) {
        Long userid = (Long)authentication.getPrincipal();

        List<SearchProfile> data = service.getFollowerandfollowinglist(userid);

        return data;
    }
    
    
}
