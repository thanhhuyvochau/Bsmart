package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.constant.QuizSubmittionStatus;

import java.util.ArrayList;
import java.util.List;

public class SubmitQuizRequest {
    private Long classSectionId;
    private Long quizId;
    private QuizSubmittionStatus status;
    private List<SubmittedQuestionRequest> submittedQuestions = new ArrayList<>();

    public Long getClassSectionId() {
        return classSectionId;
    }

    public void setClassSectionId(Long classSectionId) {
        this.classSectionId = classSectionId;
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public QuizSubmittionStatus getStatus() {
        return status;
    }

    public void setStatus(QuizSubmittionStatus status) {
        this.status = status;
    }

    public List<SubmittedQuestionRequest> getSubmittedQuestions() {
        return submittedQuestions;
    }

    public void setSubmittedQuestions(List<SubmittedQuestionRequest> submittedQuestions) {
        this.submittedQuestions = submittedQuestions;
    }
}
