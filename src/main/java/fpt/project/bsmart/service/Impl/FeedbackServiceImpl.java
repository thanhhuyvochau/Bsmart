package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EFeedbackType;
import fpt.project.bsmart.entity.constant.EQuestionType;
import fpt.project.bsmart.entity.dto.FeedbackQuestionDto;
import fpt.project.bsmart.entity.dto.FeedbackTemplateDto;
import fpt.project.bsmart.entity.request.UpdateSubCourseFeedbackTemplateRequest;
import fpt.project.bsmart.entity.request.feedback.FeedbackTemplateRequest;
import fpt.project.bsmart.entity.request.feedback.AddFeedbackQuestionRequest;
import fpt.project.bsmart.entity.request.feedback.SubCourseFeedbackRequest;
import fpt.project.bsmart.entity.response.UserFeedbackResponse;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.IFeedbackService;
import fpt.project.bsmart.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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
    public List<FeedbackQuestionDto> getAllFeedbackQuestions(){
        List<FeedbackQuestion> feedbackQuestions = feedbackQuestionRepository.findAll();
        List<FeedbackQuestionDto> feedbackQuestionDtos = feedbackQuestions.stream()
                .map(ConvertUtil::convertFeedbackQuestionToFeedbackQuestionDto)
                .collect(Collectors.toList());
        return feedbackQuestionDtos;
    }

    @Override
    public Long addNewQuestion(AddFeedbackQuestionRequest addFeedbackQuestionRequest) {

        if (addFeedbackQuestionRequest.getNewQuestion().getQuestion() == null
                || addFeedbackQuestionRequest.getNewQuestion().getQuestion().trim().isEmpty()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Empty.EMPTY_FEEDBACK_QUESTION));
        }
        HashMap<String, Long> possibleAnswers = addFeedbackQuestionRequest.getNewQuestion().getPossibleAnswer();
        if (addFeedbackQuestionRequest.getNewQuestion().getQuestionType() == EQuestionType.MULTIPLE_CHOICE) {
            if (possibleAnswers.isEmpty()
                    || possibleAnswers.size() < FeedbackQuestionUtil.MIN_ANSWER_IN_QUESTION
                    || possibleAnswers.size() > FeedbackQuestionUtil.MAX_ANSWER_IN_QUESTION) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Invalid.INVALID_NUMBER_OF_ANSWER_IN_FEEDBACK_QUESTION) + possibleAnswers.size());
            }

            for (Long score : possibleAnswers.values()) {
                if (score < FeedbackQuestionUtil.MIN_QUESTION_SCORE || score > FeedbackQuestionUtil.MAX_QUESTION_SCORE) {
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Invalid.INVALID_SCORE_IN_FEEDBACK_QUESTION) + score));
                }
            }

            boolean isDuplicateScore = possibleAnswers.values().stream()
                    .distinct()
                    .count() != possibleAnswers.size();
            if(isDuplicateScore){
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.DUPLICATE_SCORE_IN_FEEDBACK_QUESTION)));
            }

            boolean isContainMaxScore = possibleAnswers.values().stream()
                    .anyMatch(x -> x.equals(FeedbackQuestionUtil.MAX_QUESTION_SCORE));
            if(!isContainMaxScore){
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.MISSING_MAX_SCORE)));
            }
        }

        FeedbackQuestion question = new FeedbackQuestion();
        question.setQuestion(addFeedbackQuestionRequest.getNewQuestion().getQuestion());
        question.setQuestionType(addFeedbackQuestionRequest.getNewQuestion().getQuestionType());

        if (addFeedbackQuestionRequest.getNewQuestion().getQuestionType() == EQuestionType.MULTIPLE_CHOICE) {
            question.setPossibleAnswer(FeedbackQuestionUtil.convertAnswersToAnswerString(new ArrayList<>(possibleAnswers.keySet())));
            question.setPossibleScore(FeedbackQuestionUtil.convertScoresToScoreString(new ArrayList<>(possibleAnswers.values())));
        }

        return feedbackQuestionRepository.save(question).getId();
    }

    private List<FeedbackQuestion> getFeedbackQuestionsByIds(FeedbackTemplateRequest feedbackTemplateRequest){
        int numberOfQuestionInRequestTemplate = feedbackTemplateRequest.getQuestionList().size();
        if (numberOfQuestionInRequestTemplate < FeedbackQuestionUtil.MIN_QUESTION_IN_TEMPLATE
                || numberOfQuestionInRequestTemplate > FeedbackQuestionUtil.MAX_QUESTION_IN_TEMPLATE) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Invalid.INVALID_QUESTION_LIST_SIZE) + numberOfQuestionInRequestTemplate);
        }

        Long numberOfQuestion = feedbackQuestionRepository.countByIdIn(feedbackTemplateRequest.getQuestionList());

        if (numberOfQuestion != numberOfQuestionInRequestTemplate) {
            for (Long id : feedbackTemplateRequest.getQuestionList()) {
                if (!feedbackQuestionRepository.existsById(id)) {
                    throw ApiException.create(HttpStatus.NOT_FOUND)
                            .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.FEEDBACK_QUESTION_NOT_FOUND_BY_ID) + id);
                }
            }
        }
        List<FeedbackQuestion> feedbackQuestionList = feedbackQuestionRepository.findAllById(feedbackTemplateRequest.getQuestionList());
        if(!feedbackTemplateRequest.getFeedbackType().equals(EFeedbackType.REPORT)){
            boolean isMultipleChoiceQuestionExist = feedbackQuestionList.stream()
                    .anyMatch(x -> x.getQuestionType().equals(EQuestionType.MULTIPLE_CHOICE));

            if(!isMultipleChoiceQuestionExist){
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.MISSING_MULTI_CHOICE_FEEDBACK_QUESTION));
            }
        }
        return feedbackQuestionList;
    }

    public Long updateFeedbackTemplate(Long id, FeedbackTemplateRequest feedbackTemplateRequest){
        FeedbackTemplate feedbackTemplate = feedbackTemplateRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(""));

        if(feedbackTemplateRequest.getTemplateName() != null || !feedbackTemplateRequest.getTemplateName().trim().isEmpty()){
            feedbackTemplate.setTemplateName(feedbackTemplateRequest.getTemplateName());
        }
        if (feedbackTemplateRequest.getQuestionList() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Empty.EMPTY_QUESTION_LIST));
        }
        feedbackTemplate.setQuestions(getFeedbackQuestionsByIds(feedbackTemplateRequest));

        Role permission = roleRepository.findRoleByCode(feedbackTemplateRequest.getPermission())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(Constants.ErrorMessage.ROLE_NOT_FOUND_BY_ID));
        feedbackTemplate.setPermission(permission);
        if(feedbackTemplateRequest.getFeedbackType() != null){
            feedbackTemplate.setFeedbackType(feedbackTemplateRequest.getFeedbackType());
        }

        return feedbackTemplateRepository.save(feedbackTemplate).getId();
    }

    @Override
    public Long addNewFeedbackTemplate(FeedbackTemplateRequest feedbackTemplateRequest) {

        if (feedbackTemplateRequest.getTemplateName() == null || feedbackTemplateRequest.getTemplateName().trim().isEmpty()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Empty.EMPTY_FEEDBACK_TEMPLATE_NAME));
        }

        if (feedbackTemplateRequest.getQuestionList() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Empty.EMPTY_QUESTION_LIST));
        }

        Role permission = roleRepository.findRoleByCode(feedbackTemplateRequest.getPermission())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(Constants.ErrorMessage.ROLE_NOT_FOUND_BY_ID));

        FeedbackTemplate feedbackTemplate = new FeedbackTemplate();
        feedbackTemplate.setQuestions(getFeedbackQuestionsByIds(feedbackTemplateRequest));
        feedbackTemplate.setFeedbackType(feedbackTemplateRequest.getFeedbackType());
        feedbackTemplate.setPermission(permission);

        return feedbackTemplateRepository.save(feedbackTemplate).getId();
    }

    private FeedbackTemplate findTemplateById(Long id){
        return feedbackTemplateRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.FEEDBACK_TEMPLATE_NOT_FOUND_BY_ID) + id));
    }
    @Override
    public FeedbackTemplateDto getFeedbackTemplateById(Long id) {
        if(id == null){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Empty.EMPTY_FEEDBACK_TEMPLATE_ID));
        }
        FeedbackTemplate feedbackTemplate = findTemplateById(id);
        return ConvertUtil.convertTemplateToTemplateDto(feedbackTemplate);
    }

    public Long updateFeedbackTemplateToSubCourse(UpdateSubCourseFeedbackTemplateRequest request){
        if(request.getFeedbackTemplateId() == null){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }

        if (request.getSubCourseId() == null){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }
        /*
        User user = SecurityUtil.getCurrentUser();
        boolean isAdmin = SecurityUtil.isHasAnyRole(user, EUserRole.ADMIN);
        if(!isAdmin){
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.FORBIDDEN));
        }
        */
        FeedbackTemplate feedbackTemplate = findTemplateById(request.getFeedbackTemplateId());
        SubCourse subCourse = subCourseRepository.findById(request.getSubCourseId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.SUB_COURSE_NOT_FOUND_BY_ID) + request.getSubCourseId()));
        if(subCourse.getFeedbackTemplate() == null){
            subCourse.setFeedbackTemplate(feedbackTemplate);
        }else{
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Subcourse đã có feedback template");
        }
        return subCourseRepository.save(subCourse).getId();
    }

    private boolean isValueInRange(double value, double range, double approximate){
        return value >= range - approximate;
    }

    @Override
    public Long addNewSubCourseFeedback(SubCourseFeedbackRequest subCourseFeedbackRequest) {
        User user = SecurityUtil.getCurrentUser();

        Class clazz = classRepository.findById(subCourseFeedbackRequest.getClassID())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.CLASS_NOT_FOUND_BY_ID) + subCourseFeedbackRequest.getClassID()));
        boolean isStudentInClass = clazz.getStudentClasses().stream()
                .anyMatch(x -> x.getStudent().equals(user));
        if(!isStudentInClass){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.STUDENT_NOT_BELONG_TO_CLASS));
        }
        boolean isAlreadyFeedback = subCourseFeedbackRepository
                .findBySubCourseAndFeedbackTypeAndFeedbackAnswer_FeedbackUser(clazz.getSubCourse(), subCourseFeedbackRequest.getFeedbackType(), user)
                .isPresent();
        if(isAlreadyFeedback){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.STUDENT_ALREADY_FEEDBACK));
        }
        //check tiến độ
        double classProgressPercentage = ClassUtil.getPercentageOfClassTime(clazz).getPercentage();
        //in range
        switch (subCourseFeedbackRequest.getFeedbackType()){
            case SUB_COURSE_FIRST_HALF:
                if(!isValueInRange(classProgressPercentage, CLASS_PERCENTAGE_FOR_FIRST_FEEDBACK, PERCENTAGE_RANGE)){
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Invalid.INVALID_FEEDBACK_TIME) + classProgressPercentage);
                }
                break;
            case SUB_COURSE_SECOND_HALF:
                if(!isValueInRange(classProgressPercentage, CLASS_PERCENTAGE_FOR_SECOND_FEEDBACK, PERCENTAGE_RANGE)){
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Invalid.INVALID_FEEDBACK_TIME) + classProgressPercentage);
                }
                break;
            default:
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Invalid.INVALID_FEEDBACK_TYPE) + subCourseFeedbackRequest.getFeedbackType());
        }

        FeedbackTemplate feedbackTemplate = feedbackTemplateRepository.findById(subCourseFeedbackRequest.getFeedbackAnswer().getTemplateId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.FEEDBACK_TEMPLATE_NOT_FOUND_BY_ID) + subCourseFeedbackRequest.getFeedbackAnswer().getTemplateId()));
        String feedbackAnswerString = "";
        long totalScore = 0L;
        for (int i = 0; i < feedbackTemplate.getQuestions().size(); i++){
            String answer = subCourseFeedbackRequest.getFeedbackAnswer().getAnswer().get(i);
            if(feedbackTemplate.getQuestions().get(i).getQuestionType().equals(EQuestionType.MULTIPLE_CHOICE)){
                if(StringUtil.isNullOrEmpty(answer)){
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Empty.EMPTY_FEEDBACK_ANSWER));
                }
                int answerIndex;
                try{
                    answerIndex = Integer.parseInt(answer);
                }catch (NumberFormatException e){
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.ANSWER_FORMAT_EXCEPTION));
                }
                List<Long> possibleScore = FeedbackQuestionUtil.convertScoreStringToScoreList(feedbackTemplate.getQuestions().get(i).getPossibleScore());
                totalScore += possibleScore.get(answerIndex);
            }
            feedbackAnswerString = FeedbackQuestionUtil.addNewAnswerToAnswerString(feedbackAnswerString, answer);
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
