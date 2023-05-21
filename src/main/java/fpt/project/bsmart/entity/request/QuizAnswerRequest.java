package fpt.project.bsmart.entity.request;

public class QuizAnswerRequest {
    private String answer;
    private Boolean isRight = false;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean getRight() {
        return isRight;
    }

    public void setRight(Boolean right) {
        isRight = right;
    }
}
