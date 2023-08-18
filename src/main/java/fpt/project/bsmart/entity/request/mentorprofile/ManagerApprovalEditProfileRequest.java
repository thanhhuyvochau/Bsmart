package fpt.project.bsmart.entity.request.mentorprofile;


import fpt.project.bsmart.entity.constant.EMentorProfileEditStatus;
import fpt.project.bsmart.entity.constant.EMentorProfileStatus;

import java.io.Serializable;

public class ManagerApprovalEditProfileRequest implements Serializable {

    private EMentorProfileEditStatus status;
    private String message;

    public EMentorProfileEditStatus getStatus() {
        return status;
    }

    public void setStatus(EMentorProfileEditStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
