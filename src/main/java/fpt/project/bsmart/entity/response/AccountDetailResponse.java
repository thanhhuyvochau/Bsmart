package fpt.project.bsmart.entity.response;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.EAccountDetailStatus;
import fpt.project.bsmart.entity.dto.ResourceDto;
import fpt.project.bsmart.entity.dto.SubjectDto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class AccountDetailResponse {

    private Long id;

    private Long accountId;

    private String userName;

    private Boolean isKeycloak;

    private EAccountDetailStatus status;
    private String firstName;
    private String lastName;


    private Instant birthDay;


    private String email;


    private String phone;

    private VoiceResponse voice;


    private GenderResponse gender;


    private String currentAddress;

    private String level;
    private String idCard;


    private String trainingSchoolName;


    private String majors;


    private List<ClassLevelResponse> classLevel = new ArrayList<>();


    private Boolean isActive;


    private List<SubjectDto> subjects;

    private List<ResourceDto> resources;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }


    public EAccountDetailStatus getStatus() {
        return status;
    }

    public void setStatus(EAccountDetailStatus status) {
        this.status = status;
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

    public Instant getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Instant birthDay) {
        this.birthDay = birthDay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public GenderResponse getGender() {
        return gender;
    }

    public void setGender(GenderResponse gender) {
        this.gender = gender;
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

    public String getTrainingSchoolName() {
        return trainingSchoolName;
    }

    public void setTrainingSchoolName(String trainingSchoolName) {
        this.trainingSchoolName = trainingSchoolName;
    }

    public String getMajors() {
        return majors;
    }

    public void setMajors(String majors) {
        this.majors = majors;
    }

    public List<ClassLevelResponse> getClassLevel() {
        return classLevel;
    }

    public void setClassLevel(List<ClassLevelResponse> classLevel) {
        this.classLevel = classLevel;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }


    public List<SubjectDto> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectDto> subjects) {
        this.subjects = subjects;
    }

    public List<ResourceDto> getResources() {
        return resources;
    }

    public void setResources(List<ResourceDto> resources) {
        this.resources = resources;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getKeycloak() {
        return isKeycloak;
    }

    public void setKeycloak(Boolean keycloak) {
        isKeycloak = keycloak;
    }

    public void setVoice(VoiceResponse voice) {
        this.voice = voice;
    }

    public VoiceResponse getVoice() {
        return voice;
    }
}
