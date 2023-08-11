package fpt.project.bsmart.entity.response;

import fpt.project.bsmart.entity.dto.FeedbackSubmissionDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedbackResponse {
    private Integer submissionCount = 0;
    private Double averageRate = 0.0;
    private Map<Integer, Long> rateCount = new HashMap<>();
    private List<FeedbackSubmissionDto> submissions = new ArrayList<>();

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

    public List<FeedbackSubmissionDto> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<FeedbackSubmissionDto> submissions) {
        this.submissions = submissions;
    }

    public Map<Integer, Long> getRateCount() {
        return rateCount;
    }

    public void setRateCount(Map<Integer, Long> rateCount) {
        this.rateCount = rateCount;
    }

}
