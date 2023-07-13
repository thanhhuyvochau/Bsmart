package fpt.project.bsmart.entity.response.course;

import java.util.List;

public class CompletenessCourseResponse {

    private Integer percentComplete;


    private Boolean allowSendingApproval;


    public Integer getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(Integer percentComplete) {
        this.percentComplete = percentComplete;
    }


    public Boolean getAllowSendingApproval() {
        return allowSendingApproval;
    }

    public void setAllowSendingApproval(Boolean allowSendingApproval) {
        this.allowSendingApproval = allowSendingApproval;
    }
}
