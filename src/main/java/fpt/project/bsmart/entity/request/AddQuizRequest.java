package fpt.project.bsmart.entity.request;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class AddQuizRequest extends ActivityRequest{
    private String code;
    private Float defaultPoint;
    private Boolean isSuffleQuestion = false;
    private Boolean isAllowReview = true;
    private Integer allowReviewAfterMin = 0;
    private String password;
    private List<QuizQuestionRequest> quizQuestions = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Float getDefaultPoint() {
        return defaultPoint;
    }

    public void setDefaultPoint(Float defaultPoint) {
        this.defaultPoint = defaultPoint;
    }

    public Boolean getIsSuffleQuestion() {
        return isSuffleQuestion;
    }

    public void setIsSuffleQuestion(Boolean suffleQuestion) {
        isSuffleQuestion = suffleQuestion;
    }

    public Boolean getIsAllowReview() {
        return isAllowReview;
    }

    public void setIsAllowReview(Boolean isAllowReview){
        this.isAllowReview = isAllowReview;
    }

    public Integer getAllowReviewAfterMin(){
        return this.allowReviewAfterMin;
    };
    public void setAllowReviewAfterMin(Integer allowReviewAfterMin) {
        this.allowReviewAfterMin = allowReviewAfterMin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<QuizQuestionRequest> getQuizQuestions() {
        return quizQuestions;
    }

    public void setQuizQuestions(List<QuizQuestionRequest> quizQuestions) {
        this.quizQuestions = quizQuestions;
    }
}
