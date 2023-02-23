package fpt.project.bsmart.entity.dto;


import fpt.project.bsmart.entity.common.EQuestionType;

import java.io.Serializable;
import java.util.List;

public class SurveyQuestionAnswerDto implements Serializable {

    private Long   questionId ;
    private String question ;

    private EQuestionType questionType  ;

    private List<AnswerDto> answersMultipleChoice   ;


    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
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
