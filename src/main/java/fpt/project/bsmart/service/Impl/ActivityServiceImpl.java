package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.builder.ActivityBuilder;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.constant.*;
import fpt.project.bsmart.entity.dto.ActivityDetailDto;
import fpt.project.bsmart.entity.dto.QuizDto;
import fpt.project.bsmart.entity.dto.QuizSubmittionDto;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.request.activity.LessonDto;
import fpt.project.bsmart.entity.request.activity.MentorCreateAnnouncementForClass;
import fpt.project.bsmart.entity.request.activity.MentorCreateResourceRequest;
import fpt.project.bsmart.entity.request.activity.MentorCreateSectionForCourse;
import fpt.project.bsmart.entity.response.Avtivity.*;
import fpt.project.bsmart.entity.response.QuizSubmissionResultResponse;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.IActivityService;
import fpt.project.bsmart.util.*;
import fpt.project.bsmart.util.adapter.MinioAdapter;
import fpt.project.bsmart.util.specification.QuizSubmissionSpecificationBuilder;
import fpt.project.bsmart.validator.ActivityValidator;
import fpt.project.bsmart.validator.AssignmentValidator;
import io.minio.ObjectWriteResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.*;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Empty.*;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Invalid.*;

@Service
@Transactional
public class ActivityServiceImpl implements IActivityService {


    @Value("${minio.endpoint}")
    String minioUrl;

    private final CourseRepository courseRepository;

    private final ActivityRepository activityRepository;

    private final LessonRepository lessonRepository;
    private final QuizSubmissionRepository quizSubmissionRepository;
    private final QuizRepository quizRepository;
    private final MinioAdapter minioAdapter;
    private final MessageUtil messageUtil;
    private final PasswordEncoder encoder;

    private final AssignmentFileRepository assignmentFileRepository;
    private final ClassRepository classRepository;
    private final AssignmentRepository assignmentRepository;
    private final AssignmentSubmittionRepository assignmentSubmittionRepository;

    public ActivityServiceImpl(CourseRepository courseRepository, ActivityRepository activityRepository, LessonRepository lessonRepository, QuizSubmissionRepository quizSubmissionRepository, QuizRepository quizRepository, MinioAdapter minioAdapter, MessageUtil messageUtil, PasswordEncoder encoder, AssignmentFileRepository assignmentFileRepository, ClassRepository classRepository, AssignmentRepository assignmentRepository, AssignmentSubmittionRepository assignmentSubmittionRepository) {
        this.courseRepository = courseRepository;
        this.activityRepository = activityRepository;
        this.lessonRepository = lessonRepository;
        this.quizSubmissionRepository = quizSubmissionRepository;
        this.quizRepository = quizRepository;
        this.minioAdapter = minioAdapter;
        this.messageUtil = messageUtil;
        this.encoder = encoder;
        this.assignmentFileRepository = assignmentFileRepository;
        this.classRepository = classRepository;
        this.assignmentRepository = assignmentRepository;
        this.assignmentSubmittionRepository = assignmentSubmittionRepository;
    }


