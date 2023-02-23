package fpt.project.bsmart.entity.response;

import fpt.project.bsmart.entity.common.EFeedbackAccountLogStatus;

import java.time.Instant;

public class FeedbackAccountLogResponse {

    private Long id;



    private String content;


    private EFeedbackAccountLogStatus status;




    private Long account;



    private Long accountDetail;

    private Instant createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EFeedbackAccountLogStatus getStatus() {
        return status;
    }

    public void setStatus(EFeedbackAccountLogStatus status) {
        this.status = status;
    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }

    public Long getAccountDetail() {
        return accountDetail;
    }

    public void setAccountDetail(Long accountDetail) {
        this.accountDetail = accountDetail;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }
}
