package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.constant.QuestionType;

import java.util.ArrayList;
import java.util.List;


public class AddQuestionRequest {
    private Long subjectId;

    private String question;

    private QuestionType questionType;

    private Boolean isShared = false;

    private List<AddAnswerRequest> answers = new ArrayList<>();

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

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

    public Boolean getIsShared() {
        return isShared;
    }

    public void setIsShared(Boolean shared) {
        isShared = shared;
    }

    public List<AddAnswerRequest> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AddAnswerRequest> answers) {
        this.answers = answers;
    }
}
