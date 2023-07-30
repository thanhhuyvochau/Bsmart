package fpt.project.bsmart.entity.request;


import fpt.project.bsmart.entity.constant.EMentorProfileStatus;

import java.io.Serializable;
import java.util.List;

public class ManagerApprovalSkillRequest implements Serializable {

    List<Long> skillIds;
    List<Long> degreeIds;
    private Boolean status;
    private String message;

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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
