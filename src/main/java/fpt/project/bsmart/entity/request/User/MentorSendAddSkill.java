package fpt.project.bsmart.entity.request.User;

import fpt.project.bsmart.entity.request.UpdateSkillRequest;

import java.util.ArrayList;
import java.util.List;

public class MentorSendAddSkill {
    private List<UpdateSkillRequest> mentorSkills = new ArrayList<>();


    public List<UpdateSkillRequest> getMentorSkills() {
        return mentorSkills;
    }

    public void setMentorSkills(List<UpdateSkillRequest> mentorSkills) {
        this.mentorSkills = mentorSkills;
    }

}
