package fpt.project.bsmart.entity.response;

import java.util.HashMap;

public class UserFeedbackResponse {
    private long feedbackAnswerId;
    private String userName;
    private HashMap<String, String> feedbackAnswers;

    public long getFeedbackAnswerId() {
        return feedbackAnswerId;
    }

    public void setFeedbackAnswerId(long feedbackAnswerId) {
        this.feedbackAnswerId = feedbackAnswerId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public HashMap<String, String> getFeedbackAnswers() {
        return feedbackAnswers;
    }

    public void setFeedbackAnswers(HashMap<String, String> feedbackAnswers) {
        this.feedbackAnswers = feedbackAnswers;
    }

}
