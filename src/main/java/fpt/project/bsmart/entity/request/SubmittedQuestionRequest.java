package fpt.project.bsmart.entity.request;

import java.util.ArrayList;
import java.util.List;

public class SubmittedQuestionRequest {
    private Long questionId;
    private List<Long> answerId = new ArrayList<>();

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public List<Long> getAnswerId() {
        return answerId;
    }

    public void setAnswerId(List<Long> answerId) {
        this.answerId = answerId;
    }
}
