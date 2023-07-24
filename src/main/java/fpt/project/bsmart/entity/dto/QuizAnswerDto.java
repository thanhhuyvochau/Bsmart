package fpt.project.bsmart.entity.dto;

public class QuizAnswerDto extends BaseQuizAnswerDto{
    private Boolean isRight = false;

    public Boolean getIsRight() {
        return isRight;
    }

    public void setIsRight(Boolean right) {
        isRight = right;
    }
}
