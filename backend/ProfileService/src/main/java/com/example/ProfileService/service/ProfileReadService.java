package com.example.ProfileService.service;

import org.springframework.cloud.context.named.NamedContextFactory.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.ProfileService.Exception.ProfileNotFoundException;
import com.example.ProfileService.common.SearchSpecification;
import com.example.ProfileService.model.Profile;
import com.example.ProfileService.model.Dto.ProfileResponsedto;
import com.example.ProfileService.model.Dto.SearchProfile;
import com.example.ProfileService.repository.ProfileRepository;
import com.example.ProfileService.service.Interface.IProfileReadService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ProfileReadService implements IProfileReadService{

    private ProfileRepository repository;
    private final RestTemplate restTemplate;

    public ProfileReadService(ProfileRepository repository, RestTemplate restTemplate){
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    @Override
    public ProfileResponsedto getProfile(Long userId) {

        Profile profile = repository.findByUserId(userId)
                        .orElseThrow(() -> 
                        new  ProfileNotFoundException("Profile Not found"));

        return mapToResponse(profile);

    }

    private long getSnippetCount() {

    String url = "http://inventory-service/inventory/vaultcount";

    HttpServletRequest request =
            ((ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes())
                    .getRequest();

    String authHeader = request.getHeader("Authorization");

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", authHeader);

    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<Long> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            Long.class
    );

    return response.getBody();
}

    private ProfileResponsedto mapToResponse(Profile profile) {

    ProfileResponsedto dto = new ProfileResponsedto();

    dto.setId(profile.getId());
    dto.setUserId(profile.getUserId());
    dto.setUsername(profile.getUsername());
    dto.setFullname(profile.getFullname());
    dto.setEmail(profile.getEmail());
    dto.setBio(profile.getBio());
    dto.setCity(profile.getCity());
    dto.setState(profile.getState());
    dto.setCountry(profile.getCountry());
    dto.setCompany(profile.getCompany());
    dto.setPhonenumber(profile.getPhonenumber());
    dto.setSolved(profile.getSolved());
    dto.setSnippets((int)getSnippetCount());
    dto.setJoinedAt(profile.getJoined_at());
    dto.setAvatarPublicId(profile.getAvatarPublicId());
    dto.setAvatarSecureUrl(profile.getAvatarSecureUrl());
    dto.setBannerPublicId(profile.getBannerPublicId());
    dto.setBannerSecureUrl(profile.getBannerSecureUrl());

    return dto;
}

    @Override
    public Page<SearchProfile> getallprofile(Pageable pageable,String searchTerm) {
        
        if(searchTerm == null || searchTerm.isEmpty()){
            return Page.empty(pageable);
        }
        
        SearchSpecification specification = new SearchSpecification(searchTerm);
        return repository.findAll(specification, pageable)
                    .map(profile -> new SearchProfile(
                        profile.getUsername(),
                        profile.getFullname(),
                        profile.getAvatarPublicId(),
                        profile.getAvatarSecureUrl(),
                        profile.getUserId()
                    ));
    }

    @Override
    public SearchProfile getConnectProfile(Long userid) {
        
        Profile profile = repository.findByUserId(userid).orElseThrow(
            () -> new ProfileNotFoundException("Profile doesn't exist")
        );

        SearchProfile profile2 = new SearchProfile();
        profile2.setUserId(profile.getUserId());
        profile2.setUsername(profile.getUsername());
        profile2.setFullname(profile.getFullname());
        profile2.setAvatarpublicid(profile.getAvatarPublicId());
        profile2.setAvatarsecureurl(profile.getAvatarSecureUrl());

        return profile2;
    }
    
}
