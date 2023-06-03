package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.constant.EQuestionType;

import java.util.ArrayList;
import java.util.List;

public class FeedbackQuestionDto {
    private Long id;
    private String question;
    private List<FeedbackAnswerDto> answers = new ArrayList<>();
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

    public List<FeedbackAnswerDto> getAnswers() {
        return answers;
    }

    public void setAnswers(List<FeedbackAnswerDto> answers) {
        this.answers = answers;
    }

    public EQuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(EQuestionType questionType) {
        this.questionType = questionType;
    }

}
