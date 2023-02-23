package fpt.project.bsmart.entity.request;


import fpt.project.bsmart.entity.common.EQuestionType;
import fpt.project.bsmart.entity.dto.AnswerDto;

import java.io.Serializable;
import java.util.List;

public class SurveyQuestionRequest implements Serializable {

    private String question ;
    private Boolean isVisible ;
    private EQuestionType questionType  ;

    private List<AnswerDto> answersMultipleChoice   ;



    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }

    public EQuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(EQuestionType questionType) {
        this.questionType = questionType;
    }

    public List<AnswerDto> getAnswersMultipleChoice() {
        return answersMultipleChoice;
    }

    public void setAnswersMultipleChoice(List<AnswerDto> answersMultipleChoice) {
        this.answersMultipleChoice = answersMultipleChoice;
    }


}
