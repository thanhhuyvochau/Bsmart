package fpt.project.bsmart.entity.dto;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.project.bsmart.config.json.View;

public class BaseQuizAnswerDto {
    private Long id;
    private String answer;
    @JsonView(View.Teacher.class)
    private Boolean isRight = false;

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

    public void setIsRight(Boolean isRight) {
        this.isRight = isRight;
    }
}
