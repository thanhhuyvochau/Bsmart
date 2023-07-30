package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.dto.feedback.FeedbackTemplateDto;
import fpt.project.bsmart.entity.request.StudentSubmitFeedbackRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static fpt.project.bsmart.util.Constants.ErrorMessage.Empty.EMPTY_FEEDBACK_ANSWER;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Empty.EMPTY_QUESTION;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Invalid.INVALID_NUMBER_OF_ANSWER_IN_FEEDBACK_QUESTION;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Invalid.INVALID_QUESTION_LIST_SIZE;

@Component
public class FeedbackUtil {
    public static final String PREFIX = "_";
    public static final Integer MIN_QUESTION_PER_TEMPLATE = 3;
    public static final Integer MAX_QUESTION_PER_TEMPLATE = 10;
    public static final Integer MIN_ANSWER_PER_QUESTION = 2;
    public static final Integer MAX_ANSWER_PER_QUESTION = 5;
    public static final Integer MIN_RATE_PER_SUBMISSION = 1;
    public static final Integer MAX_RATE_PER_SUBMISSION = 5;
    public static final Double MIN_CLASS_PERCENTAGE_TO_FEEDBACK = 70.0;
    private static MessageUtil staticMessageUtil;

    public FeedbackUtil(MessageUtil messageUtil){
        staticMessageUtil = messageUtil;
    }

    public static List<FeedbackQuestion> validateFeedbackQuestionsInRequest(FeedbackTemplateDto request, FeedbackTemplate feedbackTemplate){
        int questionCount = request.getQuestions().size();
        if(questionCount < FeedbackUtil.MIN_QUESTION_PER_TEMPLATE
                || questionCount > FeedbackUtil.MAX_QUESTION_PER_TEMPLATE){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(staticMessageUtil.getLocalMessage(INVALID_QUESTION_LIST_SIZE) + questionCount);
        }
        ArrayList<FeedbackQuestion> feedbackQuestions = new ArrayList<>();
        for(FeedbackTemplateDto.FeedbackQuestionDto question : request.getQuestions()){
            if(StringUtil.isNullOrEmpty(question.getQuestion())){
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(staticMessageUtil.getLocalMessage(EMPTY_QUESTION));
            }
            FeedbackQuestion feedbackQuestion = new FeedbackQuestion();
            if(question.getAnswers() == null || question.getAnswers().isEmpty()){
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(staticMessageUtil.getLocalMessage(EMPTY_FEEDBACK_ANSWER));
            }
            ArrayList<FeedbackAnswer> feedbackAnswers = validateFeedbackAnswers(question, feedbackQuestion);
            feedbackQuestion.setAnswers(feedbackAnswers);
            feedbackQuestion.setQuestion(question.getQuestion());
            feedbackQuestion.setTemplate(feedbackTemplate);
            feedbackQuestions.add(feedbackQuestion);
        }
        return feedbackQuestions;
    }

    private static ArrayList<FeedbackAnswer> validateFeedbackAnswers(FeedbackTemplateDto.FeedbackQuestionDto question, FeedbackQuestion feedbackQuestion){
        ArrayList<FeedbackAnswer> feedbackAnswers = new ArrayList<>();
        int answerCount = question.getAnswers().size();
        if(answerCount < FeedbackUtil.MIN_ANSWER_PER_QUESTION
                || answerCount > FeedbackUtil.MAX_ANSWER_PER_QUESTION){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(staticMessageUtil.getLocalMessage(INVALID_NUMBER_OF_ANSWER_IN_FEEDBACK_QUESTION) + answerCount);
        }
        for(FeedbackTemplateDto.FeedbackAnswerDto answer : question.getAnswers()){
            if(StringUtil.isNullOrEmpty(answer.getAnswer())){
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(staticMessageUtil.getLocalMessage(EMPTY_FEEDBACK_ANSWER));
            }
            FeedbackAnswer feedbackAnswer = new FeedbackAnswer();
            feedbackAnswer.setQuestion(feedbackQuestion);
            feedbackAnswer.setAnswer(answer.getAnswer());
            feedbackAnswers.add(feedbackAnswer);
        }
        return feedbackAnswers;
    }

    public static Boolean isClassAvailableToFeedback(Class clazz){
        return ClassUtil.getPercentageOfClassTime(clazz).getPercentage() >= FeedbackUtil.MIN_CLASS_PERCENTAGE_TO_FEEDBACK;
    }

    /**
     * format: Feedback type_Course code_Full name
     * @param clazz
     * @param user
     * @return feedback submission name
     */
    public static String generateFeedbackSubmissionName(Class clazz, User user){
        return clazz.getFeedbackTemplate().getType().getName() +
                PREFIX +
                clazz.getCourse().getCode() +
                PREFIX +
                user.getFullName();
    }

    public static Double calculateCourseRate(List<FeedbackSubmission> submissions){
        if(submissions == null || submissions.isEmpty()){
            return 0.0;
        }
        Double totalRate = submissions.stream()
                .mapToDouble(FeedbackSubmission::getRate)
                .sum();
        Double rate = totalRate / submissions.size();
        return rate;
    }

    public static ArrayList<FeedbackSubmitAnswer> validateSubmittedAnswer(FeedbackSubmission feedbackSubmission, StudentSubmitFeedbackRequest request){
        ArrayList<FeedbackSubmitAnswer> submitAnswers = new ArrayList<>();
        for(FeedbackQuestion feedbackQuestion : feedbackSubmission.getTemplate().getQuestions()){
            Boolean isRequestContainQuestionInTemplate = request.getSubmitAnswers().containsKey(feedbackQuestion.getId());
            if(Boolean.FALSE.equals(isRequestContainQuestionInTemplate)){
                throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(staticMessageUtil.getLocalMessage(""));
            }
            Long answerId = request.getSubmitAnswers().get(feedbackQuestion.getId());
            FeedbackAnswer feedbackAnswer = feedbackQuestion.getAnswers().stream()
                    .filter(x -> x.getId().equals(answerId))
                    .findFirst()
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(staticMessageUtil.getLocalMessage("")));
            FeedbackSubmitAnswer submitAnswer = new FeedbackSubmitAnswer();
            submitAnswer.setAnswer(feedbackAnswer);
            submitAnswer.setSubmission(feedbackSubmission);
            submitAnswers.add(submitAnswer);
        }
        return submitAnswers;
    }
}