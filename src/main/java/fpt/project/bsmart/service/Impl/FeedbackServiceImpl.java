package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EFeedbackType;
import fpt.project.bsmart.entity.constant.EQuestionType;
import fpt.project.bsmart.entity.dto.FeedbackTemplateDto;
import fpt.project.bsmart.entity.request.feedback.AddFeedbackTemplateRequest;
import fpt.project.bsmart.entity.request.feedback.AddQuestionRequest;
import fpt.project.bsmart.entity.request.feedback.SubCourseFeedbackRequest;
import fpt.project.bsmart.entity.response.UserFeedbackResponse;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.IFeedbackService;
import fpt.project.bsmart.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.ClassUtil.*;

@Service
public class FeedbackServiceImpl implements IFeedbackService {
    private final MessageUtil messageUtil;
    private final ClassRepository classRepository;
    private final FeedbackQuestionRepository feedbackQuestionRepository;
    private final FeedbackTemplateRepository feedbackTemplateRepository;
    private final FeedbackAnswerRepository feedbackAnswerRepository;
    private final RoleRepository roleRepository;
    private final SubCourseFeedbackRepository subCourseFeedbackRepository;

    private final SubCourseRepository subCourseRepository;

    public FeedbackServiceImpl(MessageUtil messageUtil, ClassRepository classRepository, FeedbackQuestionRepository feedbackQuestionRepository, FeedbackAnswerRepository feedbackAnswerRepository, RoleRepository roleRepository, FeedbackTemplateRepository feedbackTemplateRepository, SubCourseFeedbackRepository subCourseFeedbackRepository, SubCourseRepository subCourseRepository) {
        this.messageUtil = messageUtil;
        this.classRepository = classRepository;
        this.feedbackQuestionRepository = feedbackQuestionRepository;
        this.feedbackAnswerRepository = feedbackAnswerRepository;
        this.roleRepository = roleRepository;
        this.feedbackTemplateRepository = feedbackTemplateRepository;
        this.subCourseFeedbackRepository = subCourseFeedbackRepository;
        this.subCourseRepository = subCourseRepository;
    }

    @Override
    public Long addNewQuestion(AddQuestionRequest addQuestionRequest) {
        if (addQuestionRequest == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }

        if (addQuestionRequest.getNewQuestion().getQuestion() == null
                || addQuestionRequest.getNewQuestion().getQuestion().trim().isEmpty()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }

        if (addQuestionRequest.getNewQuestion().getQuestionType() == EQuestionType.MULTIPLE_CHOICE) {
            if (addQuestionRequest.getNewQuestion().getPossibleAnswer().isEmpty()
                    || addQuestionRequest.getNewQuestion().getPossibleAnswer().size() < QuestionUtil.MIN_ANSWER_IN_QUESTION
                    || addQuestionRequest.getNewQuestion().getPossibleAnswer().size() > QuestionUtil.MAX_ANSWER_IN_QUESTION) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
            }
            for (Long score : addQuestionRequest.getNewQuestion().getPossibleAnswer().values()) {
                if (score < QuestionUtil.MIN_QUESTION_SCORE || score > QuestionUtil.MAX_QUESTION_SCORE) {
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
                }
            }
        }

        FeedbackQuestion question = new FeedbackQuestion();
        question.setQuestion(addQuestionRequest.getNewQuestion().getQuestion());
        question.setQuestionType(addQuestionRequest.getNewQuestion().getQuestionType());

        if (addQuestionRequest.getNewQuestion().getQuestionType() == EQuestionType.MULTIPLE_CHOICE) {
            question.setPossibleAnswer(QuestionUtil.convertAnswersToAnswerString(new ArrayList<>(addQuestionRequest.getNewQuestion().getPossibleAnswer().keySet())));
            question.setPossibleScore(QuestionUtil.convertScoresToScoreString(new ArrayList<>(addQuestionRequest.getNewQuestion().getPossibleAnswer().values())));
        }

