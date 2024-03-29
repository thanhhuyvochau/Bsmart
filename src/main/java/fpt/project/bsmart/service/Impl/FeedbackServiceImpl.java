package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.constant.EFeedbackType;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.entity.dto.FeedbackSubmissionDto;
import fpt.project.bsmart.entity.dto.feedback.FeedbackTemplateDto;
import fpt.project.bsmart.entity.request.FeedbackSubmissionSearchRequest;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fpt.project.bsmart.util.Constants.ErrorMessage.*;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Empty.EMPTY_QUESTION_LIST;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Invalid.*;

@Service
public class FeedbackServiceImpl implements IFeedbackService {
    private final FeedbackTemplateRepository feedbackTemplateRepository;
    private final FeedbackSubmissionRepository feedbackSubmissionRepository;
    private final ClassRepository classRepository;
    private final CourseRepository courseRepository;
    private final MentorProfileRepository mentorProfileRepository;
    private final MessageUtil messageUtil;

    private final FeedbackQuestionsRepository feedbackQuestionsRepository;

    private final FeedbackAnswerRepository feedbackAnswerRepository;
    @Qualifier("offensiveWord")
    private final ArrayList<String> offensiveWord;

    public FeedbackServiceImpl(FeedbackTemplateRepository feedbackTemplateRepository, FeedbackSubmissionRepository feedbackSubmissionRepository, ClassRepository classRepository, CourseRepository courseRepository, MentorProfileRepository mentorProfileRepository, MessageUtil messageUtil, FeedbackQuestionsRepository feedbackQuestionsRepository, FeedbackAnswerRepository feedbackAnswerRepository, ArrayList<String> offensiveWord) {
        this.feedbackTemplateRepository = feedbackTemplateRepository;
        this.feedbackSubmissionRepository = feedbackSubmissionRepository;
        this.classRepository = classRepository;
        this.courseRepository = courseRepository;
        this.mentorProfileRepository = mentorProfileRepository;
        this.messageUtil = messageUtil;
        this.feedbackQuestionsRepository = feedbackQuestionsRepository;
        this.feedbackAnswerRepository = feedbackAnswerRepository;
        this.offensiveWord = offensiveWord;
    }

