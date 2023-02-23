package fpt.project.bsmart.entity.response;

import fpt.project.bsmart.entity.common.EAccountDetailStatus;
import fpt.project.bsmart.entity.dto.RoleDto;

import java.time.Instant;

public class AccountResponse {

    private Long id;
    private String username;
    private Boolean isActive = false;
    private String firstName;
    private String lastName;
    private String email;

    private Instant birthday;

    private String phoneNumber;

    private RoleDto role;
    private GenderResponse gender;
    private String avatar;


    private VoiceResponse voice;


    private String currentAddress;


    private String idCard;


    private String schoolName;


    private String majors;

    private EAccountDetailStatus status;


    private String level;

    public VoiceResponse getVoice() {
        return voice;
    }

    public void setVoice(VoiceResponse voice) {
        this.voice = voice;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getMajors() {
        return majors;
    }

    public void setMajors(String majors) {
        this.majors = majors;
    }

    public EAccountDetailStatus getStatus() {
        return status;
    }

    public void setStatus(EAccountDetailStatus status) {
        this.status = status;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public RoleDto getRole() {
        return role;
    }

    public void setRole(RoleDto role) {
        this.role = role;
    }

    public GenderResponse getGender() {
        return gender;
    }

    public void setGender(GenderResponse gender) {
        this.gender = gender;
    }
}
