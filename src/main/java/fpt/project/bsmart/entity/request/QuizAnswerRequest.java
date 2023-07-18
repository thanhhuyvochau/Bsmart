package fpt.project.bsmart.entity.request;

public class QuizAnswerRequest {
    private String answer;
    private boolean right = false;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean getRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }
}
