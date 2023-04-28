package fpt.project.bsmart.entity.request;

public class AddAnswerRequest {

    private String answer;
    private Boolean isRight = false;
    private String key;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean getIsRight() {
        return isRight;
    }

    public void setIsRight(Boolean right) {
        isRight = right;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
