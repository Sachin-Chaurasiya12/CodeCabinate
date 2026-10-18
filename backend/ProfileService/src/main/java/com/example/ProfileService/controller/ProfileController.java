package com.example.ProfileService.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ProfileService.model.Profile;
import com.example.ProfileService.model.Dto.Image;
import com.example.ProfileService.model.Dto.ProfileRequestdto;
import com.example.ProfileService.model.Dto.ProfileResponsedto;
import com.example.ProfileService.model.Dto.SearchProfile;
import com.example.ProfileService.service.Interface.IProfileReadService;
import com.example.ProfileService.service.Interface.IProfileWriteService;


@RestController
@RequestMapping("/profile")
public class ProfileController {

    private IProfileWriteService service;
    private IProfileReadService readService;
    public ProfileController(IProfileWriteService service,IProfileReadService readService){
        this.service = service;
        this.readService = readService;
    }
    
    //Post Mapping
    @PostMapping
    public ResponseEntity<String> createDefautProfile(@RequestBody ProfileRequestdto requestdto) {

        service.createDefaultProfile(requestdto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Success");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateProfile(
        @RequestBody ProfileRequestdto requestdto,
        Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();
        service.updateProfile(requestdto,userId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Success");
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map> uploadAvatar(Image image, Authentication authentication){
        try {
            Long userId = (Long) authentication.getPrincipal();
            return service.uploadFile(image, userId);
    
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping(value = "/uploadBanner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map> uploadBanner(Image image, Authentication authentication){
        try {
            Long userId = (Long) authentication.getPrincipal();
            return service.uploadBanner(image, userId);
    
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("error", e.getMessage()));
        }
    }
    
    //Get Mapping
    @GetMapping
    public ResponseEntity<ProfileResponsedto> getUserProfile(Authentication authentication){

        Long userId = (Long) authentication.getPrincipal();
        ProfileResponsedto response = readService.getProfile(userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/searchprofile")
    public Page<SearchProfile> fetchprofile(
        @RequestParam(defaultValue = "15") int size,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(required = false) String searchterm
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<SearchProfile> responsedto = readService.getallprofile(pageable, searchterm);

        return responsedto;
    }

    @GetMapping("/chatprofile/{userid}")
    public SearchProfile connectedprofile(@PathVariable Long userid){
        
        SearchProfile profile = readService.getConnectProfile(userid);

        return profile;
    }
}
