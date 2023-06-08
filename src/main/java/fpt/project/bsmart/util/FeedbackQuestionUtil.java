package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.FeedbackAnswer;
import fpt.project.bsmart.entity.FeedbackQuestion;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.request.feedback.FeedbackAnswerRequest;
import fpt.project.bsmart.entity.request.feedback.FeedbackQuestionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class FeedbackQuestionUtil {
    private static MessageUtil messageUtil;
    public static final Long MIN_QUESTION_SCORE = 0L;
    public static final Long MAX_QUESTION_SCORE = 5L;
    public static final Integer MIN_ANSWER_IN_QUESTION = 2;
    public static final Integer MAX_ANSWER_IN_QUESTION = 5;
    public static final Integer MIN_QUESTION_IN_TEMPLATE = 2;
    public static final Integer MAX_QUESTION_IN_TEMPLATE = 10;

    public FeedbackQuestionUtil(MessageUtil messageUtil){
        this.messageUtil = messageUtil;
    }

    public static void validateFeedbackAnswer(FeedbackQuestionRequest request){
        List<FeedbackAnswerRequest> answers = request.getAnswers();
        if (answers.isEmpty()
                || answers.size() < FeedbackQuestionUtil.MIN_ANSWER_IN_QUESTION
                || answers.size() > FeedbackQuestionUtil.MAX_ANSWER_IN_QUESTION) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Invalid.INVALID_NUMBER_OF_ANSWER_IN_FEEDBACK_QUESTION) + answers.size());
        }

        for(FeedbackAnswerRequest feedbackAnswer : answers){
            if(feedbackAnswer.getAnswer().trim().isEmpty()){
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
            }
            if (feedbackAnswer.getScore() < FeedbackQuestionUtil.MIN_QUESTION_SCORE || feedbackAnswer.getScore() > FeedbackQuestionUtil.MAX_QUESTION_SCORE) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Invalid.INVALID_SCORE_IN_FEEDBACK_QUESTION) + feedbackAnswer.getScore());
            }
        }

        List<String> answerList = answers.stream()
                .map(FeedbackAnswerRequest::getAnswer)
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        boolean isDuplicateAnswer = answerList.stream()
                .distinct()
                .count() != answerList.size();

        if(isDuplicateAnswer){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }

        List<Long> scoreList = answers.stream()
                .map(FeedbackAnswerRequest::getScore)
                .collect(Collectors.toList());

        boolean isDuplicateScore = scoreList.stream()
                .distinct()
                .count() != scoreList.size();
        if(isDuplicateScore){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.DUPLICATE_SCORE_IN_FEEDBACK_QUESTION));
        }

        boolean isContainMaxScore = scoreList.stream()
                .anyMatch(x -> x.equals(FeedbackQuestionUtil.MAX_QUESTION_SCORE));
        if(!isContainMaxScore){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.MISSING_MAX_SCORE));
        }
    }

    public static List<FeedbackAnswer> feedbackAnswerRequestMapper(FeedbackQuestion question, List<FeedbackAnswerRequest> answers){
        List<FeedbackAnswer> feedbackAnswers = new ArrayList<>();
        for (FeedbackAnswerRequest feedbackAnswer : answers){
            FeedbackAnswer fa = new FeedbackAnswer();
            fa.setQuestion(question);
            fa.setAnswer(feedbackAnswer.getAnswer());
            fa.setScore(feedbackAnswer.getScore());
            feedbackAnswers.add(fa);
        }
        return feedbackAnswers;
    }
}
