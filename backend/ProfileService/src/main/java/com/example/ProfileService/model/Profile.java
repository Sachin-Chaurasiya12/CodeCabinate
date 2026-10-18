package com.example.ProfileService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "profiles",indexes = @Index(name="idx_userId", columnList = "userId"))

public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String fullname;

    @Size(max = 225, message = "Bio cannot exceed 225 characters")
    private String bio;

    private Integer snippets;

    private Integer solved;
    @Pattern(
        regexp = "^[0-9]{10}$",
        message = "Phone number must be exactly 10 digits"
    )
    private String phonenumber;

    private String city;

    private String state;

    private String country;

    private String company;

    @Column(name = "avatar_public_id")
    private String avatarPublicId;

    @Column(name = "avatar_secure_url")
    private String avatarSecureUrl;

    @Column(name = "banner_public_id")
    private String bannerPublicId;

    @Column(name = "banner_secure_url")
    private String bannerSecureUrl;

    @Lob
    @Column(name = "userId")
    private Long userId;

    private String email;

    @Column(name = "joined_at")
    private LocalDateTime joined_at;

    public Profile() {
    }

    public Profile(Long id, String username, String fullname, String bio, Integer snippets,
                   Integer solved, String phonenumber, String city, String state,
                   String country, String company, String avatarPublicId,
                   String avatarSecureUrl, String bannerPublicId,
                   String bannerSecureUrl,
                   Long userId, String email,
                   LocalDateTime joined_at) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
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
        this.userId = userId;
        this.email = email;
        this.joined_at = joined_at;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getJoined_at() {
        return joined_at;
    }

    public void setJoined_at(LocalDateTime joined_at) {
        this.joined_at = joined_at;
    }
}