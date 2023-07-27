package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.constant.EFeedbackType;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.entity.dto.feedback.FeedbackTemplateDto;
import fpt.project.bsmart.entity.request.FeedbackTemplateRequest;
import fpt.project.bsmart.entity.request.StudentSubmitFeedbackRequest;
import fpt.project.bsmart.repository.ClassRepository;
import fpt.project.bsmart.repository.FeedbackSubmissionRepository;
import fpt.project.bsmart.repository.FeedbackTemplateRepository;
import fpt.project.bsmart.service.IFeedbackService;
import fpt.project.bsmart.util.*;
import fpt.project.bsmart.util.specification.FeedbackTemplateSpecificationBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.*;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Empty.*;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Invalid.*;

@Service
public class FeedbackServiceImpl implements IFeedbackService {
    private final FeedbackTemplateRepository feedbackTemplateRepository;
    private final FeedbackSubmissionRepository feedbackSubmissionRepository;
    private final ClassRepository classRepository;
    private final MessageUtil messageUtil;
    @Qualifier("offensiveWord")
    private final ArrayList<String> offensiveWord;
    public FeedbackServiceImpl(FeedbackTemplateRepository feedbackTemplateRepository, FeedbackSubmissionRepository feedbackSubmissionRepository, ClassRepository classRepository, MessageUtil messageUtil, ArrayList<String> offensiveWord) {
        this.feedbackTemplateRepository = feedbackTemplateRepository;
        this.feedbackSubmissionRepository = feedbackSubmissionRepository;
        this.classRepository = classRepository;
        this.messageUtil = messageUtil;
        this.offensiveWord = offensiveWord;
    }

    @Override
    public Long createFeedbackTemplate(FeedbackTemplateDto request) {
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
        ArrayList<FeedbackQuestion> feedbackQuestions = FeedbackUtil.validateFeedbackQuestionsInRequest(request);
        //Format: FeedbackType_FeedbackName
        String templateName = new StringBuilder(request.getType().getName()).append(FeedbackUtil.PREFIX).append(request.getName()).toString();
        FeedbackTemplate feedbackTemplate = new FeedbackTemplate();
        feedbackTemplate.setType(request.getType());
        feedbackTemplate.setName(templateName);
        feedbackTemplate.setQuestions(feedbackQuestions);
        return feedbackTemplateRepository.save(feedbackTemplate).getId();
    }

    @Override
    public Long updateFeedbackTemplate(FeedbackTemplateDto request) {
        if(request.getId() == null){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(EMPTY_FEEDBACK_TEMPLATE_ID));
        }
        FeedbackTemplate feedbackTemplate = findTemplateById(request.getId());
        if(feedbackTemplate.getIsFixed()){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
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
            ArrayList<FeedbackQuestion> feedbackQuestions = FeedbackUtil.validateFeedbackQuestionsInRequest(request);
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
    public ApiPage<FeedbackTemplateDto> getAll(FeedbackTemplateRequest request, Pageable pageable) {
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
        if(feedbackTemplate.getIsFixed()){
            return false;
        }
        feedbackTemplateRepository.delete(feedbackTemplate);
        return true;
    }
    @Override
    public FeedbackTemplateDto getTemplateById(Long id) {
        FeedbackTemplate feedbackTemplate = findTemplateById(id);
        User user = SecurityUtil.getCurrentUser();
        if(SecurityUtil.isHasAnyRole(user, EUserRole.STUDENT)){
            if(!feedbackTemplate.getIsFixed()){
                throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(FORBIDDEN));
            }
        }
        return ConvertUtil.convertFeedbackToFeedbackTemplateDto(feedbackTemplate);
    }


    @Override
    public Boolean assignFeedbackTemplateForClass(Long templateId, Long classId){
        Class clazz = findClassById(classId);
        if(FeedbackUtil.isClassAvailableToFeedback(clazz)){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_CLASS_PERCENTAGE_TO_ASSIGN_TEMPLATE));
        }
        FeedbackTemplate feedbackTemplate = findTemplateById(templateId);
        clazz.setFeedbackTemplate(feedbackTemplate);
        if(!feedbackTemplate.getIsFixed()){
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
        }
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
        FeedbackSubmission feedbackSubmission = new FeedbackSubmission();
        ArrayList<FeedbackSubmitAnswer> submitAnswers = new ArrayList<>();
        for(FeedbackQuestion feedbackQuestion : feedbackTemplate.getQuestions()){
            if(!request.getSubmitAnswers().containsKey(feedbackQuestion.getId())){
                throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(""));
            }
            Long answerId = request.getSubmitAnswers().get(feedbackQuestion.getId());
            FeedbackAnswer feedbackAnswer = feedbackQuestion.getAnswers().stream()
                    .filter(x -> x.getId().equals(answerId))
                    .findFirst()
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("")));
            FeedbackSubmitAnswer submitAnswer = new FeedbackSubmitAnswer();
            submitAnswer.setAnswer(feedbackAnswer);
            submitAnswer.setSubmission(feedbackSubmission);
            submitAnswers.add(submitAnswer);
        }
        feedbackSubmission.setAnswers(submitAnswers);
        if(feedbackTemplate == null){
            throw ApiException.create(HttpStatus.INTERNAL_SERVER_ERROR).withMessage(messageUtil.getLocalMessage(""));
        }
        if(request.getComment() != null && request.getComment().isEmpty()){
            Boolean isContainOffensiveWord = offensiveWord.stream().anyMatch(x -> request.getComment().contains(x));
            if(isContainOffensiveWord){
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
            }
            feedbackSubmission.setComment(request.getComment());
        }
        feedbackSubmission.setRate(request.getRate());
        feedbackSubmission.setTemplate(feedbackTemplate);
        feedbackSubmission.setSubmitBy(user);
        feedbackSubmission.setClazz(clazz);
        feedbackSubmission.setName(FeedbackUtil.generateFeedbackSubmissionName(clazz, user));
        return feedbackSubmissionRepository.save(feedbackSubmission).getId();
    }
}
