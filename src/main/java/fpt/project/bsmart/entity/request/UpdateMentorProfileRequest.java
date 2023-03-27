package fpt.project.bsmart.entity.request;


import fpt.project.bsmart.entity.dto.MentorSkillDto;

import java.util.ArrayList;
import java.util.List;

public class UpdateMentorProfileRequest {
    private String introduce;
    private String workingExperiences;
    private List<MentorSkillDto> mentorSkills = new ArrayList<>();

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getWorkingExperiences() {
        return workingExperiences;
    }

    public void setWorkingExperiences(String workingExperiences) {
        this.workingExperiences = workingExperiences;
    }

    public List<MentorSkillDto> getMentorSkills() {
        return mentorSkills;
    }

    public void setMentorSkills(List<MentorSkillDto> mentorSkills) {
        this.mentorSkills = mentorSkills;
    }
}
