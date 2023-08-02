package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.constant.EFeedbackType;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.entity.dto.feedback.FeedbackTemplateDto;
import fpt.project.bsmart.entity.request.FeedbackTemplateRequest;
import fpt.project.bsmart.entity.request.FeedbackTemplateSearchRequest;
import fpt.project.bsmart.entity.request.StudentSubmitFeedbackRequest;
import fpt.project.bsmart.entity.response.FeedbackSubmissionResponse;
import fpt.project.bsmart.entity.response.FeedbackResponse;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.IFeedbackService;
import fpt.project.bsmart.util.*;
import fpt.project.bsmart.util.specification.FeedbackSubmissionSpecificationBuilder;
import fpt.project.bsmart.util.specification.FeedbackTemplateSpecificationBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.*;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Empty.EMPTY_FEEDBACK_TEMPLATE_ID;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Empty.EMPTY_QUESTION_LIST;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Invalid.INVALID_FEEDBACK_TYPE;

@Service
public class FeedbackServiceImpl implements IFeedbackService {
    private final FeedbackTemplateRepository feedbackTemplateRepository;
    private final FeedbackSubmissionRepository feedbackSubmissionRepository;
    private final ClassRepository classRepository;
    private final CourseRepository courseRepository;
    private final MentorProfileRepository mentorProfileRepository;
    private final MessageUtil messageUtil;
    @Qualifier("offensiveWord")
    private final ArrayList<String> offensiveWord;
    public FeedbackServiceImpl(FeedbackTemplateRepository feedbackTemplateRepository, FeedbackSubmissionRepository feedbackSubmissionRepository, ClassRepository classRepository, CourseRepository courseRepository, MentorProfileRepository mentorProfileRepository, MessageUtil messageUtil, ArrayList<String> offensiveWord) {
        this.feedbackTemplateRepository = feedbackTemplateRepository;
        this.feedbackSubmissionRepository = feedbackSubmissionRepository;
        this.classRepository = classRepository;
        this.courseRepository = courseRepository;
        this.mentorProfileRepository = mentorProfileRepository;
        this.messageUtil = messageUtil;
        this.offensiveWord = offensiveWord;
    }

