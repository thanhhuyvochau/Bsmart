package fpt.project.bsmart.entity.request;


import fpt.project.bsmart.entity.constant.EMentorProfileStatus;

import java.io.Serializable;

public class ManagerApprovalAccountRequest implements Serializable {

    private EMentorProfileStatus status;
    private String message;

    public EMentorProfileStatus getStatus() {
        return status;
    }

    public void setStatus(EMentorProfileStatus status) {
        this.status = status;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
