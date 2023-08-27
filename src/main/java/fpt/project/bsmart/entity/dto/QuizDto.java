package fpt.project.bsmart.entity.dto;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.project.bsmart.config.json.View;
import fpt.project.bsmart.entity.constant.QuizStatus;

import java.util.ArrayList;
import java.util.List;

public class QuizDto {
    private Long id;
    private String code;
    private Integer questionCount = 0;
    private QuizStatus status;
    @JsonView(View.Teacher.class)
    private Float defaultPoint;
    @JsonView(View.Teacher.class)
    private Boolean isSuffleQuestion;
    @JsonView(View.Teacher.class)
    private Boolean isAllowReview;
    private Integer allowReviewAfterMin;
    @JsonView(View.Teacher.class)
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

    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
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

    public Boolean getIsSuffleQuestion() {
        return isSuffleQuestion;
    }

    public void setIsSuffleQuestion(Boolean suffleQuestion) {
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