    @Override
    public Boolean addActivity(ActivityRequest activityRequest, ECourseActivityType type) throws IOException {
        User currentUser = SecurityUtil.getCurrentUser();
        Course course = courseRepository.findById(activityRequest.getCourseId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.COURSE_NOT_FOUND_BY_ID) + activityRequest.getCourseId()));
        User mentor = course.getCreator();
        if (!SecurityUtil.isHasAnyRole(currentUser, EUserRole.MANAGER) && !Objects.equals(currentUser.getId(), mentor.getId())) {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.FORBIDDEN));
        }
        List<Long> authorizeClassesId = activityRequest.getAuthorizeClasses();
        List<Class> authorizeClasses = classRepository.findAllById(authorizeClassesId);
        ActivityBuilder activityBuilder = ActivityBuilder.getBuilder()
                .withName(activityRequest.getName())
                .withVisible(activityRequest.getVisible())
                .withCourse(course)
                .withType(type);

        Activity parentActivity = null;
        List<ActivityAuthorize> parentActivityAuthorize = null;
        if (!Objects.equals(type, ECourseActivityType.SECTION)) {
            parentActivity = activityRepository.findByIdAndType(activityRequest.getParentActivityId(), ECourseActivityType.SECTION).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                    .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.SECTION_NOT_FOUND_BY_ID) + activityRequest.getParentActivityId()));
            parentActivityAuthorize = parentActivity.getActivityAuthorizes();
            activityBuilder.withParent(parentActivity);
        }
        Activity activity = activityBuilder.build();
        List<ActivityAuthorize> activityAuthorizes = activity.getActivityAuthorizes();
        for (Class authorizeClass : authorizeClasses) {
            ActivityAuthorize activityAuthorize = new ActivityAuthorize();
            activityAuthorize.setActivity(activity);
            activityAuthorize.setAuthorizeClass(authorizeClass);
            activityAuthorizes.add(activityAuthorize);
            if (parentActivityAuthorize != null) {
                boolean existInParent = parentActivityAuthorize.stream()
                        .anyMatch(authorize -> Objects.equals(authorize.getAuthorizeClass().getId(), authorizeClass.getId()));
                if (!existInParent) {
                    ActivityAuthorize newParentActivityAuthorize = new ActivityAuthorize();
                    newParentActivityAuthorize.setActivity(parentActivity);
                    newParentActivityAuthorize.setAuthorizeClass(authorizeClass);
                    parentActivityAuthorize.add(newParentActivityAuthorize);
                }
            }
        }
        boolean result = createDetailActivity(activityRequest, type, activity);
        if (result) {
            activityRepository.save(activity);
        }
        return result;
    }

    @Override
    public List<Long> mentorCreateSectionForCourse(Long id, MentorCreateSectionForCourse sessions) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));


        User currentUserAccountLogin = SecurityUtil.getCurrentUser();

        User creator = course.getCreator();
        if (!creator.equals(currentUserAccountLogin)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(YOU_DO_NOT_HAVE_PERMISSION_TO_CREATE_CLASS_FOR_THIS_COURSE));
        }

        return mentorCreateSectionForCourse(sessions, course);
    }

    private List<Long> mentorCreateSectionForCourse(MentorCreateSectionForCourse session, Course course) {
        List<Activity> activityList = new ArrayList<>();
        List<fpt.project.bsmart.entity.Lesson> lessonList = new ArrayList<>();

        Activity activitySection = new Activity();
        activitySection.setName(session.getName());
        activitySection.setType(ECourseActivityType.SECTION);
        activitySection.setCourse(course);


        List<LessonDto> lessons = session.getLessons();
        lessons.forEach(mentorCreateLessonForCourse -> {
            Activity activityLesson = new Activity();
            activityLesson.setParent(activitySection);
            activityLesson.setType(ECourseActivityType.LESSON);
            activityLesson.setCourse(course);
            fpt.project.bsmart.entity.Lesson lesson = new fpt.project.bsmart.entity.Lesson();
            lesson.setDescription(mentorCreateLessonForCourse.getDescription());
            lesson.setActivity(activityLesson);
            lessonList.add(lesson);
            activityList.add(activityLesson);
        });
        activityList.add(activitySection);


        lessonRepository.saveAll(lessonList);
        List<Activity> activityListSaved = activityRepository.saveAll(activityList);
        return activityListSaved.stream().map(Activity::getId).collect(Collectors.toList());
    }

    private boolean createDetailActivity(ActivityRequest activityRequest, ECourseActivityType type, Activity activity) throws IOException {
        switch (type) {
            case QUIZ:
                Quiz quiz = addQuiz((AddQuizRequest) activityRequest, activity);
                activity.setQuiz(quiz);
                return true;
            case ASSIGNMENT:
                Assignment assignment = addAssignment((AssignmentRequest) activityRequest, activity);
                return true;
            case SECTION:
                // Just return for section -> section work as folder for others activities with no content inside
                return true;
            case RESOURCE:
                Resource resource = addResource((MentorCreateResourceRequest) activityRequest, activity);
                activity.setResource(resource);
                return true;
            case ANNOUNCEMENT:
                ClassAnnouncement announcement = addAnnouncement((MentorCreateAnnouncementForClass) activityRequest, activity);
                activity.setAnnouncement(announcement);
                return true;
            case LESSON:
                Lesson lesson = addLesson((LessonRequest) activityRequest, activity);
                return true;
            default:
                throw ApiException.create(HttpStatus.NO_CONTENT).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Invalid.INVALID_ACTIVITY_TYPE) + type);
        }
    }

    private boolean editDetailActivity(ActivityRequest activityRequest, ECourseActivityType type, Activity activity) throws IOException {
        switch (type) {
            case QUIZ:
                Quiz quiz = editQuiz((AddQuizRequest) activityRequest, activity);
                activity.setQuiz(quiz);
                activityRepository.save(activity);
                break;
            case ASSIGNMENT:
                editAssignment((AssignmentRequest) activityRequest, activity);
                activityRepository.save(activity);
                return true;
            case SECTION:
                // Just return for section -> section work as folder for others activities with no content inside
                return true;
            case RESOURCE:
                editResource((MentorCreateResourceRequest) activityRequest, activity);
                activityRepository.save(activity);
                return true;
            case ANNOUNCEMENT:
                editAnnouncement((MentorCreateAnnouncementForClass) activityRequest, activity);
                activityRepository.save(activity);
                return true;
            case LESSON:
                editLesson((LessonRequest) activityRequest, activity);
                activityRepository.save(activity);
                return true;
            default:
                throw ApiException.create(HttpStatus.NO_CONTENT).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Invalid.INVALID_ACTIVITY_TYPE) + type);
        }
        return false;
    }

    public Quiz addQuiz(AddQuizRequest addQuizRequest, Activity activity) {
        ActivityValidator.validateQuizInfo(addQuizRequest);
        if (addQuizRequest.getQuizQuestions() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(EMPTY_QUESTION_LIST));
        }
        List<QuizQuestionRequest> questions = addQuizRequest.getQuizQuestions();
        if (questions.size() < QuizUtil.MIN_QUESTIONS_PER_QUIZ || questions.size() > QuizUtil.MAX_QUESTIONS_PER_QUIZ) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_QUESTION_LIST_SIZE) + questions.size());
        }
        Quiz quiz = new Quiz();
        List<QuizQuestion> quizQuestions = new ArrayList<>();
        for (QuizQuestionRequest question : questions) {
            if (question.getQuestion().trim().isEmpty()) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(EMPTY_QUESTION));
            }
            List<QuizAnswerRequest> answers = question.getAnswers();
            if (answers.size() < QuizUtil.MIN_ANSWERS_PER_QUESTION || answers.size() > QuizUtil.MAX_ANSWERS_PER_QUESTION) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(SINGLE_RIGHT_ANSWER_IN_MULTIPLE_TYPE_QUESTION));
            }
            boolean isContainEmptyAnswer = answers.stream().anyMatch(x -> x.getAnswer().trim().isEmpty());
            if (isContainEmptyAnswer) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(EMPTY_ANSWER));
            }

            long numOfRightAnswer = answers.stream()
                    .filter(QuizAnswerRequest::getRight)
                    .count();
            if (numOfRightAnswer == 0) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(MISSING_RIGHT_ANSWER_IN_QUESTION));
            }
            if (question.getQuestionType().equals(QuestionType.SINGLE) && numOfRightAnswer > 1) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(MULTIPLE_RIGHT_ANSWER_IN_SINGLE_TYPE_QUESTION) + numOfRightAnswer);
            }

            QuizQuestion quizQuestion = new QuizQuestion();
            List<QuizAnswer> quizAnswers = new ArrayList<>();
            for (QuizAnswerRequest answer : answers) {
                QuizAnswer quizAnswer = new QuizAnswer();
                quizAnswer.setAnswer(answer.getAnswer());
                quizAnswer.setIsRight(answer.getRight());
                quizAnswer.setQuizQuestion(quizQuestion);
                quizAnswers.add(quizAnswer);
            }
            quizQuestion.setQuestion(question.getQuestion());
            quizQuestion.setType(question.getQuestionType());
            quizQuestion.setAnswers(quizAnswers);
            quizQuestion.setQuiz(quiz);
            quizQuestions.add(quizQuestion);
        }
        quiz.setCode(addQuizRequest.getCode());
        quiz.setStartDate(addQuizRequest.getStartDate());
        quiz.setEndDate(addQuizRequest.getEndDate());
        quiz.setTime(addQuizRequest.getTime());
        quiz.setStatus(QuizStatus.PENDING);
        quiz.setDefaultPoint(addQuizRequest.getDefaultPoint());
        quiz.setIsSuffleQuestion(addQuizRequest.getIsSuffleQuestion());
        quiz.setIsAllowReview(addQuizRequest.getIsAllowReview());
        quiz.setAllowReviewAfterMin(addQuizRequest.getAllowReviewAfterMin());
        quiz.setPassword(encoder.encode(addQuizRequest.getPassword()));
        quiz.setActivity(activity);
        quiz.setQuizQuestions(quizQuestions);
        return quiz;
    }

    public ClassAnnouncement addAnnouncement(MentorCreateAnnouncementForClass request, Activity activity) {
        if (StringUtil.isNullOrEmpty(request.getContent())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("content is empty");
        }
        if (StringUtil.isNullOrEmpty(request.getTitle())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("title is empty");
        }
        ClassAnnouncement announcement = new ClassAnnouncement();
        announcement.setTitle(request.getTitle());
        announcement.setContent(request.getContent());
        announcement.setVisible(request.getVisible());
        announcement.setActivity(activity);
        return announcement;
    }

    public ClassAnnouncement editAnnouncement(MentorCreateAnnouncementForClass request, Activity activity) {
        if (StringUtil.isNullOrEmpty(request.getContent())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("content is empty");
        }
        if (StringUtil.isNullOrEmpty(request.getTitle())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("title is empty");
        }
        ClassAnnouncement announcement = activity.getAnnouncement();
        announcement.setTitle(request.getTitle());
        announcement.setContent(request.getContent());
        announcement.setVisible(request.getVisible());
        announcement.setActivity(activity);
        return announcement;
    }

    private Resource addResource(MentorCreateResourceRequest request, Activity activity) throws IOException {
        Resource resource = new Resource();
        resource.setActivity(activity);
        resource.setUrl(createResource(request.getFile()));
        return resource;
    }

    private Resource editResource(MentorCreateResourceRequest request, Activity activity) throws IOException {
        Resource resource = activity.getResource();
        resource.setUrl(createResource(request.getFile()));
        return resource;
    }

    private String createResource(MultipartFile file) throws IOException {
        ObjectWriteResponse response = minioAdapter.uploadFile(file.getOriginalFilename(), file.getContentType(), file.getInputStream(), file.getSize());
        return UrlUtil.buildUrl(minioUrl, response);
    }

    private Assignment addAssignment(AssignmentRequest request, Activity activity) throws IOException {
        Instant now = Instant.now();
        Instant startDate = request.getStartDate();
        Instant endDate = request.getEndDate();

        if (startDate.isBefore(now)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Invalid.INVALID_START_NOW_DATE));
        }
        if (endDate.isBefore(now)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Invalid.INVALID_END_NOW_DATE));
        }
        if (startDate.isAfter(endDate)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Invalid.INVALID_START_END_DATE));
        }

        Assignment assignment = new Assignment();
        assignment.setDescription(request.getDescription());
        assignment.setStartDate(request.getStartDate());
        assignment.setEndDate(request.getEndDate());
        assignment.setEditBeForSubmitMin(request.getEditBeForSubmitMin());
        assignment.setMaxFileSubmit(request.getMaxFileSubmit());
        assignment.setMaxFileSize(request.getMaxFileSize());
        assignment.setStatus(now.equals(request.getStartDate()) ? EAssignmentStatus.OPENING : EAssignmentStatus.PENDING);
        assignment.setActivity(activity);
        assignment.setPassPoint(request.getPassPoint());
        // Lấy file đính kèm của assignment
        List<MultipartFile> attachFiles = request.getAttachFiles();
        for (MultipartFile attachFile : attachFiles) {
            assignment.getAssignmentFiles().add(createAssignmentFile(attachFile, assignment, FileType.ATTACH));
        }
        assignmentRepository.save(assignment);
        return assignment;
    }

    private AssignmentFile createAssignmentFile(MultipartFile attachFile, Assignment assignment, FileType fileType) throws IOException {
        String originalFilename = attachFile.getOriginalFilename();
        String name = originalFilename + "_" + Instant.now().toString();
        ObjectWriteResponse objectWriteResponse = minioAdapter.uploadFile(name, attachFile.getContentType(), attachFile.getInputStream(), attachFile.getSize());

        AssignmentFile assignmentFile = new AssignmentFile();
        assignmentFile.setName(originalFilename);
        assignmentFile.setFileType(fileType);
        assignmentFile.setUrl(UrlUtil.buildUrl(minioUrl, objectWriteResponse));
        assignmentFile.setAssignment(assignment);
        return assignmentFile;
    }

    @Override
    public Boolean deleteActivity(Long id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.ACTIVITY_NOT_FOUND_BY_ID) + id));
        if (activity.getFixed()) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(ACTIVITY_STATUS_HAS_FIXED));
        }
        User creator = activity.getCourse().getCreator();
        User currentUser = SecurityUtil.getCurrentUser();
        if (!Objects.equals(creator.getId(), currentUser.getId()) || !SecurityUtil.isHasAnyRole(currentUser, EUserRole.MANAGER, EUserRole.ADMIN, EUserRole.TEACHER)) {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.FORBIDDEN));
        }
        activityRepository.delete(activity);
        return true;
    }

    @Override
    public Boolean changeActivityVisible(Long id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.ACTIVITY_NOT_FOUND_BY_ID) + id));
        User subCourseMentor = activity.getCourse().getCreator();
        User currentUser = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        if (activity.getFixed()) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(ACTIVITY_STATUS_HAS_FIXED));
        }
        if (!Objects.equals(subCourseMentor.getId(), currentUser.getId()) || !SecurityUtil.isHasAnyRole(currentUser, EUserRole.MANAGER, EUserRole.ADMIN)) {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.FORBIDDEN));
        }
        activity.setVisible(activity.isVisible());
        return true;
    }

    //
    @Override
    public Boolean submitAssignment(Long id, SubmitAssignmentRequest request) throws IOException {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.ACTIVITY_NOT_FOUND_BY_ID) + id));
        Assignment assignment = activity.getAssignment();
        if (assignment == null) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Hoạt động này không phải là Assigment!");
        }
        User currentUser = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        Course course = activity.getCourse();
        Optional<Class> clazzOfUser = currentUser.getStudentClasses().stream()
                .map(StudentClass::getClazz)
                .filter(clazz -> clazz.getCourse().getId().equals(course.getId()))
                .findFirst();
        Class clazz = clazzOfUser.orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy lớp của người dùng với hoạt động đang tương tác"));
        List<MultipartFile> submittedFiles = request.getSubmittedFiles();
        if (!ActivityValidator.isAuthorizeForClass(clazz, activity)) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Lớp bạn không có thẩm quyền với assignment");

        } else if (!AssignmentValidator.isValidSubmitDate(assignment)) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Ngày nộp không hợp lệ");

        } else if (!AssignmentValidator.isValidNumberOfSubmitFile(assignment, submittedFiles)) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Số lượng file phải ít hơn:" + assignment.getMaxFileSubmit());

        } else if (!AssignmentValidator.isValidFileExtension(assignment, submittedFiles)) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Định dạng file không hợp lệ (docx, doc, xlsx, xls, csv, pptx, ppt, pdf)");
        }
        StudentClass studentClass = ClassUtil.findUserInClass(clazz, currentUser);
        AssignmentSubmition assignmentSubmition = new AssignmentSubmition();
        assignmentSubmition.setAssignment(assignment);
        assignmentSubmition.setStudentClass(studentClass);
        List<AssignmentFile> assignmentFiles = assignmentSubmition.getAssignmentFiles();
        for (MultipartFile submittedFile : request.getSubmittedFiles()) {
            AssignmentFile assignmentFile = createAssignmentFile(submittedFile, assignment, FileType.SUBMIT);
            assignmentFile.setNote(request.getNote());
            assignmentFile.setAssignmentSubmition(assignmentSubmition);
            assignmentFiles.add(assignmentFile);
        }
        assignmentSubmittionRepository.save(assignmentSubmition);
        return true;
    }

    @Override
    public List<MentorGetSectionForCourse> mentorGetSectionOfCourse(Long id) {
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));
        if (!course.getCreator().equals(currentUserAccountLogin)) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_DOES_NOT_BELONG_TO_THE_TEACHER));
        }

        List<MentorGetSectionForCourse> sectionForCourses = ActivityUtil.mentorGetSectionOfCourse(course);

        return sectionForCourses;
    }

    @Override
    public Boolean mentorUpdateSectionForCourse(Long id, MentorUpdateSectionForCourse updateRequest) {
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));
        if (!course.getCreator().equals(currentUserAccountLogin)) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_DOES_NOT_BELONG_TO_THE_TEACHER));
        }
        ActivityUtil.checkCourseIsAllowedUpdateOrDelete(course);


        Activity activity = activityRepository.findByIdAndCourse(updateRequest.getId(), course)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(ACTIVITY_NOT_FOUND_BY_ID) + id));
        activity.setName(updateRequest.getName());

        List<MentorGetLessonForCourse> lessons = updateRequest.getLessons();
        List<fpt.project.bsmart.entity.Lesson> lessonList = new ArrayList<>();
        for (MentorGetLessonForCourse lessonUpdate : lessons) {
            fpt.project.bsmart.entity.Lesson lesson = lessonRepository.findById(lessonUpdate.getId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(LESSON_NOT_FOUND_BY_ID) + id));
            lesson.setDescription(lessonUpdate.getDescription());
            lessonList.add(lesson);
        }
        lessonRepository.saveAll(lessonList);
        activityRepository.save(activity);
        return true;
    }

    @Override
    public Boolean mentorDeleteSectionForCourse(Long id, List<MentorDeleteSectionForCourse> deleteRequest) {
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));
        if (!course.getCreator().equals(currentUserAccountLogin)) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_DOES_NOT_BELONG_TO_THE_TEACHER));
        }
        ActivityUtil.checkCourseIsAllowedUpdateOrDelete(course);

        List<Activity> activityDeleteList = new ArrayList<>();
        for (MentorDeleteSectionForCourse mentorDeleteSectionForCourse : deleteRequest) {
            Activity activity = activityRepository.findByIdAndCourse(mentorDeleteSectionForCourse.getId(), course)
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(ACTIVITY_NOT_FOUND_BY_ID) + mentorDeleteSectionForCourse.getId()));
            List<MentorDeleteLessonForCourse> lessons = mentorDeleteSectionForCourse.getLessons();

            for (MentorDeleteLessonForCourse lessonDelete : lessons) {
                fpt.project.bsmart.entity.Lesson lesson = lessonRepository.findById(lessonDelete.getId())
                        .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(LESSON_NOT_FOUND_BY_ID) + lessonDelete.getId()));
                Activity activityLesson = lesson.getActivity();
                activityLesson.setParent(null);
                activityDeleteList.add(activityLesson);
            }
            if (activity.getChildren().size() == 0) {
                activityDeleteList.add(activity);
            }
        }

        activityRepository.deleteAll(activityDeleteList);
        return true;
    }

    @Override
    public Boolean editActivity(Long id, ActivityRequest activityRequest, ECourseActivityType type) throws IOException {
        User currentUser = SecurityUtil.getCurrentUser();
        Course course = courseRepository.findById(activityRequest.getCourseId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.COURSE_NOT_FOUND_BY_ID) + activityRequest.getCourseId()));
        Activity editedActivity = activityRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(ACTIVITY_NOT_FOUND_BY_ID) + id));
        if (editedActivity.getFixed()) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(ACTIVITY_STATUS_HAS_FIXED));
        }
        User mentor = course.getCreator();
        if (!SecurityUtil.isHasAnyRole(currentUser, EUserRole.MANAGER) && !Objects.equals(currentUser.getId(), mentor.getId())) {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.FORBIDDEN));
        }
        List<Long> authorizeClassesId = activityRequest.getAuthorizeClasses();
        List<Class> authorizeClasses = classRepository.findAllById(authorizeClassesId);
        ActivityBuilder activityBuilder = ActivityBuilder.getBuilder()
                .withName(activityRequest.getName())
                .withVisible(activityRequest.getVisible())
                .withCourse(course)
                .withType(type);

        Activity parentActivity = null;
        List<ActivityAuthorize> parentActivityAuthorize = null;
        if (!Objects.equals(type, ECourseActivityType.SECTION)) {
            parentActivity = activityRepository.findByIdAndType(activityRequest.getParentActivityId(), ECourseActivityType.SECTION).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                    .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.SECTION_NOT_FOUND_BY_ID) + activityRequest.getParentActivityId()));
            parentActivityAuthorize = parentActivity.getActivityAuthorizes();
            activityBuilder.withParent(parentActivity);
        }
        Activity activity = activityBuilder.modify(editedActivity);
        activity.getActivityAuthorizes().clear();
        List<ActivityAuthorize> activityAuthorizes = activity.getActivityAuthorizes();
        for (Class authorizeClass : authorizeClasses) {
            ActivityAuthorize activityAuthorize = new ActivityAuthorize();
            activityAuthorize.setActivity(activity);
            activityAuthorize.setAuthorizeClass(authorizeClass);
            activityAuthorizes.add(activityAuthorize);
            if (parentActivityAuthorize != null) {
                boolean existInParent = parentActivityAuthorize.stream()
                        .anyMatch(authorize -> Objects.equals(authorize.getAuthorizeClass().getId(), authorizeClass.getId()));
                if (!existInParent) {
                    ActivityAuthorize newParentActivityAuthorize = new ActivityAuthorize();
                    newParentActivityAuthorize.setActivity(parentActivity);
                    newParentActivityAuthorize.setAuthorizeClass(authorizeClass);
                    parentActivityAuthorize.add(newParentActivityAuthorize);
                }
            }
        }
        return editDetailActivity(activityRequest, type, activity);
    }

    private Assignment editAssignment(AssignmentRequest request, Activity activity) throws IOException {
        Instant now = Instant.now();
        Assignment assignment = activity.getAssignment();
        Instant startDate = request.getStartDate();
        Instant endDate = request.getEndDate();
        long minDiffOfStartDate = TimeUtil.compareTwoInstantTruncated(startDate, assignment.getStartDate(), ChronoUnit.MINUTES);
        long minDiffOfEndDate = TimeUtil.compareTwoInstantTruncated(endDate, assignment.getEndDate(), ChronoUnit.MINUTES);
        if (minDiffOfEndDate == 0 && minDiffOfStartDate == 0) {
            if (startDate.isBefore(now)) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Invalid.INVALID_START_NOW_DATE));
            }
            if (endDate.isBefore(now)) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Invalid.INVALID_END_NOW_DATE));

            }
            if (startDate.isAfter(endDate)) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Invalid.INVALID_START_END_DATE));
            }
        }
        assignment.setDescription(request.getDescription());
        assignment.setStartDate(request.getStartDate());
        assignment.setEndDate(request.getEndDate());
        assignment.setEditBeForSubmitMin(request.getEditBeForSubmitMin());
        assignment.setMaxFileSubmit(request.getMaxFileSubmit());
        assignment.setMaxFileSize(request.getMaxFileSize());
        assignment.setStatus(now.equals(request.getStartDate()) ? EAssignmentStatus.OPENING : EAssignmentStatus.PENDING);
        // Lấy file đính kèm của assignment
        List<MultipartFile> attachFiles = request.getAttachFiles();
        List<AssignmentFile> existedAssignmentFiles = assignment.getAssignmentFiles();
        for (MultipartFile attachFile : attachFiles) {
            AssignmentFile newAssignmentFile = createAssignmentFile(attachFile, assignment, FileType.ATTACH);
            existedAssignmentFiles.add(newAssignmentFile);
        }
        return assignment;
    }

    private Quiz editQuiz(AddQuizRequest request, Activity activity) {
        Quiz quiz = activity.getQuiz();
        if (quiz == null) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(""));
        }
        if (!quiz.getStatus().equals(QuizStatus.PENDING)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }
        Instant now = Instant.now();
        boolean isValidQuizDate = request.getStartDate().isAfter(now) || request.getEndDate().isAfter(now)
                || request.getStartDate().isBefore(request.getEndDate());
        if (isValidQuizDate) {
            quiz.setStartDate(request.getStartDate());
            quiz.setEndDate(request.getEndDate());
        }
        if (request.getIsSuffleQuestion() != null) {
            quiz.setIsSuffleQuestion(request.getIsSuffleQuestion());
        }
        if (StringUtil.isNotNullOrEmpty(request.getPassword())) {
            quiz.setPassword(request.getPassword());
        }
        if (request.getIsAllowReview() != null) {
            quiz.setIsAllowReview(request.getIsAllowReview());
        }
        if (request.getAllowReviewAfterMin() != null && request.getAllowReviewAfterMin() > 0) {
            quiz.setAllowReviewAfterMin(request.getAllowReviewAfterMin());
        }
        if (request.getDefaultPoint() != null && request.getDefaultPoint() >= 0
                && request.getDefaultPoint() <= 10) {
            quiz.setDefaultPoint(request.getDefaultPoint());
        }
        if (request.getTime() != null && request.getTime() > 0) {
            quiz.setTime(request.getTime());
        }
        if (StringUtil.isNotNullOrEmpty(request.getCode())) {
            quiz.setCode(request.getCode());
        }
        return quiz;
    }

    @Override
    public ActivityDetailDto getDetailActivity(Long id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.ACTIVITY_NOT_FOUND_BY_ID) + id));
        Course course = activity.getCourse();
        User currentUser = SecurityUtil.getCurrentUser();

        if (SecurityUtil.isHasAnyRole(currentUser, EUserRole.MANAGER, EUserRole.ADMIN)) {
            return ConvertUtil.convertActivityDetailToDto(activity);
        }

        ActivityDetailDto activityDto = ConvertUtil.convertActivityDetailToDto(activity);
        if (SecurityUtil.isHasAnyRole(currentUser, EUserRole.TEACHER) && Objects.equals(course.getCreator().getId(), currentUser.getId())) {
            ResponseUtil.responseForRole(EUserRole.TEACHER);
            return activityDto;
        }

        Optional<Class> clazzOfUser = currentUser.getStudentClasses().stream()
                .map(StudentClass::getClazz)
                .filter(clazz -> clazz.getCourse().getId().equals(course.getId()))
                .findFirst();
        boolean isStudentOfClass = clazzOfUser.isPresent();
        if (SecurityUtil.isHasAnyRole(currentUser, EUserRole.STUDENT) && isStudentOfClass) {
            ResponseUtil.responseForRole(EUserRole.STUDENT);
            return activityDto;
        } else {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.FORBIDDEN));
        }
    }

    private Quiz findQuizById(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(QUIZ_NOT_FOUND_BY_ID) + id));
    }

    private User validateUser(Activity activity) {
        User user = SecurityUtil.getCurrentUser();
        Course course = activity.getCourse();
        Class classes = user.getStudentClasses().stream()
                .map(StudentClass::getClazz)
                .filter(x -> x.getCourse().getId().equals(course.getId()))
                .findFirst()
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(STUDENT_NOT_BELONG_TO_CLASS)));
        if (!ActivityValidator.isAuthorizeForClass(classes, activity)) {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(ACTIVITY_NOT_AUTHORIZED_FOR_YOUR_CLASS) + classes.getId());
        }
        return user;
    }

    private void isAvailableToAttempt(Quiz quiz, User user) {
        Instant currentTime = Instant.now();
        if (currentTime.isBefore(quiz.getStartDate()) || currentTime.isAfter(quiz.getEndDate())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_QUIZ_ATTEMPT_TIME));
        }
        if (!quiz.getStatus().equals(QuizStatus.OPENING)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_QUIZ_STATUS_FOR_ATTEMPT));
        }
        if (!quiz.getIsUnlimitedAttempt()) {
            int submitTimes = quiz.getQuizSubmittions().stream()
                    .filter(x -> x.getSubmittedBy().equals(user))
                    .collect(Collectors.toList()).size();
            boolean isAvailableToAttempt = submitTimes < quiz.getAttemptNumber();
            if (!isAvailableToAttempt) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_NUMBER_OF_STUDENT_ATTEMPT) + quiz.getAttemptNumber());
            }
        }
    }

    private Quiz getQuizByActivityId(Long id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(ACTIVITY_NOT_FOUND_BY_ID) + id));
        boolean isQuizActivity = ActivityUtil.isCorrectActivityType(activity, ECourseActivityType.QUIZ);
        if (!isQuizActivity) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(ACTIVITY_NOT_FOUND_BY_ID) + id);
        }
        return activity.getQuiz();
    }

    public QuizDto studentAttemptQuiz(Long id, StudentAttemptQuizRequest request) {
        Quiz quiz = getQuizByActivityId(id);
        User user = validateUser(quiz.getActivity());
        isAvailableToAttempt(quiz, user);

        if (request.getPassword().trim().isEmpty()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(EMPTY_PASSWORD));
        }
        boolean isMatchPassword = encoder.matches(request.getPassword(), quiz.getPassword());
        if (!isMatchPassword) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_PASSWORD));
        }
        QuizDto quizDto = ConvertUtil.convertQuizToQuizDto(quiz, true);
        return quizDto;
    }

    public QuizSubmissionResultResponse studentViewQuizResult(Long id) {
        QuizSubmittion submittion = quizSubmissionRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(QUIZ_SUBMISSION_NOT_FOUND_BY_ID) + id));
        User user = SecurityUtil.getCurrentUser();
        boolean isBelongToMentorOrStudent;
        if (SecurityUtil.isHasAnyRole(user, EUserRole.TEACHER)) {
            isBelongToMentorOrStudent = ActivityUtil.isBelongToMentor(submittion.getQuiz().getActivity());
        } else {
            isBelongToMentorOrStudent = Objects.equals(user, submittion.getSubmittedBy());
        }
        if (!isBelongToMentorOrStudent) {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(FORBIDDEN));
        }
        return ConvertUtil.convertQuizSubmissionToSubmissionResult(submittion);
    }

    public ApiPage<QuizSubmissionResultResponse> teacherViewQuizResult(Long id, QuizResultRequest request, Pageable pageable) {
        Quiz quiz = findQuizById(id);
        boolean isBelongToMentor = ActivityUtil.isBelongToMentor(quiz.getActivity());
        if (!isBelongToMentor) {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(FORBIDDEN));
        }
        QuizSubmissionSpecificationBuilder builder = QuizSubmissionSpecificationBuilder.quizSubmissionSpecificationBuilder()
                .queryByQuiz(quiz.getId())
                .queryByClass(request.getClassId())
                .queryByPoint(request.getComparison(), request.getPoint())
                .queryByStatus(request.getStatus())
                .isAfter(request.getStartDate())
                .isBefore(request.getEndDate());
        Page<QuizSubmittion> quizSubmittionPage = quizSubmissionRepository.findAll(builder.build(), pageable);
        return PageUtil.convert(quizSubmittionPage.map(ConvertUtil::convertQuizSubmissionToSubmissionResult));
    }

    public QuizSubmittionDto reviewQuiz(Long id) {
        QuizSubmittion quizSubmittion = quizSubmissionRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(QUIZ_SUBMISSION_NOT_FOUND_BY_ID) + id));
        User user = SecurityUtil.getCurrentUser();
        boolean isTeacher = SecurityUtil.isHasAnyRole(user, EUserRole.TEACHER);
        if (isTeacher) {
            boolean isBelongToMentor = ActivityUtil.isBelongToMentor(quizSubmittion.getQuiz().getActivity());
            if (!isBelongToMentor) {
                throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(FORBIDDEN));
            }
        } else {
            boolean isProposer = quizSubmittion.getSubmittedBy().equals(user);
            if (!isProposer) {
                throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(FORBIDDEN));
            }
            if (!quizSubmittion.getQuiz().getIsAllowReview()) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(NOT_ALLOW_REVIEW_QUIZ));
            }
            long reviewAfter = quizSubmittion.getQuiz().getAllowReviewAfterMin();
            Instant endTime = quizSubmittion.getCreated().plus(reviewAfter, ChronoUnit.MINUTES);
            Instant now = Instant.now();
            if (endTime.isAfter(now)) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(QUIZ_HAVE_NOT_FINISHED) + endTime);
            }
        }
        return ConvertUtil.convertQuizSubmittionToQuizSubmittionDto(quizSubmittion);
    }


    public Boolean studentSubmitQuiz(Long activityId, SubmitQuizRequest request) {
        Quiz quiz = getQuizByActivityId(activityId);
        User user = validateUser(quiz.getActivity());
        isAvailableToAttempt(quiz, user);
        List<SubmittedQuestionRequest> submittedQuestions = request.getSubmittedQuestions();
        List<QuizQuestion> quizQuestions = quiz.getQuizQuestions();
        QuizSubmittion quizSubmittion = new QuizSubmittion();
        List<QuizSubmitQuestion> submitQuestions = new ArrayList<>();
        for (QuizQuestion quizQuestion : quizQuestions) {
            SubmittedQuestionRequest submittedQuestion = submittedQuestions.stream()
                    .filter(x -> x.getQuestionId().equals(quizQuestion.getId())).findFirst()
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(QUESTION_NOT_FOUND_BY_ID) + quizQuestion.getId()));
            QuizSubmitQuestion submitQuestion = new QuizSubmitQuestion();
            submitQuestion.setQuizSubmittion(quizSubmittion);
            submitQuestion.setQuizQuestion(quizQuestion);

            List<QuizSubmitAnswer> quizSubmitAnswers;
            List<QuizAnswer> quizAnswers = quizQuestion.getAnswers();
            List<Long> submittedAnswers = submittedQuestion.getAnswerId();
            switch (quizQuestion.getType()) {
                case SINGLE:
                    quizSubmitAnswers = handleSingleChoice(submittedAnswers, quizAnswers, submitQuestion);
                    break;
                case MULTIPLE:
                    quizSubmitAnswers = handleMultipleChoice(submittedAnswers, quizAnswers, submitQuestion);
                    break;
                default:
                    throw ApiException.create(HttpStatus.NO_CONTENT).withMessage(messageUtil.getLocalMessage(INVALID_QUESTION_TYPE) + quizQuestion.getType());
            }
            submitQuestion.setQuizSubmitAnswers(quizSubmitAnswers);
            submitQuestions.add(submitQuestion);
        }
        Map<String, Float> submissionResultMap = calculateSubmissionResult(submitQuestions);
        int correctNumber = submissionResultMap.get(QuizUtil.CORRECT_QUESTION_NUMBER_KEY).intValue();
        float point = submissionResultMap.get(QuizUtil.POINT_KEY);
        quizSubmittion.setQuiz(quiz);
        quizSubmittion.setStatus(request.getStatus());
        quizSubmittion.setSubmittedBy(user);
        quizSubmittion.setSubmitQuestions(submitQuestions);
        quizSubmittion.setCorrectNumber(correctNumber);
        quizSubmittion.setIncorrectNumber(quiz.getQuizQuestions().size() - correctNumber);
        quizSubmittion.setPoint(point < quiz.getDefaultPoint() ? quiz.getDefaultPoint() : point);
        quizSubmissionRepository.save(quizSubmittion);
        return true;
    }

    private Map<String, Float> calculateSubmissionResult(List<QuizSubmitQuestion> quizSubmitQuestions) {
        int correctNumber = 0;
        float totalPoint = 0;
        for (QuizSubmitQuestion quizSubmitQuestion : quizSubmitQuestions) {
            long pointPerQuestion = 10 / quizSubmitQuestions.size();
            List<QuizSubmitAnswer> quizSubmitAnswers = quizSubmitQuestion.getQuizSubmitAnswers();
            if (!quizSubmitAnswers.isEmpty()) {
                if (quizSubmitQuestion.getQuizQuestion().getType().equals(QuestionType.SINGLE)) {
                    if (quizSubmitAnswers.get(0).getQuizAnswer().getIsRight()) {
                        correctNumber++;
                        totalPoint += pointPerQuestion;
                    }
                } else {
                    List<QuizAnswer> quizAnswers = quizSubmitQuestion.getQuizQuestion().getAnswers();
                    long submittedCorrectAnswerCount = quizSubmitAnswers.stream()
                            .filter(x -> x.getQuizAnswer().getIsRight())
                            .count();
                    long quizCorrectAnswerCount = quizAnswers.stream()
                            .filter(x -> x.getIsRight())
                            .count();
                    if (submittedCorrectAnswerCount > 0) {
                        correctNumber++;
                        long correctPercentage = submittedCorrectAnswerCount / quizCorrectAnswerCount;
                        totalPoint += pointPerQuestion * correctPercentage;
                    }
                }
            }
        }
        Map<String, Float> resultMap = new HashMap<>();
        resultMap.put(QuizUtil.CORRECT_QUESTION_NUMBER_KEY, (float) correctNumber);
        resultMap.put(QuizUtil.POINT_KEY, totalPoint);
        return resultMap;
    }

    private List<QuizSubmitAnswer> handleSingleChoice(List<Long> submittedAnswers, List<QuizAnswer> quizAnswers, QuizSubmitQuestion quizSubmitQuestion) {
        List<QuizSubmitAnswer> quizSubmitAnswers = new ArrayList<>();
        if (!submittedAnswers.isEmpty()) {
            boolean isOnlyOneAnswer = submittedAnswers.size() == 1;
            if (!isOnlyOneAnswer) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(MULTIPLE_RIGHT_ANSWER_IN_SINGLE_TYPE_QUESTION));
            }
            Long submittedAnswerId = submittedAnswers.get(0);
            QuizSubmitAnswer quizSubmitAnswer = getQuizSubmitAnswerBySubmittedId(quizAnswers, submittedAnswerId, quizSubmitQuestion);
            quizSubmitAnswers.add(quizSubmitAnswer);
        }
        return quizSubmitAnswers;
    }

    private List<QuizSubmitAnswer> handleMultipleChoice(List<Long> submittedAnswers, List<QuizAnswer> quizAnswers, QuizSubmitQuestion quizSubmitQuestion) {
        List<QuizSubmitAnswer> quizSubmitAnswers = new ArrayList<>();
        if (!submittedAnswers.isEmpty()) {
            for (int i = 0; i < submittedAnswers.size(); i++) {
                Long submittedAnswerId = submittedAnswers.get(i);
                QuizSubmitAnswer quizSubmitAnswer = getQuizSubmitAnswerBySubmittedId(quizAnswers, submittedAnswerId, quizSubmitQuestion);
                quizSubmitAnswers.add(quizSubmitAnswer);
            }
        }
        return quizSubmitAnswers;
    }

    private QuizSubmitAnswer getQuizSubmitAnswerBySubmittedId(List<QuizAnswer> quizAnswers, Long submittedAnswerId, QuizSubmitQuestion quizSubmitQuestion) {
        QuizSubmitAnswer quizSubmitAnswer = new QuizSubmitAnswer();
        QuizAnswer quizAnswer = quizAnswers.stream()
                .filter(x -> x.getId().equals(submittedAnswerId)).findFirst()
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(ANSWER_NOT_FOUND_BY_ID) + submittedAnswerId));
        quizSubmitAnswer.setQuizAnswer(quizAnswer);
        quizSubmitAnswer.setQuizSubmitQuestion(quizSubmitQuestion);
        return quizSubmitAnswer;
    }


    private Lesson addLesson(LessonRequest request, Activity activity) throws IOException {
        Lesson lesson = new Lesson(request.getDescription(), activity);
        lessonRepository.save(lesson);
        return lesson;
    }

    private Lesson editLesson(LessonRequest request, Activity activity) throws IOException {
        Lesson lesson = activity.getLesson();
        lesson.setDescription(request.getDescription());
        lessonRepository.save(lesson);
        return lesson;
    }
}
