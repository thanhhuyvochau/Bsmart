package fpt.project.bsmart.entity.request;


import java.io.Serializable;
import java.util.List;

public class MentorSendSkillRequest implements Serializable {

    List<Long> skillIds;
    List<Long> degreeIds;


    public List<Long> getSkillIds() {
        return skillIds;
    }

    public void setSkillIds(List<Long> skillIds) {
        this.skillIds = skillIds;
    }

    public List<Long> getDegreeIds() {
        return degreeIds;
    }

    public void setDegreeIds(List<Long> degreeIds) {
        this.degreeIds = degreeIds;
    }


}
