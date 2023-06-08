package fpt.project.bsmart.entity.request.feedback;

public class SubmitFeedbackAnswer {
    public Long submitAnswerId;
    private String submitFilledAnswer;

    public Long getSubmitAnswerId() {
        return submitAnswerId;
    }

    public void setSubmitAnswerId(Long submitAnswerId) {
        this.submitAnswerId = submitAnswerId;
    }

    public String getSubmitFilledAnswer() {
        return submitFilledAnswer;
    }

    public void setSubmitFilledAnswer(String submitFilledAnswer) {
        this.submitFilledAnswer = submitFilledAnswer;
    }
}
