package fpt.project.bsmart.entity.response;

import fpt.project.bsmart.entity.constant.QuizSubmittionStatus;

import java.time.Instant;

public class QuizSubmissionResultResponse {
    private Long id;
    private String submitBy;
    private Instant submitAt;
    private Float point;
    private Integer correctNumber;
    private QuizSubmittionStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubmitBy() {
        return submitBy;
    }

    public void setSubmitBy(String submitBy) {
        this.submitBy = submitBy;
    }

    public Instant getSubmitAt() {
        return submitAt;
    }

    public void setSubmitAt(Instant submitAt) {
        this.submitAt = submitAt;
    }

    public Float getPoint() {
        return point;
    }

    public void setPoint(Float point) {
        this.point = point;
    }

    public Integer getCorrectNumber() {
        return correctNumber;
    }

    public void setCorrectNumber(Integer correctNumber) {
        this.correctNumber = correctNumber;
    }

    public QuizSubmittionStatus getStatus() {
        return status;
    }

    public void setStatus(QuizSubmittionStatus status) {
        this.status = status;
    }
}
