package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.constant.QuizSubmittionStatus;

import java.util.ArrayList;
import java.util.List;

public class SubmitQuizRequest {
    private QuizSubmittionStatus status;
    private List<SubmittedQuestionRequest> submittedQuestions = new ArrayList<>();

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
