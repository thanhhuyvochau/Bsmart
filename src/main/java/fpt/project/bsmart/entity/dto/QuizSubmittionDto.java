package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.constant.QuizSubmittionStatus;

import java.util.ArrayList;
import java.util.List;

public class QuizSubmittionDto {
    private Long id;
    private Long quizId;
    private Integer correctNumber = 0;
    private Integer incorrectNumber = 0;
    private Float point;
    private QuizSubmittionStatus status;
    private List<QuizSubmitQuestionDto> questions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public Integer getCorrectNumber() {
        return correctNumber;
    }

    public void setCorrectNumber(Integer correctNumber) {
        this.correctNumber = correctNumber;
    }

    public Integer getIncorrectNumber() {
        return incorrectNumber;
    }

    public void setIncorrectNumber(Integer incorrectNumber) {
        this.incorrectNumber = incorrectNumber;
    }

    public Float getPoint() {
        return point;
    }

    public void setPoint(Float point) {
        this.point = point;
    }

    public QuizSubmittionStatus getStatus() {
        return status;
    }

    public void setStatus(QuizSubmittionStatus status) {
        this.status = status;
    }

    public List<QuizSubmitQuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuizSubmitQuestionDto> questions) {
        this.questions = questions;
    }
}
