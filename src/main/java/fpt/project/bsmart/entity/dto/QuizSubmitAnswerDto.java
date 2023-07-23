package fpt.project.bsmart.entity.dto;

public class QuizSubmitAnswerDto {
    private Long id;
    private String answer;
    private Boolean isRight = false;
    private Boolean isChosen = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Boolean getIsChosen() {
        return isChosen;
    }

    public void setIsChosen(Boolean chosen) {
        isChosen = chosen;
    }
}
