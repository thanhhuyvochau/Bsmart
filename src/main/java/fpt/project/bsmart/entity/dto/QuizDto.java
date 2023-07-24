package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.constant.QuizStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class QuizDto {
    private Long id;
    private String code;
    private Instant startDate;
    private Instant endDate;
    private Integer time;
    private QuizStatus status;
    private Float defaultPoint;
    private Boolean isSuffleQuestion;
    private Boolean isAllowReview;
    private Integer allowReviewAfterMin;
    private String password;
    private Long activityId;
    private List<QuizQuestionDto> quizQuestions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public QuizStatus getStatus() {
        return status;
    }

    public void setStatus(QuizStatus status) {
        this.status = status;
    }

    public Float getDefaultPoint() {
        return defaultPoint;
    }

    public void setDefaultPoint(Float defaultPoint) {
        this.defaultPoint = defaultPoint;
    }

    public Boolean getSuffleQuestion() {
        return isSuffleQuestion;
    }

    public void setSuffleQuestion(Boolean suffleQuestion) {
        isSuffleQuestion = suffleQuestion;
    }

    public Boolean getIsAllowReview() {
        return isAllowReview;
    }

    public void setIsAllowReview(Boolean allowReview) {
        isAllowReview = allowReview;
    }

    public Integer getAllowReviewAfterMin() {
        return allowReviewAfterMin;
    }

    public void setAllowReviewAfterMin(Integer allowReviewAfterMin) {
        this.allowReviewAfterMin = allowReviewAfterMin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public List<QuizQuestionDto> getQuizQuestions() {
        return quizQuestions;
    }

    public void setQuizQuestions(List<QuizQuestionDto> quizQuestions) {
        this.quizQuestions = quizQuestions;
    }
}