        return feedbackQuestionRepository.save(question).getId();
    }

    @Override
    public Long addNewFeedbackTemplate(AddFeedbackTemplateRequest addFeedbackTemplateRequest) {
        if (addFeedbackTemplateRequest == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }

        if (addFeedbackTemplateRequest.getTemplateName() == null || addFeedbackTemplateRequest.getTemplateName().trim().isEmpty()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }

        if (addFeedbackTemplateRequest.getQuestionList() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }

        int numberOfQuestionInRequestTemplate = addFeedbackTemplateRequest.getQuestionList().size();
        if (numberOfQuestionInRequestTemplate < QuestionUtil.MIN_QUESTION_IN_TEMPLATE
                || numberOfQuestionInRequestTemplate > QuestionUtil.MAX_QUESTION_IN_TEMPLATE) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("") + numberOfQuestionInRequestTemplate);
        }

        Role permission = roleRepository.findRoleByCode(addFeedbackTemplateRequest.getPermission())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(""));
        Long numberOfQuestion = feedbackQuestionRepository.countByIdIn(addFeedbackTemplateRequest.getQuestionList());

        if (numberOfQuestion != numberOfQuestionInRequestTemplate) {
            for (Long id : addFeedbackTemplateRequest.getQuestionList()) {
                if (!feedbackQuestionRepository.existsById(id)) {
                    throw ApiException.create(HttpStatus.NOT_FOUND)
                            .withMessage(messageUtil.getLocalMessage("") + id);
                }
            }
        }

        List<FeedbackQuestion> feedbackQuestionList = feedbackQuestionRepository.findAllById(addFeedbackTemplateRequest.getQuestionList());
        if(!addFeedbackTemplateRequest.getFeedbackType().equals(EFeedbackType.REPORT)){
            boolean isMultipleChoiceQuestionExist = feedbackQuestionList.stream()
                    .anyMatch(x -> x.getQuestionType().equals(EQuestionType.MULTIPLE_CHOICE));

            if(!isMultipleChoiceQuestionExist){
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
            }
        }
        FeedbackTemplate feedbackTemplate = new FeedbackTemplate();
        feedbackTemplate.setQuestions(feedbackQuestionList);
        feedbackTemplate.setFeedbackType(addFeedbackTemplateRequest.getFeedbackType());
        feedbackTemplate.setPermission(permission);

        return feedbackTemplateRepository.save(feedbackTemplate).getId();
    }

    @Override
    public FeedbackTemplateDto getFeedbackTemplateById(Long id) {
        if(id == null){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }
        FeedbackTemplate feedbackTemplate = feedbackTemplateRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("") + id));
        return ConvertUtil.convertTemplateToTemplateDto(feedbackTemplate);
    }

    private boolean isValueInRange(double value, double range, double approximate){
        return value >= range - approximate && value <= range + approximate;
    }

    @Override
    public Long addNewSubCourseFeedback(SubCourseFeedbackRequest subCourseFeedbackRequest) {
        User user = SecurityUtil.getCurrentUser();

        Class clazz = classRepository.findById(subCourseFeedbackRequest.getClassID())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage("") + subCourseFeedbackRequest.getClassID()));
        boolean isStudentInClass = clazz.getStudentClasses().stream()
                .anyMatch(x -> x.getStudent().equals(user));
        if(!isStudentInClass){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }
        boolean isAlreadyFeedback = subCourseFeedbackRepository
                .findBySubCourseAndFeedbackTypeAndFeedbackAnswer_FeedbackUser(clazz.getSubCourse(), subCourseFeedbackRequest.getFeedbackType(), user)
                .isPresent();
        if(isAlreadyFeedback){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }
        //check tiến độ
        double classProgressPercentage = ClassUtil.getPercentageOfClassTime(clazz).getPercentage();
        //in range
        switch (subCourseFeedbackRequest.getFeedbackType()){
            case SUB_COURSE_FIRST_HALF:
                if(!isValueInRange(classProgressPercentage, CLASS_PERCENTAGE_FOR_FIRST_FEEDBACK, PERCENTAGE_RANGE)){
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage("") + classProgressPercentage);
                }
                break;
            case SUB_COURSE_SECOND_HALF:
                if(!isValueInRange(classProgressPercentage, CLASS_PERCENTAGE_FOR_SECOND_FEEDBACK, PERCENTAGE_RANGE)){
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage("") + classProgressPercentage);
                }
                break;
            default:
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage("") + subCourseFeedbackRequest.getFeedbackType());
        }

        FeedbackTemplate feedbackTemplate = feedbackTemplateRepository.findById(subCourseFeedbackRequest.getFeedbackAnswer().getTemplateId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage("") + subCourseFeedbackRequest.getFeedbackAnswer().getTemplateId()));
        String feedbackAnswerString = "";
        long totalScore = 0L;
        for (int i = 0; i < feedbackTemplate.getQuestions().size(); i++){
            String answer = subCourseFeedbackRequest.getFeedbackAnswer().getAnswer().get(i);
            if(feedbackTemplate.getQuestions().get(i).getQuestionType().equals(EQuestionType.MULTIPLE_CHOICE)){
                if(StringUtil.isNullOrEmpty(answer)){
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
                }
                int answerIndex;
                try{
                    answerIndex = Integer.parseInt(answer);
                }catch (NumberFormatException e){
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
                }
                List<Long> possibleScore = QuestionUtil.convertScoreStringToScoreList(feedbackTemplate.getQuestions().get(i).getPossibleScore());
                totalScore += possibleScore.get(answerIndex);
            }
            feedbackAnswerString = QuestionUtil.addNewAnswerToAnswerString(feedbackAnswerString, answer);
        }

        FeedbackAnswer feedbackAnswer = new FeedbackAnswer();
        feedbackAnswer.setFeedbackUser(user);
        feedbackAnswer.setFeedbackTemplate(feedbackTemplate);
        feedbackAnswer.setAnswer(feedbackAnswerString);

        long multipleQuestionCount = feedbackTemplate.getQuestions().stream()
                .filter(x -> x.getQuestionType().equals(EQuestionType.MULTIPLE_CHOICE))
                .count();

        SubCourseFeedback subCourseFeedback = new SubCourseFeedback();
        subCourseFeedback.setSubCourse(clazz.getSubCourse());
        subCourseFeedback.setFeedbackAnswer(feedbackAnswer);
        subCourseFeedback.setScore(multipleQuestionCount == 0 ? null : (double) totalScore / multipleQuestionCount);
        subCourseFeedback.setFeedbackType(subCourseFeedback.getFeedbackType());
        if(StringUtil.isNotNullOrEmpty(subCourseFeedback.getOpinion())){
            subCourseFeedback.setOpinion(subCourseFeedback.getOpinion());
        }
        return subCourseFeedbackRepository.save(subCourseFeedback).getId();
    }

    @Override
    public List<UserFeedbackResponse> getFeedbackByClass(Long id) {
        Class clazz = classRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("")));
        List<User> users = clazz.getStudentClasses().stream()
                .map(StudentClass::getStudent)
                .collect(Collectors.toList());
        List<FeedbackAnswer> feedbackAnswerList = feedbackAnswerRepository.getAllByFeedbackUserIn(users);
        List<UserFeedbackResponse> userFeedbackResponses = feedbackAnswerList.stream()
                .map(ConvertUtil::convertFeedbackAnswerToUserFeedbackResponse)
                .collect(Collectors.toList());
        return userFeedbackResponses;
    }

}
