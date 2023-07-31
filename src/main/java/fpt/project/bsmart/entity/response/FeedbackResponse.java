package fpt.project.bsmart.entity.response;

import java.util.ArrayList;
import java.util.List;

public class FeedbackResponse {
    private Integer submissionCount = 0;
    private Double averageRate = 0.0;
    private List<FeedbackSubmission> submissions = new ArrayList<>();

    public Integer getSubmissionCount() {
        return submissionCount;
    }

    public void setSubmissionCount(Integer submissionCount) {
        this.submissionCount = submissionCount;
    }

    public Double getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(Double averageRate) {
        this.averageRate = averageRate;
    }

    public List<FeedbackSubmission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<FeedbackSubmission> submissions) {
        this.submissions = submissions;
    }

    public static class FeedbackSubmission{
        private String submitBy;
        private Integer rate = 0;
        private String comment;

        public String getSubmitBy() {
            return submitBy;
        }

        public void setSubmitBy(String submitBy) {
            this.submitBy = submitBy;
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
}
