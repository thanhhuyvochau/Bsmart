package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.constant.EQuestionType;

import java.util.HashMap;

public class QuestionDto {
    private Long id;
    private String question;
    private HashMap<String, Long> possibleAnswer;
    private EQuestionType questionType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public HashMap<String, Long> getPossibleAnswer() {
        return possibleAnswer;
    }

    public void setPossibleAnswer(HashMap<String, Long> possibleAnswer) {
        this.possibleAnswer = possibleAnswer;
    }

    public EQuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(EQuestionType questionType) {
        this.questionType = questionType;
    }
}
