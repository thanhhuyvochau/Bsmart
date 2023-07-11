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

    public Boolean getIsRight() {
        return isRight;
    }

    public void setISRight(Boolean right) {
        isRight = right;
    }
}
