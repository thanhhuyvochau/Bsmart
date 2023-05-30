package fpt.project.bsmart.entity.request;


import fpt.project.bsmart.entity.constant.ECourseStatus;

import java.io.Serializable;

public class ManagerApprovalCourseRequest implements Serializable {

    private ECourseStatus status;
    private String message ;

    public ECourseStatus getStatus() {
        return status;
    }

    public void setStatus(ECourseStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
