package fpt.project.bsmart.entity.request.mentorprofile;

import fpt.project.bsmart.entity.constant.EGenderType;
import fpt.project.bsmart.entity.request.UpdateSkillRequest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MentorRequestEditProfileRequest {

    private String fullName;
    private Instant birthday;
    private String address;
    private String phone;
    private EGenderType gender ;

    private String linkedinLink;
    private String facebookLink;
    private String website;


    private String introduce;
    private String workingExperience;
    private List<UpdateSkillRequest> mentorSkills = new ArrayList<>();

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getWorkingExperience() {
        return workingExperience;
    }

    public void setWorkingExperience(String workingExperience) {
        this.workingExperience = workingExperience;
    }

    public List<UpdateSkillRequest> getMentorSkills() {
        return mentorSkills;
    }

    public void setMentorSkills(List<UpdateSkillRequest> mentorSkills) {
        this.mentorSkills = mentorSkills;
    }
}
