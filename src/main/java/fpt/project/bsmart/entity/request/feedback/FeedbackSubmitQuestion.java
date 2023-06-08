package fpt.project.bsmart.entity.request.feedback;

public class FeedbackSubmitQuestion {
    private Long questionId;
    private SubmitFeedbackAnswer submitAnswers;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public SubmitFeedbackAnswer getSubmitAnswers() {
        return submitAnswers;
    }

    public void setSubmitAnswers(SubmitFeedbackAnswer submitAnswers) {
        this.submitAnswers = submitAnswers;
    }

}
