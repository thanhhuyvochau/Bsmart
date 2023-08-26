package fpt.project.bsmart.entity.request;


import fpt.project.bsmart.entity.constant.ECourseClassStatus;

import java.io.Serializable;
import java.util.List;

public class ManagerApprovalCourseRequest implements Serializable {

    List<Long> classIds;
    private ECourseClassStatus status;
    private String message;

    public List<Long> getClassIds() {
        return classIds;
    }

    public void setClassIds(List<Long> classIds) {
        this.classIds = classIds;
    }

    public ECourseClassStatus getStatus() {
        return status;
    }

    public void setStatus(ECourseClassStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}