package fpt.project.bsmart.entity.request.mentorprofile;


import fpt.project.bsmart.entity.constant.EGenderType;
import fpt.project.bsmart.entity.constant.EMentorProfileEditStatus;
import fpt.project.bsmart.entity.dto.mentor.MentorProfileRequestEditDTO;

import java.time.Instant;


public class ManagerSearchEditProfileRequest {
    private String q;
    private EMentorProfileEditStatus status;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public EMentorProfileEditStatus getStatus() {
        return status;
    }

    public void setStatus(EMentorProfileEditStatus status) {
        this.status = status;
    }
}
