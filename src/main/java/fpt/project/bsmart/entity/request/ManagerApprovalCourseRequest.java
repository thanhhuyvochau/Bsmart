package fpt.project.bsmart.entity.request;


import fpt.project.bsmart.entity.constant.ECourseStatus;

import java.io.Serializable;
import java.util.List;

public class ManagerApprovalCourseRequest implements Serializable {

    private List<Long> classId ;
    private ECourseStatus status;
    private String message ;


    public List<Long> getClassId() {
        return classId;
    }

    public void setClassId(List<Long> classId) {
        this.classId = classId;
    }

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
