package fpt.project.bsmart.entity.response;

import fpt.project.bsmart.entity.common.ECandicateStatus;

public class CandicateResponse {
    private AccountResponse teacher;
    private ECandicateStatus status;

    public AccountResponse getTeacher() {
        return teacher;
    }

    public void setTeacher(AccountResponse teacher) {
        this.teacher = teacher;
    }

    public ECandicateStatus getStatus() {
        return status;
    }

    public void setStatus(ECandicateStatus status) {
        this.status = status;
    }
}
