package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.request.FeedbackTemplateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.Empty.EMPTY_FEEDBACK_ANSWER;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Empty.EMPTY_QUESTION;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Invalid.INVALID_NUMBER_OF_ANSWER_IN_FEEDBACK_QUESTION;

@Component
public class FeedbackUtil {
    public static final String PREFIX = "_";
    public static final Integer MIN_QUESTION_PER_TEMPLATE = 2;
    public static final Integer MAX_QUESTION_PER_TEMPLATE = 10;
    public static final Integer MIN_ANSWER_PER_QUESTION = 2;
    public static final Integer MAX_ANSWER_PER_QUESTION = 5;
    public static final Integer MIN_RATE_PER_SUBMISSION = 1;
    public static final Integer MAX_RATE_PER_SUBMISSION = 5;
    public static final Integer MIN_CHARACTER_IN_COMMENT = 20;
    public static final Double MIN_CLASS_PERCENTAGE_TO_FEEDBACK = 70.0;
    private static MessageUtil staticMessageUtil;

    public FeedbackUtil(MessageUtil messageUtil) {
        staticMessageUtil = messageUtil;
    }

    public static List<FeedbackQuestion> validateFeedbackQuestionsInRequest(FeedbackTemplateRequest request, FeedbackTemplate feedbackTemplate) {
        int questionCount = request.getQuestions().size();

        if (questionCount < FeedbackUtil.MIN_QUESTION_PER_TEMPLATE || questionCount > FeedbackUtil.MAX_QUESTION_PER_TEMPLATE) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Phải tạo ít nhất 2 và nhiều nhất 10 câu hỏi !");
        }

        ArrayList<FeedbackQuestion> feedbackQuestions = new ArrayList<>();
        for (FeedbackTemplateRequest.FeedbackQuestionRequest question : request.getQuestions()) {
            if (StringUtil.isNullOrEmpty(question.getQuestion())) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(staticMessageUtil.getLocalMessage(EMPTY_QUESTION));
            }
            FeedbackQuestion feedbackQuestion = new FeedbackQuestion();

            ArrayList<FeedbackAnswer> feedbackAnswers = validateFeedbackAnswers(question, feedbackQuestion);
            feedbackQuestion.setAnswers(feedbackAnswers);
            feedbackQuestion.setQuestion(question.getQuestion());
            feedbackQuestion.setTemplate(feedbackTemplate);
            feedbackQuestions.add(feedbackQuestion);

        }

        return feedbackQuestions;
    }

//    public static List<FeedbackQuestion> validateFeedbackQuestionsDefaultInRequest(List<FeedbackTemplateRequest.FeedbackQuestionRequest> questions) {
//
//        if (questions.size() < 1) {
//            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Phải tạo ít nhất 1  câu hỏi !");
//        }
//
//        ArrayList<FeedbackQuestion> feedbackQuestions = new ArrayList<>();
//        for (FeedbackTemplateRequest.FeedbackQuestionRequest question : questions) {
//            if (StringUtil.isNullOrEmpty(question.getQuestion())) {
//                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(staticMessageUtil.getLocalMessage(EMPTY_QUESTION));
//            }
//            FeedbackQuestion feedbackQuestion = new FeedbackQuestion();
//            if (question.getType().equals(MULTIPLECHOICE)) {
//                if (question.getAnswers() == null || question.getAnswers().isEmpty()) {
//                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Không được để trống câu trả lời trắc nghiệm");
//                }
//            }
//            ArrayList<FeedbackAnswer> feedbackAnswers = validateFeedbackAnswers(question, feedbackQuestion);
//            feedbackQuestion.setQuestionType(question.getType());
//            feedbackQuestion.setAnswers(feedbackAnswers);
//            feedbackQuestion.setQuestion(question.getQuestion());
//            feedbackQuestions.add(feedbackQuestion);
//
//        }
//        return feedbackQuestions;
//    }

    private static ArrayList<FeedbackAnswer> validateFeedbackAnswers(FeedbackTemplateRequest.FeedbackQuestionRequest question, FeedbackQuestion feedbackQuestion) {
        ArrayList<FeedbackAnswer> feedbackAnswers = new ArrayList<>();
        int answerCount = question.getAnswers().size();
        if (answerCount < FeedbackUtil.MIN_ANSWER_PER_QUESTION
                || answerCount > FeedbackUtil.MAX_ANSWER_PER_QUESTION) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(staticMessageUtil.getLocalMessage(INVALID_NUMBER_OF_ANSWER_IN_FEEDBACK_QUESTION) + answerCount);
        }
        for (FeedbackTemplateRequest.FeedbackAnswerRequest answer : question.getAnswers()) {
            if (StringUtil.isNullOrEmpty(answer.getAnswer())) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(staticMessageUtil.getLocalMessage(EMPTY_FEEDBACK_ANSWER));
            }
            FeedbackAnswer feedbackAnswer = new FeedbackAnswer();
            feedbackAnswer.setQuestion(feedbackQuestion);
            feedbackAnswer.setAnswer(answer.getAnswer());
            feedbackAnswers.add(feedbackAnswer);
        }
        return feedbackAnswers;
    }

    public static Boolean isClassAvailableToFeedback(Class clazz) {
        return ClassUtil.getPercentageOfClassTime(clazz).getPercentage() >= FeedbackUtil.MIN_CLASS_PERCENTAGE_TO_FEEDBACK;
    }

    public static Map<Integer, Long> getRateCount(List<Integer> rates){
        Map<Integer, Long> rateCount = rates.stream()
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()));
        for (int i = FeedbackUtil.MIN_RATE_PER_SUBMISSION; i <= FeedbackUtil.MAX_RATE_PER_SUBMISSION; i++){
            int rate = i;
            rateCount.computeIfAbsent(rate, x -> rateCount.put(rate, 0L));
        }
        return rateCount;
    }

    public static Double calculateAverageRate(Map<Integer, Long> rateCountMap) {
        double totalWeigh = 0;
        long totalSubmission = 0;
        for(Map.Entry<Integer,Long> entry : rateCountMap.entrySet()){
            totalWeigh += entry.getKey() * entry.getValue();
            totalSubmission += entry.getValue();
        }
        return totalWeigh == 0 ? 0 : Math.round((totalWeigh / totalSubmission) * 100.0) / 100.0;
    }

//    public static ArrayList<FeedbackSubmitAnswer> validateSubmittedAnswer(FeedbackSubmission feedbackSubmission, StudentSubmitFeedbackRequest request) {
//        ArrayList<FeedbackSubmitAnswer> submitAnswers = new ArrayList<>();
//        for (FeedbackQuestion feedbackQuestion : feedbackSubmission.getTemplate().getQuestions()) {
//            Boolean isRequestContainQuestionInTemplate = request.getSubmitAnswers().containsKey(feedbackQuestion.getId());
//            if (Boolean.FALSE.equals(isRequestContainQuestionInTemplate)) {
//                throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(staticMessageUtil.getLocalMessage(""));
//            }
//            Long answerId = request.getSubmitAnswers().get(feedbackQuestion.getId());
//            FeedbackAnswer feedbackAnswer = feedbackQuestion.getAnswers().stream()
//                    .filter(x -> x.getId().equals(answerId))
//                    .findFirst()
//                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(staticMessageUtil.getLocalMessage("")));
//            FeedbackSubmitAnswer submitAnswer = new FeedbackSubmitAnswer();
//            submitAnswer.setAnswer(feedbackAnswer);
//            submitAnswer.setSubmission(feedbackSubmission);
//            submitAnswers.add(submitAnswer);
//        }
//        return submitAnswers;
//    }
}
