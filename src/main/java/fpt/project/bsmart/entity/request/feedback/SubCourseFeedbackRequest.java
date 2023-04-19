package fpt.project.bsmart.entity.request.feedback;

import fpt.project.bsmart.entity.constant.EFeedbackType;

public class SubCourseFeedbackRequest {
    private Long subCourseId;
    private FeedbackAnswerRequest feedbackAnswer;
    private String opinion;
    private EFeedbackType feedbackType;

    public Long getSubCourseId() {
        return subCourseId;
    }

    public void setSubCourseId(Long subCourseId) {
        this.subCourseId = subCourseId;
    }

    public FeedbackAnswerRequest getFeedbackAnswer() {
        return feedbackAnswer;
    }

    public void setFeedbackAnswer(FeedbackAnswerRequest feedbackAnswer) {
        this.feedbackAnswer = feedbackAnswer;
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
}
