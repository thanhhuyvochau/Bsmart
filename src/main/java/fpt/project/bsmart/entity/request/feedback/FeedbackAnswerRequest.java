package fpt.project.bsmart.entity.request.feedback;

public class FeedbackAnswerRequest {
    private String answer;
    private Long score;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }
}
