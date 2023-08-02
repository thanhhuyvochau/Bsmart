package fpt.project.bsmart.entity.response.mentor;

import fpt.project.bsmart.entity.MentorProfile;
import fpt.project.bsmart.entity.UserImage;
import fpt.project.bsmart.entity.constant.EGenderType;
import fpt.project.bsmart.entity.dto.*;
import fpt.project.bsmart.entity.dto.mentor.TeachInformationDTO;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ManagerGetRequestApprovalSkillResponse {
    private Long id;
    private Long mentorProfileId;

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

    private String website;

    private List<ImageDto> userImages = new ArrayList<>();

    private WalletDto wallet;

    private MentorProfileDTO mentorProfile;

    private TeachInformationDTO teachInformation ;
    private boolean isVerified = false;

   private List<MentorSkillDto> mentorSkillRequest ;

   private List<ImageDto> degreeRequest ;

   private Instant created ;

   private Integer totalSkillRequest = 0;

    private Integer totalDegreeRequest = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMentorProfileId() {
        return mentorProfileId;
    }

    public void setMentorProfileId(Long mentorProfileId) {
        this.mentorProfileId = mentorProfileId;
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

    public List<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDto> roles) {
        this.roles = roles;
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

    public WalletDto getWallet() {
        return wallet;
    }

    public void setWallet(WalletDto wallet) {
        this.wallet = wallet;
    }

    public MentorProfileDTO getMentorProfile() {
        return mentorProfile;
    }

    public void setMentorProfile(MentorProfileDTO mentorProfile) {
        this.mentorProfile = mentorProfile;
    }

    public TeachInformationDTO getTeachInformation() {
        return teachInformation;
    }

    public void setTeachInformation(TeachInformationDTO teachInformation) {
        this.teachInformation = teachInformation;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public List<MentorSkillDto> getMentorSkillRequest() {
        return mentorSkillRequest;
    }

    public void setMentorSkillRequest(List<MentorSkillDto> mentorSkillRequest) {
        this.mentorSkillRequest = mentorSkillRequest;
    }

    public List<ImageDto> getDegreeRequest() {
        return degreeRequest;
    }

    public void setDegreeRequest(List<ImageDto> degreeRequest) {
        this.degreeRequest = degreeRequest;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Integer getTotalSkillRequest() {
        return totalSkillRequest;
    }

    public void setTotalSkillRequest(Integer totalSkillRequest) {
        this.totalSkillRequest = totalSkillRequest;
    }

    public Integer getTotalDegreeRequest() {
        return totalDegreeRequest;
    }

    public void setTotalDegreeRequest(Integer totalDegreeRequest) {
        this.totalDegreeRequest = totalDegreeRequest;
    }
}
