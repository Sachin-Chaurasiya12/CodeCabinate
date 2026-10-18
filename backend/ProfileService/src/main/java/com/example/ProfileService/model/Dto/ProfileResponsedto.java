package com.example.ProfileService.model.Dto;

import java.time.LocalDateTime;

public class ProfileResponsedto {

    private Long id;
    private Long userId;
    private String username;
    private String fullname;
    private String email;
    private String bio;
    private Integer snippets;
    private Integer solved;
    private String phonenumber;
    private String city;
    private String state;
    private String country;
    private String company;

    private String avatarPublicId;
    private String avatarSecureUrl;

    private String bannerPublicId;
    private String bannerSecureUrl;

    private LocalDateTime joined_at;

    public ProfileResponsedto() {
    }


    public ProfileResponsedto(Long id, Long userId, String username, String fullname,
                              String email, String bio, Integer snippets,
                              Integer solved, String phonenumber, String city,
                              String state, String country, String company,
                              String avatarPublicId, String avatarSecureUrl,
                              String bannerPublicId, String bannerSecureUrl,
                              LocalDateTime joined_at) {

        this.id = id;
        this.userId = userId;
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.bio = bio;
        this.snippets = snippets;
        this.solved = solved;
        this.phonenumber = phonenumber;
        this.city = city;
        this.state = state;
        this.country = country;
        this.company = company;
        this.avatarPublicId = avatarPublicId;
        this.avatarSecureUrl = avatarSecureUrl;
        this.bannerPublicId = bannerPublicId;
        this.bannerSecureUrl = bannerSecureUrl;
        this.joined_at = joined_at;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Integer getSnippets() {
        return snippets;
    }

    public void setSnippets(Integer snippets) {
        this.snippets = snippets;
    }

    public Integer getSolved() {
        return solved;
    }

    public void setSolved(Integer solved) {
        this.solved = solved;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAvatarPublicId() {
        return avatarPublicId;
    }

    public void setAvatarPublicId(String avatarPublicId) {
        this.avatarPublicId = avatarPublicId;
    }

    public String getAvatarSecureUrl() {
        return avatarSecureUrl;
    }

    public void setAvatarSecureUrl(String avatarSecureUrl) {
        this.avatarSecureUrl = avatarSecureUrl;
    }

    public String getBannerPublicId() {
        return bannerPublicId;
    }

    public void setBannerPublicId(String bannerPublicId) {
        this.bannerPublicId = bannerPublicId;
    }

    public String getBannerSecureUrl() {
        return bannerSecureUrl;
    }

    public void setBannerSecureUrl(String bannerSecureUrl) {
        this.bannerSecureUrl = bannerSecureUrl;
    }

    public LocalDateTime getJoinedAt() {
        return joined_at;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joined_at = joinedAt;
    }
}