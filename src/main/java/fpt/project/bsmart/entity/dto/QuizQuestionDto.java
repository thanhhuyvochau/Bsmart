package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.constant.QuestionType;

import java.util.ArrayList;
import java.util.List;

public class QuizQuestionDto<T extends BaseQuizAnswerDto> {
    private Long id;
    private String question;
    private QuestionType type;
    private List<T> answers = new ArrayList<>();

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

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public List<T> getAnswers() {
        return answers;
    }

    public void setAnswers(List<T> answers) {
        this.answers = answers;
    }
}
