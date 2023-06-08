package fpt.project.bsmart.entity.request.feedback;


import fpt.project.bsmart.entity.constant.EQuestionType;

import java.util.ArrayList;
import java.util.List;

public class FeedbackQuestionRequest {
    private String question;
    private EQuestionType questionType;
    private List<FeedbackAnswerRequest> answers = new ArrayList<>();

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<FeedbackAnswerRequest> getAnswers() {
        return answers;
    }

    public void setAnswers(List<FeedbackAnswerRequest> answers) {
        this.answers = answers;
    }

    public EQuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(EQuestionType questionType) {
        this.questionType = questionType;
    }

}
