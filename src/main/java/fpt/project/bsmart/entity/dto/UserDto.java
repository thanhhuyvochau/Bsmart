package fpt.project.bsmart.entity.dto;


import fpt.project.bsmart.entity.constant.EGenderType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class UserDto {
    private static final long serialVersionUID = 1L;

    private Long id;


    private String fullName;

    private String email;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant birthday;


    private String address;

    private String phone;

    private Boolean status;
    private EGenderType gender;

    List<RoleDto> roles = new ArrayList<>();



    private String linkedinLink;

    private String facebookLink;


    private List<ImageDto> userImages = new ArrayList<>();

    private WalletDto wallet;

    private MentorProfileDTO mentorProfile;
    private boolean isVerified = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    /*
    public String getIntroduce() {
        return introduce;
    }
    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
    */

    public Instant getBirthday() {
        return birthday;
    }

    public void setBirthday(Instant birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDto> roles) {
        this.roles = roles;
    }



    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public EGenderType getGender() {
        return gender;
    }

    public void setGender(EGenderType gender) {
        this.gender = gender;
    }

    public String getLinkedinLink() {
        return linkedinLink;
    }

    public void setLinkedinLink(String linkedinLink) {
        this.linkedinLink = linkedinLink;
    }

    public WalletDto getWallet() {
        return wallet;
    }

    public void setWallet(WalletDto wallet) {
        this.wallet = wallet;
    }

    public List<ImageDto> getUserImages() {
        return userImages;
    }

    public void setUserImages(List<ImageDto> userImages) {
        this.userImages = userImages;
    }

    public MentorProfileDTO getMentorProfile() {
        return mentorProfile;
    }

    public void setMentorProfile(MentorProfileDTO mentorProfile) {
        this.mentorProfile = mentorProfile;
    }

    public boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean verified) {
        isVerified = verified;
    }
}
