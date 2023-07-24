package fpt.project.bsmart.entity.response;

import fpt.project.bsmart.entity.constant.QuizSubmittionStatus;

import java.time.Instant;

public class QuizSubmissionResultResponse {
    private Long id;
    private UserInfo submitBy;
    private Instant submitAt;
    private Float point;
    private Integer correctNumber;
    private Integer totalQuestion;
    private QuizSubmittionStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserInfo getSubmitBy() {
        return submitBy;
    }

    public void setSubmitBy(UserInfo submitBy) {
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

    public Integer getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(Integer totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public QuizSubmittionStatus getStatus() {
        return status;
    }

    public void setStatus(QuizSubmittionStatus status) {
        this.status = status;
    }

    public static class UserInfo{

        public UserInfo(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        private Long id;
        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
