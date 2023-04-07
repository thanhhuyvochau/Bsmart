package fpt.project.bsmart.entity.request;


import java.util.ArrayList;
import java.util.List;

public class UpdateMentorProfileRequest {
    private String introduce;
    private String workingExperience;
    private List<UpdateSkillRequest> mentorSkills = new ArrayList<>();

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