    @Override
    public Long createFeedbackTemplate(FeedbackTemplateRequest request) {
        if (StringUtil.isNullOrEmpty(request.getName())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Empty.EMPTY_FEEDBACK_TEMPLATE_NAME));
        }
        if (request.getType() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_FEEDBACK_TYPE));
        }
        if (!request.getType().equals(EFeedbackType.COURSE) && !request.getType().equals(EFeedbackType.REPORT)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_FEEDBACK_TYPE));
        }
        if (request.getQuestions() == null || request.getQuestions().isEmpty()) {
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
        // kiem tra xem feedback nay đã có người làm chưa
        // nếu có rồi  ko cho updat
        if (Boolean.TRUE.equals(feedbackTemplate.getIsFixed())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(FEEDBACK_TEMPLATE_IS_FIXED));
        }
        if (StringUtil.isNotNullOrEmpty(request.getName())) {
            feedbackTemplate.setName(request.getName());
        }
        if (request.getType() != null) {
            if (!request.getType().equals(EFeedbackType.COURSE) && !request.getType().equals(EFeedbackType.REPORT)) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_FEEDBACK_TYPE));
            }
            feedbackTemplate.setType(request.getType());
        }
        if (request.getQuestions() != null && !request.getQuestions().isEmpty()) {
            List<FeedbackQuestion> feedbackQuestions = FeedbackUtil.validateFeedbackQuestionsInRequest(request, feedbackTemplate);
            feedbackTemplate.setQuestions(feedbackQuestions);
        }
        return feedbackTemplateRepository.save(feedbackTemplate).getId();
    }

    private FeedbackTemplate findTemplateById(Long id) {
        return feedbackTemplateRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(FEEDBACK_TEMPLATE_NOT_FOUND_BY_ID) + id));
    }

    private Class findClassById(Long id) {
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

    public Boolean deleteFeedbackTemplate(Long id) {
        FeedbackTemplate feedbackTemplate = findTemplateById(id);
        if (Boolean.TRUE.equals(feedbackTemplate.getIsFixed())) {
           throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(FEEDBACK_TEMPLATE_IS_FIXED));
        }
        feedbackTemplateRepository.delete(feedbackTemplate);
        return true;
    }

    @Override
    public FeedbackTemplateDto getTemplateById(Long id) {
        FeedbackTemplate feedbackTemplate = findTemplateById(id);
        User user = SecurityUtil.getCurrentUser();
        if (Boolean.TRUE.equals(SecurityUtil.isHasAnyRole(user, EUserRole.STUDENT)) && (Boolean.FALSE.equals(feedbackTemplate.getIsFixed()))) {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(FORBIDDEN));
        }
        return ConvertUtil.convertFeedbackToFeedbackTemplateDto(feedbackTemplate);
    }


    @Override
    public Boolean assignFeedbackTemplateForClass(Long templateId, List<Long> classId) {
        List<Class> classListToSetFeedbackTemplate = classRepository.findAllById(classId);

        FeedbackTemplate feedbackTemplate = findTemplateById(templateId);

        List<Class> classListToSave = new ArrayList<>();
        classListToSetFeedbackTemplate.forEach(aClass -> {
            aClass.setFeedbackTemplate(feedbackTemplate);
            classListToSave.add(aClass);
        });

        classRepository.saveAll(classListToSave);
        return true;
    }

    @Override
    public Boolean changeDefaultTemplate(Long id) {
        FeedbackTemplate updateTemplate = findTemplateById(id);
        updateTemplate.setIsDefault(true);

        FeedbackTemplate defaultTemplate = feedbackTemplateRepository.findByTypeAndIsDefault(updateTemplate.getType(), true);

        if (defaultTemplate != null) {
            defaultTemplate.setIsDefault(false);
            feedbackTemplateRepository.save(defaultTemplate);
        }
        feedbackTemplateRepository.save(updateTemplate);
        return true;
    }


    public Long studentSubmitFeedback(Long classId, StudentSubmitFeedbackRequest request) {
        Class clazz = findClassById(classId);
        User user = SecurityUtil.getCurrentUser();
        ClassUtil.findUserInClass(clazz, user);
        Boolean isAlreadySubmit = feedbackSubmissionRepository.findByClazzAndSubmitBy(clazz, user).isPresent();
        if(isAlreadySubmit){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(STUDENT_ALREADY_FEEDBACK));
        }
        FeedbackTemplate feedbackTemplate = clazz.getFeedbackTemplate();
        if (feedbackTemplate == null) {
            throw ApiException.create(HttpStatus.INTERNAL_SERVER_ERROR).withMessage("Hiện tại lớp chưa có mẫu đánh giá ! ");
        }
        if(request.getCourseRate() < FeedbackUtil.MIN_RATE_PER_SUBMISSION || request.getCourseRate() > FeedbackUtil.MAX_RATE_PER_SUBMISSION){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_FEEDBACK_RATE) + request.getCourseRate());
        }
        if(request.getMentorRate() < FeedbackUtil.MIN_RATE_PER_SUBMISSION || request.getMentorRate() > FeedbackUtil.MAX_RATE_PER_SUBMISSION){
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_FEEDBACK_RATE) + request.getMentorRate());
        }

        FeedbackSubmission feedbackSubmission = new FeedbackSubmission();
        List<FeedbackQuestion> questionsOfTemplate = feedbackTemplate.getQuestions();
        List<Long> questionsIdOfTemplate = questionsOfTemplate.stream().map(FeedbackQuestion::getId).collect(Collectors.toList());
        // kiem tra danh sách học sinh trả lời câu hỏi có đủ chưa
        List<StudentSubmitFeedbackRequest.SubmittedAnswer> submittedAnswers = request.getSubmittedAnswers();
        List<Long> questionsIdOfMemberDoIt = submittedAnswers.stream().map(StudentSubmitFeedbackRequest.SubmittedAnswer::getQuestionId).distinct().collect(Collectors.toList());
        if (questionsIdOfTemplate.size() != questionsIdOfMemberDoIt.size()) {
            throw ApiException.create(HttpStatus.INTERNAL_SERVER_ERROR).withMessage(messageUtil.getLocalMessage(NOT_ENOUGH_ANSWER_IN_FEEDBACK_SUBMISSION));
        }
        List<FeedbackSubmitAnswer> feedbackSubmitAnswers = new ArrayList<>();
        for (StudentSubmitFeedbackRequest.SubmittedAnswer studentSubmitFeedbackRequest : submittedAnswers) {
            if (!questionsIdOfTemplate.contains(studentSubmitFeedbackRequest.getQuestionId())) {
                throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(FEEDBACK_QUESTION_NOT_FOUND_BY_ID) + studentSubmitFeedbackRequest.getQuestionId());
            }
            FeedbackQuestion feedbackQuestion = feedbackTemplate.getQuestions().stream()
                    .filter(x -> x.getId().equals(studentSubmitFeedbackRequest.getQuestionId()))
                    .findFirst()
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(FEEDBACK_QUESTION_NOT_FOUND_BY_ID) + studentSubmitFeedbackRequest.getQuestionId()));
            // Lấy tất cả câu trả lời của câu hỏi
            List<FeedbackAnswer> answersOfQuestion = feedbackQuestion.getAnswers();
            FeedbackAnswer feedbackAnswer = answersOfQuestion.stream().filter(x -> x.getId().equals(studentSubmitFeedbackRequest.getAnswerId()))
                    .findFirst()
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(INVALID_FEEDBACK_ANSWER_OPTION) + studentSubmitFeedbackRequest.getAnswerId()));
            FeedbackSubmitAnswer submitAnswers = new FeedbackSubmitAnswer();
            submitAnswers.setSubmission(feedbackSubmission);
            submitAnswers.setAnswer(feedbackAnswer);
            feedbackSubmitAnswers.add(submitAnswers);
        }
        if(request.getComment() != null && !request.getComment().isEmpty()){
            if(request.getComment().length() < FeedbackUtil.MIN_CHARACTER_IN_COMMENT){
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(NOT_ENOUGH_CHARACTER_IN_FEEDBACK_COMMENT));
            }
            Boolean isContainOffensiveWord = offensiveWord.stream().anyMatch(x -> request.getComment().contains(x));
            if(isContainOffensiveWord){
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(FEEDBACK_COMMENT_CONTAIN_OFFENSIVE_WORD));
            }
            feedbackSubmission.setComment(request.getComment());
        }
        feedbackSubmission.setCourseRate(request.getCourseRate());
        feedbackSubmission.setMentorRate(request.getMentorRate());
        feedbackSubmission.setTemplate(feedbackTemplate);
        feedbackSubmission.setAnswers(feedbackSubmitAnswers);
        feedbackSubmission.setSubmitBy(user);
        feedbackSubmission.setClazz(clazz);
        feedbackSubmissionRepository.save(feedbackSubmission);
        if(Boolean.FALSE.equals(feedbackTemplate.getIsFixed())){
            feedbackTemplate.setIsFixed(true);
        }
        return feedbackSubmission.getId();
    }

