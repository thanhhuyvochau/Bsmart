package fpt.project.bsmart.entity.response.member;

import fpt.project.bsmart.entity.constant.EGenderType;
import fpt.project.bsmart.entity.dto.ImageDto;
import fpt.project.bsmart.entity.dto.MentorProfileDTO;
import fpt.project.bsmart.entity.dto.WalletDto;
import fpt.project.bsmart.entity.dto.mentor.TeachInformationDTO;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MemberDetailResponse {
    private Long id;

    private String fullName;


    private String email;

    private Instant birthday;

    private String address;

    private String phone;

    private Boolean status;
    private EGenderType gender;

    private String linkedinLink;

    private String facebookLink;

    private String website;

    private List<ImageDto> userImages = new ArrayList<>();

    private StudyInformationDTO studyInformation;

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

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<ImageDto> getUserImages() {
        return userImages;
    }

    public void setUserImages(List<ImageDto> userImages) {
        this.userImages = userImages;
    }

    public StudyInformationDTO getStudyInformation() {
        return studyInformation;
    }

    public void setStudyInformation(StudyInformationDTO studyInformation) {
        this.studyInformation = studyInformation;
    }
}
