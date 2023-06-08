package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.constant.EFeedbackType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SubCourseFeedbackDto {
    private Long id;
    private Long subCourseId;
    private String feedbackName;
    private List<SubmitFeedbackQuestionDto> questions = new ArrayList<>();
    private Double score;
    private String opinion;
    private EFeedbackType feedbackType;
    private long submitUserId;
    private Instant submitDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubCourseId() {
        return subCourseId;
    }

    public void setSubCourseId(Long subCourseId) {
        this.subCourseId = subCourseId;
    }

    public String getFeedbackName() {
        return feedbackName;
    }

    public void setFeedbackName(String feedbackName) {
        this.feedbackName = feedbackName;
    }

    public List<SubmitFeedbackQuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<SubmitFeedbackQuestionDto> questions) {
        this.questions = questions;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public EFeedbackType getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(EFeedbackType feedbackType) {
        this.feedbackType = feedbackType;
    }

    public long getSubmitUserId() {
        return submitUserId;
    }

    public void setSubmitUserId(long submitUserId) {
        this.submitUserId = submitUserId;
    }

    public Instant getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Instant submitDate) {
        this.submitDate = submitDate;
    }
}
