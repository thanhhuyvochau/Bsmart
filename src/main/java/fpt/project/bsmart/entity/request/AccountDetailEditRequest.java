package fpt.project.bsmart.entity.request;


import fpt.project.bsmart.entity.common.EGenderType;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public class AccountDetailEditRequest implements Serializable {


    private String firstName;
    private String lastName;
    private Instant birthDay;

    private String email;

//    private String password;
    private String phone;
    private EGenderType gender;

    private Long voiceId;

    private String currentAddress;

    private String idCard;

    private String trainingSchoolName;

    private String majors;

    private String level;

    private List<Long> subjects;
    private List<Long> classLevels;




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

//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public EGenderType getGender() {
        return gender;
    }

    public void setGender(EGenderType gender) {
        this.gender = gender;
    }


    public Long getVoiceId() {
        return voiceId;
    }

    public void setVoiceId(Long voiceId) {
        this.voiceId = voiceId;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<Long> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Long> subjects) {
        this.subjects = subjects;
    }

    public List<Long> getClassLevels() {
        return classLevels;
    }

    public void setClassLevels(List<Long> classLevels) {
        this.classLevels = classLevels;
    }

//    public List<UploadAvatarRequest> getFiles() {
//        return files;
//    }
//
//    public void setFiles(List<UploadAvatarRequest> files) {
//        this.files = files;
//    }

}