//    public Long studentUpdateFeedback(Long submissionId, StudentSubmitFeedbackRequest request) {
//        if (request.getRate() < FeedbackUtil.MIN_RATE_PER_SUBMISSION
//                || request.getRate() > FeedbackUtil.MAX_RATE_PER_SUBMISSION) {
//            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
//        }
//        FeedbackSubmission feedbackSubmission = feedbackSubmissionRepository.findById(submissionId)
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("") + submissionId));
//        FeedbackTemplate feedbackTemplate = feedbackSubmission.getTemplate();
//        if (feedbackTemplate == null) {
//            throw ApiException.create(HttpStatus.INTERNAL_SERVER_ERROR).withMessage(messageUtil.getLocalMessage(""));
//        }
//        ArrayList<FeedbackSubmitAnswer> submitAnswers = FeedbackUtil.validateSubmittedAnswer(feedbackSubmission, request);
//        feedbackSubmission.setAnswers(submitAnswers);
//        if (StringUtil.isNotNullOrEmpty(request.getComment())) {
//            Boolean isContainOffensiveWord = offensiveWord.stream().anyMatch(x -> request.getComment().contains(x));
//            if (Boolean.TRUE.equals(isContainOffensiveWord)) {
//                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
//            }
//            feedbackSubmission.setComment(request.getComment());
//        }
//        feedbackSubmission.setRate(request.getRate());
//
//        feedbackTemplate.setIsFixed(true);
//        feedbackTemplateRepository.save(feedbackTemplate);
//        return feedbackSubmissionRepository.save(feedbackSubmission).getId();
//    }

    public ApiPage<FeedbackSubmissionResponse> getClassFeedback(Long classId, Pageable pageable) {
        Class clazz = findClassById(classId);
        User user = SecurityUtil.getCurrentUser();
//        Boolean isClassBelongToMentor = Objects.equals(user, clazz.getMentor());
//        if (!isClassBelongToMentor) {
//            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(FORBIDDEN));
//        }
        Page<FeedbackSubmission> feedbackSubmissionPage = feedbackSubmissionRepository.findAllByClazz(clazz, pageable);
        List<FeedbackSubmissionResponse> responses = feedbackSubmissionPage.getContent().stream()
                .map(ConvertUtil::convertFeedbackSubmissionToResponse)
                .collect(Collectors.toList());
        return PageUtil.convert(new PageImpl<>(responses, pageable, feedbackSubmissionPage.getTotalElements()));
    }

    public FeedbackSubmissionResponse studentGetFeedback(Long classId){
        Class clazz = findClassById(classId);
        Optional<User> optionalUser = SecurityUtil.getCurrentUserOptional();
        User user = SecurityUtil.getUserOrThrowException(optionalUser);
        boolean isStudentBelongToClass = clazz.getStudentClasses().stream()
                .map(StudentClass::getStudent)
                .anyMatch(student -> student.getId().equals(user.getId()));
        if(!isStudentBelongToClass){
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(STUDENT_NOT_BELONG_TO_CLASS));
        }
        FeedbackSubmissionSpecificationBuilder builder = FeedbackSubmissionSpecificationBuilder.feedbackSubmissionSpecificationBuilder()
                .filterByCourse(clazz.getCourse().getId())
                .filterBySubmitted(user);
        List<FeedbackSubmission> feedbackSubmissions = feedbackSubmissionRepository.findAll(builder.build());
        if(feedbackSubmissions.isEmpty()){
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Học sinh chưa feedback");
        }
        FeedbackSubmission feedbackSubmission = feedbackSubmissions.get(0);
        return ConvertUtil.convertFeedbackSubmissionToResponse(feedbackSubmission);
    }

    public FeedbackResponse getCourseFeedback(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + courseId));
        FeedbackSubmissionSpecificationBuilder builder = FeedbackSubmissionSpecificationBuilder.feedbackSubmissionSpecificationBuilder()
                .filterByCourse(course.getId());
        List<FeedbackSubmission> feedbackSubmissions = feedbackSubmissionRepository.findAll(builder.build());
        FeedbackResponse response = new FeedbackResponse();
        List<FeedbackSubmissionDto> submissions = feedbackSubmissions.stream()
                .map(x -> ConvertUtil.convertFeedbackSubmissionToFeedbackSubmissionDto(x, true))
                .collect(Collectors.toList());
        setValueForFeedbackResponse(response, submissions, feedbackSubmissions.stream().map(FeedbackSubmission::getCourseRate), feedbackSubmissions);
        return response;
    }

    public FeedbackResponse getMentorFeedback(Long mentorId) {
        MentorProfile mentorProfile = mentorProfileRepository.findById(mentorId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(MENTOR_PROFILE_NOT_FOUND_BY_ID) + mentorId));
        FeedbackSubmissionSpecificationBuilder builder = FeedbackSubmissionSpecificationBuilder.feedbackSubmissionSpecificationBuilder()
                .filterByMentor(mentorProfile.getId());
        List<FeedbackSubmission> feedbackSubmissions = feedbackSubmissionRepository.findAll(builder.build());
        FeedbackResponse response = new FeedbackResponse();
        List<FeedbackSubmissionDto> submissions = feedbackSubmissions.stream()
                .map(x -> ConvertUtil.convertFeedbackSubmissionToFeedbackSubmissionDto(x, false))
                .collect(Collectors.toList());
        setValueForFeedbackResponse(response, submissions, feedbackSubmissions.stream().map(FeedbackSubmission::getMentorRate), feedbackSubmissions);
        return response;
    }

    private void setValueForFeedbackResponse(FeedbackResponse response, List<FeedbackSubmissionDto> submissions, Stream<Integer> integerStream, List<FeedbackSubmission> feedbackSubmissions) {
        List<Integer> rates = integerStream.collect(Collectors.toList());
        Map<Integer, Long> rateCount = FeedbackUtil.getRateCount(rates);
        response.setAverageRate(FeedbackUtil.calculateAverageRate(rateCount));
        response.setRateCount(rateCount);
        response.setSubmissions(submissions);
        response.setSubmissionCount(feedbackSubmissions.size());
    }

    public ApiPage<FeedbackSubmissionDto> getFeedbackSubmission(FeedbackSubmissionSearchRequest request, Pageable pageable){
        FeedbackSubmissionSpecificationBuilder builder = FeedbackSubmissionSpecificationBuilder.feedbackSubmissionSpecificationBuilder()
                .filterByRate(request.getRate(), request.getIsCourse());
        builder = Boolean.TRUE.equals(request.getIsCourse()) ? builder.filterByCourse(request.getId()) : builder.filterByMentor(request.getId());
        Page<FeedbackSubmission> feedbackSubmissionPage = feedbackSubmissionRepository.findAll(builder.build(), pageable);
        List<FeedbackSubmission> submissions = feedbackSubmissionPage.getContent().stream().collect(Collectors.toList());
        Page<FeedbackSubmission> feedbackSubmissions = new PageImpl<>(submissions, pageable, submissions.size());
        return PageUtil.convert(feedbackSubmissions.map(x -> ConvertUtil.convertFeedbackSubmissionToFeedbackSubmissionDto(x, request.getIsCourse())));
    }
}
