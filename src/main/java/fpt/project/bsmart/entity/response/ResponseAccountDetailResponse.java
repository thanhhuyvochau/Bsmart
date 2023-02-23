package fpt.project.bsmart.entity.response;

import java.util.List;


public class ResponseAccountDetailResponse {


    private AccountDetailResponse accountDetail ;
    private List<FeedbackAccountLogResponse> feedbackAccountLog ;

    public AccountDetailResponse getAccountDetail() {
        return accountDetail;
    }

    public void setAccountDetail(AccountDetailResponse accountDetail) {
        this.accountDetail = accountDetail;
    }

    public List<FeedbackAccountLogResponse> getFeedbackAccountLog() {
        return feedbackAccountLog;
    }

    public void setFeedbackAccountLog(List<FeedbackAccountLogResponse> feedbackAccountLog) {
        this.feedbackAccountLog = feedbackAccountLog;
    }
}
