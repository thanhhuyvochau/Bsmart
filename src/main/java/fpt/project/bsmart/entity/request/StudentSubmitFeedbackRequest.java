package fpt.project.bsmart.entity.request;

import java.util.HashMap;
import java.util.Map;

public class StudentSubmitFeedbackRequest {

    private Long questionId ;

    private Long answerId ;



    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }


}