    @Override
    public Long createFeedbackTemplate(FeedbackTemplateRequest request) {
        if(StringUtil.isNullOrEmpty(request.getName())){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Empty.EMPTY_FEEDBACK_TEMPLATE_NAME));
        }
        if(request.getType() == null){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_FEEDBACK_TYPE));
        }
        if(!request.getType().equals(EFeedbackType.COURSE) && !request.getType().equals(EFeedbackType.REPORT)){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_FEEDBACK_TYPE));
        }
        if(request.getQuestions() == null || request.getQuestions().isEmpty()){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(EMPTY_QUESTION_LIST));
        }
        FeedbackTemplate feedbackTemplate = new FeedbackTemplate();
        List<FeedbackQuestion> feedbackQuestions = FeedbackUtil.validateFeedbackQuestionsInRequest(request, feedbackTemplate);
        //Format: FeedbackType_FeedbackName
        String templateName = new StringBuilder(request.getType().getName()).append(FeedbackUtil.PREFIX).append(request.getName()).toString();
        feedbackTemplate.setType(request.getType());
        feedbackTemplate.setName(templateName);
        feedbackTemplate.setQuestions(feedbackQuestions);
        return feedbackTemplateRepository.save(feedbackTemplate).getId();
    }

    @Override
    public Long updateFeedbackTemplate(Long id, FeedbackTemplateRequest request) {
        FeedbackTemplate feedbackTemplate = findTemplateById(id);
        if(Boolean.TRUE.equals(feedbackTemplate.getIsFixed())){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(FEEDBACK_TEMPLATE_IS_FIXED) + id);
        }
        if(StringUtil.isNotNullOrEmpty(request.getName())){
            feedbackTemplate.setName(request.getName());
        }
        if(request.getType() != null){
            if(!request.getType().equals(EFeedbackType.COURSE) && !request.getType().equals(EFeedbackType.REPORT)){
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_FEEDBACK_TYPE));
            }
            feedbackTemplate.setType(request.getType());
        }
        if(request.getQuestions() != null || !request.getQuestions().isEmpty()){
            List<FeedbackQuestion> feedbackQuestions = FeedbackUtil.validateFeedbackQuestionsInRequest(request, feedbackTemplate);
            feedbackTemplate.setQuestions(feedbackQuestions);
        }
        return feedbackTemplateRepository.save(feedbackTemplate).getId();
    }

    private FeedbackTemplate findTemplateById(Long id){
        return feedbackTemplateRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(FEEDBACK_TEMPLATE_NOT_FOUND_BY_ID) + id));
    }

    private Class findClassById(Long id){
        return classRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CLASS_NOT_FOUND_BY_ID) + id));
    }
    @Override
    public ApiPage<FeedbackTemplateDto> getAll(FeedbackTemplateSearchRequest request, Pageable pageable) {
        FeedbackTemplateSpecificationBuilder builder = FeedbackTemplateSpecificationBuilder.feedbackTemplateSpecificationBuilder()
                .filterByType(request.getType())
                .filterByName(request.getName());
        Page<FeedbackTemplate> feedbackTemplatePage = feedbackTemplateRepository.findAll(builder.build(), pageable);
        List<FeedbackTemplateDto> feedbackTemplateDtos = feedbackTemplatePage.getContent().stream()
                .map(ConvertUtil::convertFeedbackToFeedbackTemplateDto)
                .collect(Collectors.toList());
        return PageUtil.convert(new PageImpl<>(feedbackTemplateDtos, pageable, feedbackTemplatePage.getTotalElements()));
    }

    public Boolean deleteFeedbackTemplate(Long id){
        FeedbackTemplate feedbackTemplate = findTemplateById(id);
        if(Boolean.TRUE.equals(feedbackTemplate.getIsFixed())){
            return false;
        }
        feedbackTemplateRepository.delete(feedbackTemplate);
        return true;
    }
    @Override
    public FeedbackTemplateDto getTemplateById(Long id) {
        FeedbackTemplate feedbackTemplate = findTemplateById(id);
        User user = SecurityUtil.getCurrentUser();
        if(Boolean.TRUE.equals(SecurityUtil.isHasAnyRole(user, EUserRole.STUDENT)) && (Boolean.FALSE.equals(feedbackTemplate.getIsFixed()))){
                throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(FORBIDDEN));
        }
        return ConvertUtil.convertFeedbackToFeedbackTemplateDto(feedbackTemplate);
    }


    @Override
    public Boolean assignFeedbackTemplateForClass(Long templateId, Long classId){
        Class clazz = findClassById(classId);
//        if(Boolean.TRUE.equals(FeedbackUtil.isClassAvailableToFeedback(clazz))){
//            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_CLASS_PERCENTAGE_TO_ASSIGN_TEMPLATE));
//        }
        FeedbackTemplate feedbackTemplate = findTemplateById(templateId);
        clazz.setFeedbackTemplate(feedbackTemplate);
        if(Boolean.FALSE.equals(feedbackTemplate.getIsFixed())){
            feedbackTemplate.setIsFixed(true);
        }
        classRepository.save(clazz);
        return true;
    }

    @Override
    public Boolean changeDefaultTemplate(Long id) {
        FeedbackTemplate updateTemplate = findTemplateById(id);
        FeedbackTemplate defaultTemplate = feedbackTemplateRepository.findByTypeAndIsDefault(updateTemplate.getType(), true);
        changeDefaultTemplate(updateTemplate);
        if (defaultTemplate != null) {
            defaultTemplate.setIsDefault(false);
            feedbackTemplateRepository.save(defaultTemplate);
        }
        feedbackTemplateRepository.save(updateTemplate);
        return true;
    }

    private void changeDefaultTemplate(FeedbackTemplate feedbackTemplate){
        feedbackTemplate.setIsDefault(true);
        feedbackTemplate.setIsFixed(true);
    }

    public Long studentSubmitFeedback(Long classId, StudentSubmitFeedbackRequest request){
        if(request.getRate() < FeedbackUtil.MIN_RATE_PER_SUBMISSION
                || request.getRate() > FeedbackUtil.MAX_RATE_PER_SUBMISSION){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }
        Class clazz = findClassById(classId);
        User user = SecurityUtil.getCurrentUser();
        ClassUtil.findUserInClass(clazz, user);
/*
        if(!FeedbackUtil.isClassAvailableToFeedback(clazz)){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }
*/
        FeedbackTemplate feedbackTemplate = clazz.getFeedbackTemplate();
        if(feedbackTemplate == null){
            throw ApiException.create(HttpStatus.INTERNAL_SERVER_ERROR).withMessage(messageUtil.getLocalMessage(""));
        }
        FeedbackSubmission feedbackSubmission = new FeedbackSubmission();
        feedbackSubmission.setTemplate(feedbackTemplate);
        ArrayList<FeedbackSubmitAnswer> submitAnswers = FeedbackUtil.validateSubmittedAnswer(feedbackSubmission, request);
        feedbackSubmission.setAnswers(submitAnswers);
        if(StringUtil.isNotNullOrEmpty(request.getComment())){
            Boolean isContainOffensiveWord = offensiveWord.stream().anyMatch(x -> request.getComment().contains(x));
            if(Boolean.TRUE.equals(isContainOffensiveWord)){
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
            }
            feedbackSubmission.setComment(request.getComment());
        }
        feedbackSubmission.setRate(request.getRate());
        feedbackSubmission.setSubmitBy(user);
        feedbackSubmission.setClazz(clazz);
        feedbackSubmission.setName(FeedbackUtil.generateFeedbackSubmissionName(clazz, user));
        return feedbackSubmissionRepository.save(feedbackSubmission).getId();
    }

    public Long studentUpdateFeedback(Long submissionId, StudentSubmitFeedbackRequest request){
        if(request.getRate() < FeedbackUtil.MIN_RATE_PER_SUBMISSION
                || request.getRate() > FeedbackUtil.MAX_RATE_PER_SUBMISSION){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }
        FeedbackSubmission feedbackSubmission = feedbackSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("") + submissionId));
        FeedbackTemplate feedbackTemplate = feedbackSubmission.getTemplate();
        if(feedbackTemplate == null){
            throw ApiException.create(HttpStatus.INTERNAL_SERVER_ERROR).withMessage(messageUtil.getLocalMessage(""));
        }
        ArrayList<FeedbackSubmitAnswer> submitAnswers = FeedbackUtil.validateSubmittedAnswer(feedbackSubmission, request);
        feedbackSubmission.setAnswers(submitAnswers);
        if(StringUtil.isNotNullOrEmpty(request.getComment())){
            Boolean isContainOffensiveWord = offensiveWord.stream().anyMatch(x -> request.getComment().contains(x));
            if(Boolean.TRUE.equals(isContainOffensiveWord)){
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
            }
            feedbackSubmission.setComment(request.getComment());
        }
        feedbackSubmission.setRate(request.getRate());
        return feedbackSubmissionRepository.save(feedbackSubmission).getId();
    }

    public ApiPage<FeedbackSubmissionResponse> getClassFeedback(Long classId, Pageable pageable){
        Class clazz = findClassById(classId);
        User user = SecurityUtil.getCurrentUser();
        Boolean isClassBelongToMentor = Objects.equals(user, clazz.getMentor());
        if(!isClassBelongToMentor){
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(FORBIDDEN));
        }
        Page<FeedbackSubmission> feedbackSubmissionPage = feedbackSubmissionRepository.findAllByClazz(clazz, pageable);
        List<FeedbackSubmissionResponse> responses = feedbackSubmissionPage.getContent().stream()
                .map(ConvertUtil::convertFeedbackSubmissionToResponse)
                .collect(Collectors.toList());
        return PageUtil.convert(new PageImpl<>(responses, pageable, feedbackSubmissionPage.getTotalElements()));
    }

    public FeedbackResponse getCourseFeedback(Long courseId){
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + courseId));
        FeedbackSubmissionSpecificationBuilder builder = FeedbackSubmissionSpecificationBuilder.feedbackSubmissionSpecificationBuilder()
                .filterByCourse(course.getId());
        List<FeedbackSubmission> feedbackSubmissions = feedbackSubmissionRepository.findAll(builder.build());
        FeedbackResponse feedbackResponse = new FeedbackResponse();
        List<FeedbackResponse.FeedbackSubmission> submissions = feedbackSubmissions.stream()
                .map(ConvertUtil::convertFeedbackSubmissionToFeedbackResponse)
                .collect(Collectors.toList());
        feedbackResponse.setAverageRate(FeedbackUtil.calculateCourseRate(feedbackSubmissions));
        feedbackResponse.setSubmissions(submissions);
        feedbackResponse.setSubmissionCount(feedbackSubmissions.size());
        return feedbackResponse;
    }

    public FeedbackResponse getMentorFeedback(Long mentorId){
        MentorProfile mentorProfile = mentorProfileRepository.findById(mentorId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(MENTOR_PROFILE_NOT_FOUND_BY_ID) + mentorId));
        FeedbackSubmissionSpecificationBuilder builder = FeedbackSubmissionSpecificationBuilder.feedbackSubmissionSpecificationBuilder()
                .filterByMentor(mentorProfile.getUser().getId());
        List<FeedbackSubmission> feedbackSubmissions = feedbackSubmissionRepository.findAll(builder.build());
        FeedbackResponse response = new FeedbackResponse();
        List<FeedbackResponse.FeedbackSubmission> submissions = feedbackSubmissions.stream()
                .map(ConvertUtil::convertFeedbackSubmissionToFeedbackResponse)
                .collect(Collectors.toList());
        response.setSubmissions(submissions);
        response.setAverageRate(FeedbackUtil.calculateCourseRate(feedbackSubmissions));
        response.setSubmissionCount(feedbackSubmissions.size());
        return response;
    }
}
