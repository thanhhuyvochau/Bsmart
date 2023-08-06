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
import java.util.Optional;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.*;
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
        if (!request.getType().equals(EFeedbackType.COURSE)) {
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
        // nếu có rồi  ko cho update 

        if (Boolean.TRUE.equals(feedbackTemplate.getIsFixed())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Bản đánh giá đã có lớp sử dụng ! Không thể cập nhật");
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
        if (request.getQuestions() != null || !request.getQuestions().isEmpty()) {
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
            return false;
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

        if (Boolean.FALSE.equals(feedbackTemplate.getIsFixed())) {
            feedbackTemplate.setIsFixed(true);
        }
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


    public Long studentSubmitFeedback(Long classId, List<StudentSubmitFeedbackRequest> request) {


        Class clazz = findClassById(classId);
        User user = SecurityUtil.getCurrentUser();
        ClassUtil.findUserInClass(clazz, user);

        FeedbackTemplate feedbackTemplate = clazz.getFeedbackTemplate();


        if (feedbackTemplate == null) {
            throw ApiException.create(HttpStatus.INTERNAL_SERVER_ERROR).withMessage("Hiện tại lớp chưa có mẫu đánh giá ! ");
        }
        FeedbackSubmission feedbackSubmission = new FeedbackSubmission();
        feedbackSubmission.setTemplate(feedbackTemplate);
        List<FeedbackQuestion> questionsOfTemplate = feedbackTemplate.getQuestions();
        List<Long> questionsIdOfTemplate = questionsOfTemplate.stream().map(FeedbackQuestion::getId).collect(Collectors.toList());

        // kiem tra danh sách học sinh trả lời câu hỏi có đủ chưa
        List<Long> questionsIdOfMemberDoIt = request.stream().map(studentSubmitFeedbackRequest -> studentSubmitFeedbackRequest.getQuestionId()).collect(Collectors.toList());
        if (questionsIdOfTemplate.size() > questionsIdOfMemberDoIt.size()) {
            throw ApiException.create(HttpStatus.INTERNAL_SERVER_ERROR).withMessage("Vui lòng trả lời đầy đủ câu hỏi của mẫu đánh giá!");
        }
        List<FeedbackSubmitAnswer> feedbackSubmitAnswers = new ArrayList<>();
        for (StudentSubmitFeedbackRequest studentSubmitFeedbackRequest : request) {


            if (!questionsIdOfTemplate.contains(studentSubmitFeedbackRequest.getQuestionId())) {
                throw ApiException.create(HttpStatus.INTERNAL_SERVER_ERROR).withMessage("Mẫu đánh giá không tìm thấy câu hỏi! Vui lòng kiểm tra lại");
            }
            FeedbackQuestion feedbackQuestion = feedbackQuestionsRepository.findById(studentSubmitFeedbackRequest.getQuestionId())
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                            .withMessage(messageUtil.getLocalMessage(FEEDBACK_QUESTION_NOT_FOUND_BY_ID) + studentSubmitFeedbackRequest.getQuestionId()));

            // Lấy tất cả câu trả lời của câu hỏi
            List<FeedbackAnswer> answersOfQuestion = feedbackQuestion.getAnswers();



                for (FeedbackAnswer feedbackAnswer : answersOfQuestion) {

                    Long id = feedbackAnswer.getId();
                    if (id.equals(studentSubmitFeedbackRequest.getAnswerId())) {
                        FeedbackSubmitAnswer submitAnswers = new FeedbackSubmitAnswer();
                        submitAnswers.setSubmission(feedbackSubmission);
                        submitAnswers.setAnswer(feedbackAnswer);
                        feedbackSubmitAnswers.add(submitAnswers);
                    }
                }




        }


        feedbackSubmission.setAnswers(feedbackSubmitAnswers);
        feedbackSubmission.setSubmitBy(user);
        feedbackSubmission.setClazz(clazz);
        return feedbackSubmissionRepository.save(feedbackSubmission).getId();
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

    public FeedbackResponse getCourseFeedback(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + courseId));
        FeedbackSubmissionSpecificationBuilder builder = FeedbackSubmissionSpecificationBuilder.feedbackSubmissionSpecificationBuilder()
                .filterByCourse(course.getId());
        List<FeedbackSubmission> feedbackSubmissions = feedbackSubmissionRepository.findAll(builder.build());
        FeedbackResponse feedbackResponse = new FeedbackResponse();
        List<FeedbackResponse.FeedbackSubmission> submissions = feedbackSubmissions.stream()
                .map(ConvertUtil::convertFeedbackSubmissionToFeedbackResponse)
                .collect(Collectors.toList());
//        feedbackResponse.setAverageRate(FeedbackUtil.calculateCourseRate(feedbackSubmissions));
        feedbackResponse.setSubmissions(submissions);
        feedbackResponse.setSubmissionCount(feedbackSubmissions.size());
        return feedbackResponse;
    }

    public FeedbackResponse getMentorFeedback(Long mentorId) {
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
//        response.setAverageRate(FeedbackUtil.calculateCourseRate(feedbackSubmissions));
        response.setSubmissionCount(feedbackSubmissions.size());
        return response;
    }

//    @Override
//    public Boolean createQuestionTemplate(List<FeedbackTemplateRequest.FeedbackQuestionRequest> questions) {
//        List<FeedbackQuestion> feedbackQuestions = FeedbackUtil.validateFeedbackQuestionsDefaultInRequest(questions);
//        List<FeedbackQuestion> feedbackQuestions1 = feedbackQuestionsRepository.saveAll(feedbackQuestions);
//        return true ;
//    }
}
