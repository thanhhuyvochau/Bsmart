package fpt.project.bsmart.entity.request;

import java.util.HashMap;
import java.util.Map;

public class StudentSubmitFeedbackRequest {
    private Map<Long, Long> submitAnswers = new HashMap<>();
    private Integer rate = 1;
    private String comment;

    public Map<Long, Long> getSubmitAnswers() {
        return submitAnswers;
    }

    public void setSubmitAnswers(Map<Long, Long> submitAnswers) {
        this.submitAnswers = submitAnswers;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
