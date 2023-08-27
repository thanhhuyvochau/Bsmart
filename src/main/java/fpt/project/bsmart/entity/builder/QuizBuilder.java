package fpt.project.bsmart.entity.builder;

import fpt.project.bsmart.entity.Activity;
import fpt.project.bsmart.entity.Quiz;
import fpt.project.bsmart.entity.QuizQuestion;
import fpt.project.bsmart.entity.constant.QuizStatus;

import java.util.ArrayList;
import java.util.List;

public class QuizBuilder {
    private String code;
    private Integer time;
    private QuizStatus status;
    private Float defaultPoint;
    private Boolean isSuffleQuestion = false;
    private Boolean isAllowReview = true;
    private Integer allowReviewAfterMin = 0;
    private String password;
    private Activity activity;
    private List<QuizQuestion> quizQuestions = new ArrayList<>();
    private boolean isUnlimitedAttempt = false;
    private int attemptNumber = 1;

    public QuizBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public QuizBuilder withTime(Integer time) {
        this.time = time;
        return this;
    }

    public QuizBuilder withStatus(QuizStatus status) {
        this.status = status;
        return this;
    }

    public QuizBuilder withDefaultPoint(Float defaultPoint) {
        this.defaultPoint = defaultPoint;
        return this;
    }

    public QuizBuilder withIsSuffleQuestion(Boolean isSuffleQuestion) {
        this.isSuffleQuestion = isSuffleQuestion;
        return this;
    }

    public QuizBuilder withIsAllowReview(Boolean isAllowReview) {
        this.isAllowReview = isAllowReview;
        return this;
    }

    public QuizBuilder withAllowReviewAfterMin(Integer allowReviewAfterMin) {
        this.allowReviewAfterMin = allowReviewAfterMin;
        return this;
    }

    public QuizBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public QuizBuilder withActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public QuizBuilder withQuizQuestions(List<QuizQuestion> quizQuestions) {
        this.quizQuestions = quizQuestions;
        return this;
    }

    public QuizBuilder withIsUnlimitedAttempt(boolean isUnlimitedAttempt) {
        this.isUnlimitedAttempt = isUnlimitedAttempt;
        return this;
    }

    public QuizBuilder withAttemptNumber(int attemptNumber) {
        this.attemptNumber = attemptNumber;
        return this;
    }

    public Quiz build() {
        // TODO: Validation
        Quiz quiz = new Quiz(code, status, defaultPoint, isSuffleQuestion, isAllowReview, allowReviewAfterMin, password, activity, quizQuestions, isUnlimitedAttempt, attemptNumber);
        return quiz;
    }

    public static QuizBuilder getBuilder() {
        return new QuizBuilder();
    }
}