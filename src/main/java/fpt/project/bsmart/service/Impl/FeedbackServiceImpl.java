package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EFeedbackType;
import fpt.project.bsmart.entity.constant.EQuestionType;
import fpt.project.bsmart.entity.dto.FeedbackQuestionDto;
import fpt.project.bsmart.entity.dto.FeedbackTemplateDto;
import fpt.project.bsmart.entity.dto.SubCourseFeedbackDto;
import fpt.project.bsmart.entity.request.feedback.*;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.IFeedbackService;
import fpt.project.bsmart.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements IFeedbackService {
    private final MessageUtil messageUtil;
    private final FeedbackQuestionUtil feedbackQuestionUtil;
    private final ClassRepository classRepository;
    private final FeedbackQuestionRepository feedbackQuestionRepository;
    private final FeedbackTemplateRepository feedbackTemplateRepository;
    private final RoleRepository roleRepository;
    private final SubCourseFeedbackRepository subCourseFeedbackRepository;
    private final SubCourseRepository subCourseRepository;

    public FeedbackServiceImpl(MessageUtil messageUtil, FeedbackQuestionUtil feedbackQuestionUtil, ClassRepository classRepository, FeedbackQuestionRepository feedbackQuestionRepository, RoleRepository roleRepository, FeedbackTemplateRepository feedbackTemplateRepository, SubCourseFeedbackRepository subCourseFeedbackRepository, SubCourseRepository subCourseRepository) {
        this.messageUtil = messageUtil;
        this.feedbackQuestionUtil = feedbackQuestionUtil;
        this.classRepository = classRepository;
        this.feedbackQuestionRepository = feedbackQuestionRepository;
        this.roleRepository = roleRepository;
        this.feedbackTemplateRepository = feedbackTemplateRepository;
        this.subCourseFeedbackRepository = subCourseFeedbackRepository;
        this.subCourseRepository = subCourseRepository;
    }

    @Override
    public List<FeedbackQuestionDto> getAllFeedbackQuestions(){
        List<FeedbackQuestion> feedbackQuestions = feedbackQuestionRepository.findAll();
        return feedbackQuestions.stream()
                .map(ConvertUtil::convertFeedbackQuestionToFeedbackQuestionDto)
                .collect(Collectors.toList());
    }

    private FeedbackQuestion findFeedbackQuestionById(Long id){
        return feedbackQuestionRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.FEEDBACK_QUESTION_NOT_FOUND_BY_ID) + id));
    }

    @Override
    public FeedbackQuestionDto getFeedbackQuestionById(Long id){
        FeedbackQuestion feedbackQuestion = findFeedbackQuestionById(id);
        return ConvertUtil.convertFeedbackQuestionToFeedbackQuestionDto(feedbackQuestion);
    }


    @Override
    public Long addNewQuestion(FeedbackQuestionRequest feedbackQuestionRequest) {

        if (feedbackQuestionRequest.getQuestion() == null
                || feedbackQuestionRequest.getQuestion().trim().isEmpty()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Empty.EMPTY_FEEDBACK_QUESTION));
        }

        FeedbackQuestion question = new FeedbackQuestion();
        question.setQuestion(feedbackQuestionRequest.getQuestion());
        question.setQuestionType(feedbackQuestionRequest.getQuestionType());

        if (feedbackQuestionRequest.getQuestionType() == EQuestionType.MULTIPLE_CHOICE) {
            feedbackQuestionUtil.validateFeedbackAnswer(feedbackQuestionRequest);
            question.setAnswers(feedbackQuestionUtil.feedbackAnswerRequestMapper(question, feedbackQuestionRequest.getAnswers()));
        }

        return feedbackQuestionRepository.save(question).getId();
    }

    @Override
    public Long updateFeedbackQuestion(Long id, FeedbackQuestionRequest request){
        FeedbackQuestion feedbackQuestion =  findFeedbackQuestionById(id);
        if(!feedbackQuestion.getFeedbackTemplates().isEmpty()){
            //check xem đang trong giai đoạn feedback hay không
        }
        if(request.getQuestion() != null && !request.getQuestion().trim().isEmpty()){
            feedbackQuestion.setQuestion(request.getQuestion());
        }
        feedbackQuestion.setQuestionType(request.getQuestionType());
        if (request.getQuestionType().equals(EQuestionType.MULTIPLE_CHOICE)){
            feedbackQuestionUtil.validateFeedbackAnswer(request);
            feedbackQuestion.setAnswers(feedbackQuestionUtil.feedbackAnswerRequestMapper(feedbackQuestion, request.getAnswers()));
        }else {
            feedbackQuestion.setAnswers(null);
        }
        return feedbackQuestionRepository.save(feedbackQuestion).getId();
    }

    @Override
    public Long deleteFeedbackQuestion(Long id){
        FeedbackQuestion feedbackQuestion = findFeedbackQuestionById(id);
        if(!feedbackQuestion.getFeedbackTemplates().isEmpty()){
            //check xem đang trong giai đoạn feedback hay không
        }
        feedbackQuestionRepository.delete(feedbackQuestion);
        return id;
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

    @Override
    public Long updateFeedbackTemplate(Long id, FeedbackTemplateRequest feedbackTemplateRequest){
        FeedbackTemplate feedbackTemplate = findTemplateById(id);
        if(!feedbackTemplate.getSubCourses().isEmpty()){
            //check xem đang trong giai đoạn feedback hay không
        }
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
    public Long deleteFeedbackTemplate(Long id){
        FeedbackTemplate feedbackTemplate = findTemplateById(id);
        if(!feedbackTemplate.getSubCourses().isEmpty()){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.FEEDBACK_TEMPLATE_ATTACHED_TO_SUB_COURSE));
        }
        feedbackTemplateRepository.delete(feedbackTemplate);
        return id;
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
        feedbackTemplate.setTemplateName(feedbackTemplateRequest.getTemplateName());
        feedbackTemplate.setQuestions(getFeedbackQuestionsByIds(feedbackTemplateRequest));
        feedbackTemplate.setFeedbackType(feedbackTemplateRequest.getFeedbackType());
        feedbackTemplate.setPermission(permission);

        return feedbackTemplateRepository.save(feedbackTemplate).getId();
    }

    @Override
    public List<FeedbackTemplateDto> getAllFeedbackTemplate() {
        List<FeedbackTemplate> feedbackTemplates = feedbackTemplateRepository.findAll();
        List<FeedbackTemplateDto> feedbackTemplateDtos = feedbackTemplates.stream()
                .map(ConvertUtil::convertTemplateToTemplateDto)
                .collect(Collectors.toList());
        return feedbackTemplateDtos;
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

    public Long updateFeedbackTemplateToSubCourse(Long templateId, Long subCourseId){

        /*
        User user = SecurityUtil.getCurrentUser();
        boolean isAdmin = SecurityUtil.isHasAnyRole(user, EUserRole.ADMIN);
        if(!isAdmin){
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.FORBIDDEN));
        }
        */
        FeedbackTemplate feedbackTemplate = findTemplateById(templateId);
        SubCourse subCourse = subCourseRepository.findById(subCourseId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.SUB_COURSE_NOT_FOUND_BY_ID) + subCourseId));
        if(subCourse.getFeedbackTemplate() != null){
            //check xem đang trong giai đoạn feedback hay không
        }
        subCourse.setFeedbackTemplate(feedbackTemplate);
        return subCourseRepository.save(subCourse).getId();
    }

    private boolean isValueInRange(double value, double range, double approximate){
        return value >= range - approximate;
    }

    @Override
    public Long addNewSubCourseFeedback(SubCourseFeedbackRequest subCourseFeedbackRequest) {
        User user = SecurityUtil.getCurrentUser();
        /*
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
        */
        SubCourse subCourse = findSubCourseById(subCourseFeedbackRequest.getSubCourseId());
        FeedbackTemplate feedbackTemplate = subCourse.getFeedbackTemplate();
        SubCourseFeedback subCourseFeedback = new SubCourseFeedback();

        long totalScore = 0L;
        int multipleQuestionCount = 0;
        List<FeedbackSubmitQuestion> feedbackSubmitQuestions = subCourseFeedbackRequest.getSubmitQuestions();
        List<Long> feedbackQuestionIds = feedbackTemplate.getQuestions().stream()
                .map(FeedbackQuestion::getId)
                .collect(Collectors.toList());
        Map<Long, FeedbackSubmitQuestion> submitFeedbackQuestionMap = feedbackSubmitQuestions.stream()
                .collect(Collectors.toMap(FeedbackSubmitQuestion::getQuestionId, Function.identity()));
        Map<Long, FeedbackQuestion> questionMap = feedbackTemplate.getQuestions().stream()
                .collect(Collectors.toMap(FeedbackQuestion::getId, Function.identity()));
        List<FeedbackSubmitAnswer> submitAnswers = new ArrayList<>();
        for(Long id : feedbackQuestionIds){
            FeedbackQuestion feedbackQuestion = questionMap.get(id);
            FeedbackSubmitQuestion feedbackSubmitQuestion = submitFeedbackQuestionMap.get(id);
            if(feedbackSubmitQuestion == null){
                throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(""));
            }
            FeedbackSubmitAnswer submitAnswer = new FeedbackSubmitAnswer();
            submitAnswer.setFeedbackSubmission(subCourseFeedback);
            submitAnswer.setQuestion(feedbackQuestion);
            if(feedbackQuestion.getQuestionType().equals(EQuestionType.FILL_THE_ANSWER)){
                if(StringUtil.isNotNullOrEmpty(feedbackSubmitQuestion.getSubmitAnswers().getSubmitFilledAnswer().trim())){
                    submitAnswer.setFilledAnswer(feedbackSubmitQuestion.getSubmitAnswers().getSubmitFilledAnswer());
                }
            }else {
                FeedbackAnswer feedbackAnswer = feedbackQuestion.getAnswers().stream()
                        .filter(x -> x.getId().equals(feedbackSubmitQuestion.getSubmitAnswers().getSubmitAnswerId()))
                        .findFirst().orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("")));
                submitAnswer.setSubmitAnswer(feedbackAnswer);
                totalScore += feedbackAnswer.getScore();
                multipleQuestionCount++;
            }
            submitAnswers.add(submitAnswer);
        }
        subCourseFeedback.setFeedbackName(user.getFullName());
        subCourseFeedback.setSubCourse(subCourse);
        subCourseFeedback.setScore(multipleQuestionCount == 0 ? null : (double) totalScore / multipleQuestionCount);
        subCourseFeedback.setFeedbackType(feedbackTemplate.getFeedbackType());
        subCourseFeedback.setOpinion(subCourseFeedbackRequest.getOpinion());
        subCourseFeedback.setSubmitBy(user);
        subCourseFeedback.setSubmitDate(Instant.now());
        subCourseFeedback.setSubmitAnswers(submitAnswers);
        if(StringUtil.isNotNullOrEmpty(subCourseFeedback.getOpinion())){
            subCourseFeedback.setOpinion(subCourseFeedback.getOpinion());
        }
        return subCourseFeedbackRepository.save(subCourseFeedback).getId();
    }

    private SubCourseFeedback findSubCourseFeedbackById(Long id){
        SubCourseFeedback subCourseFeedback = subCourseFeedbackRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("")));
        return subCourseFeedback;
    }

    public Long updateSubCourseFeedBack(Long id, SubCourseFeedbackRequest subCourseFeedbackRequest){
        SubCourseFeedback subCourseFeedback = findSubCourseFeedbackById(id);
        SubCourse subCourse = findSubCourseById(subCourseFeedbackRequest.getSubCourseId());
        FeedbackTemplate feedbackTemplate = subCourse.getFeedbackTemplate();
        User user = SecurityUtil.getCurrentUser();
        long totalScore = 0L;
        int multipleQuestionCount = 0;
        List<FeedbackSubmitQuestion> feedbackSubmitQuestions = subCourseFeedbackRequest.getSubmitQuestions();
        List<Long> feedbackQuestionIds = feedbackTemplate.getQuestions().stream()
                .map(FeedbackQuestion::getId)
                .collect(Collectors.toList());
        Map<Long, FeedbackSubmitQuestion> submitFeedbackQuestionMap = feedbackSubmitQuestions.stream()
                .collect(Collectors.toMap(FeedbackSubmitQuestion::getQuestionId, Function.identity()));
        Map<Long, FeedbackQuestion> questionMap = feedbackTemplate.getQuestions().stream()
                .collect(Collectors.toMap(FeedbackQuestion::getId, Function.identity()));
        List<FeedbackSubmitAnswer> submitAnswers = new ArrayList<>();
        for(Long questionID : feedbackQuestionIds){
            FeedbackQuestion feedbackQuestion = questionMap.get(questionID);
            FeedbackSubmitQuestion feedbackSubmitQuestion = submitFeedbackQuestionMap.get(questionID);
            if(feedbackSubmitQuestion == null){
                throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(""));
            }
            FeedbackSubmitAnswer submitAnswer = new FeedbackSubmitAnswer();
            submitAnswer.setFeedbackSubmission(subCourseFeedback);
            submitAnswer.setQuestion(feedbackQuestion);
            if(feedbackQuestion.getQuestionType().equals(EQuestionType.FILL_THE_ANSWER)){
                if(StringUtil.isNotNullOrEmpty(feedbackSubmitQuestion.getSubmitAnswers().getSubmitFilledAnswer().trim())){
                    submitAnswer.setFilledAnswer(feedbackSubmitQuestion.getSubmitAnswers().getSubmitFilledAnswer());
                }
            }else {
                FeedbackAnswer feedbackAnswer = feedbackQuestion.getAnswers().stream()
                        .filter(x -> x.getId().equals(feedbackSubmitQuestion.getSubmitAnswers().getSubmitAnswerId()))
                        .findFirst().orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("")));
                submitAnswer.setSubmitAnswer(feedbackAnswer);
                totalScore += feedbackAnswer.getScore();
                multipleQuestionCount++;
            }
            submitAnswers.add(submitAnswer);
        }
        subCourseFeedback.setFeedbackName(user.getFullName());
        subCourseFeedback.setSubCourse(subCourse);
        subCourseFeedback.setScore(multipleQuestionCount == 0 ? null : (double) totalScore / multipleQuestionCount);
        subCourseFeedback.setFeedbackType(feedbackTemplate.getFeedbackType());
        subCourseFeedback.setOpinion(subCourseFeedbackRequest.getOpinion());
        subCourseFeedback.setSubmitBy(user);
        subCourseFeedback.setSubmitDate(Instant.now());
        subCourseFeedback.setSubmitAnswers(submitAnswers);
        if(StringUtil.isNotNullOrEmpty(subCourseFeedback.getOpinion())){
            subCourseFeedback.setOpinion(subCourseFeedback.getOpinion());
        }
        return subCourseFeedbackRepository.save(subCourseFeedback).getId();
    }

    public SubCourseFeedbackDto getSubcourseFeedbackById(Long id){
        SubCourseFeedback subCourseFeedback = findSubCourseFeedbackById(id);
        return ConvertUtil.convertSubCourseFeedbackToSubCourseFeedbackDto(subCourseFeedback);
    }

    public List<SubCourseFeedbackDto> getAllSubCourseFeedback(Long subCourseId){
        SubCourse subCourse = findSubCourseById(subCourseId);
        List<SubCourseFeedback> subCourseFeedbacks = subCourseFeedbackRepository.getAllBySubCourse(subCourse);
        return  subCourseFeedbacks.stream()
                .map(ConvertUtil::convertSubCourseFeedbackToSubCourseFeedbackDto)
                .collect(Collectors.toList());
    }

    private SubCourse findSubCourseById(Long id){
        SubCourse subCourse = subCourseRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("")));
        return subCourse;
    }
    /*
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
    */
}
