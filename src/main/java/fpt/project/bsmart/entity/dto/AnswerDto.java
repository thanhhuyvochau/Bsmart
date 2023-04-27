package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.Question;

import javax.persistence.*;



public class AnswerDto {
    private Long id;
    
    private String answer;
    
    private Boolean isRight = false;
    private String key;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
