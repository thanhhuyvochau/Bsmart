package fpt.project.bsmart.entity.dto;

public class QuizSubmitAnswerDto extends QuizAnswerDto{
    private Boolean isChosen = false;

    public Boolean getIsChosen() {
        return isChosen;
    }

    public void setIsChosen(Boolean chosen) {
        isChosen = chosen;
    }
}
