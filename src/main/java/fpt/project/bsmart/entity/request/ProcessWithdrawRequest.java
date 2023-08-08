package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.constant.ETransactionStatus;

public class ProcessWithdrawRequest {
    private Long id;
    private ETransactionStatus status;
    private String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ETransactionStatus getStatus() {
        return status;
    }

    public void setStatus(ETransactionStatus status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
