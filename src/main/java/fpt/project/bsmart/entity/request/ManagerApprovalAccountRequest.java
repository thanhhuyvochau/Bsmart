package fpt.project.bsmart.entity.request;


import fpt.project.bsmart.entity.constant.EAccountStatus;

import java.io.Serializable;

public class ManagerApprovalAccountRequest implements Serializable {

    private EAccountStatus status;
    private String message ;

    public EAccountStatus getStatus() {
        return status;
    }

    public void setStatus(EAccountStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
