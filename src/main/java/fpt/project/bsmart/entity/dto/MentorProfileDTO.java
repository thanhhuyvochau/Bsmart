package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.constant.EMentorProfileStatus;

import java.util.List;

public class MentorProfileDTO {
    private Long id;
    private String fullName;
    private String introduce;
    private String workingExperience;

    private EMentorProfileStatus status;

    private List<MentorSkillDto> mentorSkills;

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

    public List<MentorSkillDto> getMentorSkills() {
        return mentorSkills;
    }

    public void setMentorSkills(List<MentorSkillDto> mentorSkills) {
        this.mentorSkills = mentorSkills;
    }

    public EMentorProfileStatus getStatus() {
        return status;
    }

    public void setStatus(EMentorProfileStatus status) {
        this.status = status;
    }
}
