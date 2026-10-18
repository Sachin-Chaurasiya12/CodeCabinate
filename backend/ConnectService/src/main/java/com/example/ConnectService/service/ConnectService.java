package com.example.ConnectService.service;


import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.ConnectService.model.ConnectChat;
import com.example.ConnectService.model.Statusvalue;
import com.example.ConnectService.model.dto.Connectiondata;
import com.example.ConnectService.model.dto.PageResponse;
import com.example.ConnectService.model.dto.SearchProfile;
import com.example.ConnectService.repository.ConnectionRepo;
import com.example.ConnectService.service.Interface.IConnectService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service 
public class ConnectService implements IConnectService{


    private final ConnectionRepo repo;
    @Autowired 
    private RestTemplate restTemplate;

    public ConnectService(ConnectionRepo repo){
        this.repo = repo;
    }
    @Override
    public Page<SearchProfile> getSearchProfile(int size,int page,String searchterm) {

        try {
            String url = "http://PROFILE-SERVICE/profile/searchprofile";

            HttpServletRequest request =
            ((ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes())
                    .getRequest();

            String authHeader = request.getHeader("Authorization");

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization",authHeader);
            
            HttpEntity entity = new HttpEntity<>(headers);

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("size", size)
                .queryParam("page", page)
                .queryParam("searchterm", searchterm);

            URI uri = builder.build().toUri();

            ResponseEntity<PageResponse<SearchProfile>> response = restTemplate.exchange(
                uri, 
                HttpMethod.GET, 
                entity,
                new ParameterizedTypeReference<PageResponse<SearchProfile>>() {}
            );
            
            PageResponse<SearchProfile> body = response.getBody();

            if(body == null){
                return Page.empty();
            }

            return new PageImpl<>(
                body.getContent(),
                PageRequest.of(
                    body.getNumber(), 
                    body.getSize()),
                body.getTotalElements()
            );

        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            return Page.empty();
        }
        
    }

    @Override
    public Connectiondata connectprofiles(Long userid,Long followingid) {

        boolean usertofollowing = repo.existsByFolloweridAndFollowingid(userid, followingid);

        if(!usertofollowing){

            ConnectChat chat = new ConnectChat();

            chat.setFollowingid(followingid);
            chat.setFollowerid(userid);
            chat.setCreated_at(LocalDateTime.now());
            chat.setStatus(Statusvalue.ACCEPTED);

            repo.save(chat);
        }   

        boolean followingtouser = repo.existsByFolloweridAndFollowingid(followingid, userid);

        Boolean canChat = false;

        if(usertofollowing || followingtouser){
            canChat = true;
        }   

        Connectiondata data = new Connectiondata();

        data.setCanChat(canChat);
        data.setFollowerid(userid);
        data.setFollowingid(followingid);

        return data;
    }

    @Transactional 
    @Override
    public List<SearchProfile> getFollowerandfollowinglist(Long userid) {

        List<ConnectChat> connections = repo.findByFollowerid(userid);

        List<SearchProfile> profiles = new ArrayList<>();

        HttpServletRequest request = ((ServletRequestAttributes)
            RequestContextHolder.currentRequestAttributes())
            .getRequest();
        
        String authHeader = request.getHeader("Authorization");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);

        HttpEntity entity = new HttpEntity<>(headers);

        for(ConnectChat chat : connections){

            Long OtherUserid = chat.getFollowingid();

            try {

            String url =  "http://PROFILE-SERVICE/profile/chatprofile/" + OtherUserid;

            ResponseEntity<SearchProfile> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                SearchProfile.class
                );

            SearchProfile profile = response.getBody();

            if(profile != null){
                profiles.add(profile);
            }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return profiles;
    }

}
