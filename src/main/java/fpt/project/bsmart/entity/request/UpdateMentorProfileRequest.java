package fpt.project.bsmart.entity.request;


import fpt.project.bsmart.entity.dto.MentorSkillDto;

import java.util.ArrayList;
import java.util.List;

public class UpdateMentorProfileRequest {
    private String introduce;
    private String yearOfExperiences;
    private List<MentorSkillDto> mentorSkills = new ArrayList<>();

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getYearOfExperiences() {
        return yearOfExperiences;
    }

    public void setYearOfExperiences(String yearOfExperiences) {
        this.yearOfExperiences = yearOfExperiences;
    }

    public List<MentorSkillDto> getMentorSkills() {
        return mentorSkills;
    }

    public void setMentorSkills(List<MentorSkillDto> mentorSkills) {
        this.mentorSkills = mentorSkills;
    }
}
