package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.constant.QuestionType;

import java.util.ArrayList;
import java.util.List;

public class QuizQuestionRequest {
    private String question;
    private QuestionType questionType;
    private List<QuizAnswerRequest> answers = new ArrayList<>();

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public List<QuizAnswerRequest> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuizAnswerRequest> answers) {
        this.answers = answers;
    }

}
