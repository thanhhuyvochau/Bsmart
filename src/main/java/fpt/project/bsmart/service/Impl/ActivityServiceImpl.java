package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.builder.ActivityBuilder;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.*;
import fpt.project.bsmart.entity.dto.QuizSubmittionDto;
import fpt.project.bsmart.entity.request.*;

import fpt.project.bsmart.entity.request.activity.MentorCreateLessonForCourse;
import fpt.project.bsmart.entity.request.activity.MentorCreateSectionForCourse;
import fpt.project.bsmart.repository.*;

import fpt.project.bsmart.repository.ActivityRepository;
import fpt.project.bsmart.repository.CourseRepository;
import fpt.project.bsmart.repository.QuizSubmissionRepository;

import fpt.project.bsmart.service.IActivityService;
import fpt.project.bsmart.util.*;
import fpt.project.bsmart.util.adapter.MinioAdapter;
import io.minio.ObjectWriteResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.COURSE_NOT_FOUND_BY_ID;
import static fpt.project.bsmart.util.Constants.ErrorMessage.YOU_DO_NOT_HAVE_PERMISSION_TO_CREATE_CLASS_FOR_THIS_COURSE;

@Service
@Transactional
public class ActivityServiceImpl implements IActivityService, Cloneable {


    @Value("${minio.endpoint}")
    String minioUrl;

    private final CourseRepository courseRepository;

    private final ActivityRepository activityRepository;

    private final LessonRepository lessonRepository;
    private final QuizSubmissionRepository quizSubmissionRepository;

    private final MinioAdapter minioAdapter;
    private final MessageUtil messageUtil;
    private final PasswordEncoder encoder;


    public ActivityServiceImpl(CourseRepository courseRepository, ActivityRepository activityRepository, LessonRepository lessonRepository, QuizSubmissionRepository quizSubmissionRepository, MinioAdapter minioAdapter, MessageUtil messageUtil, PasswordEncoder encoder) {
        this.courseRepository = courseRepository;
        this.activityRepository = activityRepository;
        this.lessonRepository = lessonRepository;
        this.quizSubmissionRepository = quizSubmissionRepository;
        this.minioAdapter = minioAdapter;
        this.messageUtil = messageUtil;
        this.encoder = encoder;
    }


    @Override
    public Boolean addActivity(ActivityRequest activityRequest) throws IOException {
        User currentUser = SecurityUtil.getCurrentUser();
        Course course = courseRepository.findById(activityRequest.getCourseId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.COURSE_NOT_FOUND_BY_ID) + activityRequest.getCourseId()));
        User mentor = course.getCreator();
        if (!SecurityUtil.isHasAnyRole(currentUser, EUserRole.MANAGER) && !Objects.equals(currentUser.getId(), mentor.getId())) {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.FORBIDDEN));
        }
        ECourseActivityType type = activityRequest.getType();
        ActivityBuilder activityBuilder = ActivityBuilder.getBuilder()
                .withName(activityRequest.getName())
                .withVisible(activityRequest.getVisible())
                .withCourse(course)
                .withType(type);
        if (Objects.equals(activityRequest.getType(), ECourseActivityType.SECTION)) {
            Activity parentActivity = activityRepository.findByIdAndType(activityRequest.getParentActivityId(), ECourseActivityType.SECTION).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                    .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.SECTION_NOT_FOUND_BY_ID) + activityRequest.getParentActivityId()));
            activityBuilder.withParent(parentActivity);
        }
        Activity activity = activityBuilder.build();
        activity = activityRepository.save(activity);
        return createDetailActivity(activityRequest, type, activity);
    }

    @Override
    public List<Long> mentorCreateSectionForCourse(Long id, List<MentorCreateSectionForCourse> sessions) {
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

    private List<Long> mentorCreateSectionForCourse(List<MentorCreateSectionForCourse> sessions, Course course) {
        List<Activity> activityList = new ArrayList<>();
        List<Lesson> lessonList = new ArrayList<>();
        sessions.forEach(createSectionForCourse -> {
            Activity activitySection = new Activity();
            activitySection.setName(createSectionForCourse.getName());
            activitySection.setType(ECourseActivityType.SECTION);
            activitySection.setCourse(course);


            List<MentorCreateLessonForCourse> lessons = createSectionForCourse.getLessons();
            lessons.forEach(mentorCreateLessonForCourse -> {
                Activity activityLesson = new Activity();
                activityLesson.setParent(activitySection);
                activityLesson.setType(ECourseActivityType.LESSON);
                activityLesson.setCourse(course);
                Lesson lesson = new Lesson();
                lesson.setDescription(mentorCreateLessonForCourse.getDescription());
                lesson.setActivity(activityLesson);
                lessonList.add(lesson) ;
                activityList.add(activityLesson);
            });
            activityList.add(activitySection);

        });
        lessonRepository.saveAll(lessonList) ;
        List<Activity> activityListSaved = activityRepository.saveAll(activityList);
        return activityListSaved.stream().map(Activity::getId).collect(Collectors.toList());
    }

    private boolean createDetailActivity(ActivityRequest activityRequest, ECourseActivityType type, Activity activity) throws IOException {
        switch (type) {
            case QUIZ:
                Quiz quiz = addQuiz((AddQuizRequest) activityRequest, activity);
                activity.setQuiz(quiz);
                activityRepository.save(activity);
                break;
            case ASSIGNMENT:
                Assignment assignment = addAssignment((AssignmentRequest) activityRequest, activity);
                activity.setAssignment(assignment);
                activityRepository.save(activity);
                return true;
            case SECTION:
                // Just return for section -> section work as folder for others activities with no content inside
                return true;
            case RESOURCE:
                return true;
            case ANNOUNCEMENT:
                return true;
            case LESSON:
                return true;
            default:
                throw ApiException.create(HttpStatus.NO_CONTENT).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Invalid.INVALID_ACTIVITY_TYPE) + type);
        }
        return false;
    }


    public Quiz addQuiz(AddQuizRequest addQuizRequest, Activity activity) {
        if (addQuizRequest.getCode().trim().isEmpty()) {

        }

        if (addQuizRequest.getStartDate().isBefore(Instant.now()) || addQuizRequest.getEndDate().isBefore(Instant.now())) {

        }
        if (addQuizRequest.getStartDate().isAfter(addQuizRequest.getEndDate())) {

        }

        if (addQuizRequest.getTime() < 0) {

        }
        if (addQuizRequest.getDefaultPoint() < 0) {

        }
        if (addQuizRequest.getIsAllowReview() && addQuizRequest.getAllowReviewAfterMin() < 0) {

        }
        if (addQuizRequest.getPassword().trim().isEmpty()) {

        }

        List<QuizQuestionRequest> questions = addQuizRequest.getQuizQuestions();
        if (questions.size() < QuizUtil.MIN_QUESTIONS_PER_QUIZ || questions.size() > QuizUtil.MAX_QUESTIONS_PER_QUIZ) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("");
        }

        List<QuizQuestion> quizQuestions = new ArrayList<>();
        for (QuizQuestionRequest question : questions) {
            if (question.getQuestion().trim().isEmpty()) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
            }
            List<QuizAnswerRequest> answers = question.getAnswers();
            if (answers.size() < QuizUtil.MIN_ANSWERS_PER_QUESTION || answers.size() > QuizUtil.MAX_ANSWERS_PER_QUESTION) {

            }
            boolean isContainEmptyAnswer = answers.stream().anyMatch(x -> x.getAnswer().trim().isEmpty());
            if (isContainEmptyAnswer) {

            }
            long numOfRightAnswer = answers.stream().filter(x -> x.getRight()).count();
            switch (question.getQuestionType()) {
                case SINGLE:
                    if (numOfRightAnswer > 1) {
                        throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage("") + numOfRightAnswer);
                    }
                    break;
                case MULTIPLE:
                    if (numOfRightAnswer < 2 || numOfRightAnswer == answers.size()) {
                        throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage("") + numOfRightAnswer);
                    }
                    break;
            }
            List<QuizAnswer> quizAnswers = new ArrayList<>();
            for (QuizAnswerRequest answer : answers) {
                QuizAnswer quizAnswer = new QuizAnswer();
                quizAnswer.setAnswer(answer.getAnswer());
                quizAnswer.setIsRight(answer.getRight());
                quizAnswers.add(quizAnswer);
            }
            QuizQuestion quizQuestion = new QuizQuestion();
            quizQuestion.setQuestion(question.getQuestion());
            quizQuestion.setType(question.getQuestionType());
            quizQuestion.setAnswers(quizAnswers);
            quizQuestions.add(quizQuestion);
        }
        Quiz quiz = new Quiz();
        quiz.setCode(addQuizRequest.getCode());
        quiz.setStartDate(addQuizRequest.getStartDate());
        quiz.setEndDate(addQuizRequest.getEndDate());
        quiz.setTime(addQuizRequest.getTime());
        quiz.setStatus(QuizStatus.PENDING);
        quiz.setDefaultPoint(addQuizRequest.getDefaultPoint());
        quiz.setIsSuffleQuestion(addQuizRequest.getSuffleQuestion());
        quiz.setIsAllowReview(addQuizRequest.getIsAllowReview());
        quiz.setAllowReviewAfterMin(addQuizRequest.getAllowReviewAfterMin());
        quiz.setPassword(encoder.encode(addQuizRequest.getPassword()));
        quiz.setActivity(activity);
        quiz.setQuizQuestions(quizQuestions);
        return quiz;
    }

    private Assignment addAssignment(AssignmentRequest request, Activity activity) throws IOException {
        Instant now = Instant.now();
        Instant startDate = request.getStartDate();
        Instant endDate = request.getEndDate();

        if (startDate.isBefore(now) || endDate.isBefore(now) || startDate.isAfter(endDate)) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Invalid.INVALID_ASSIGNMENT_DATE));
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
        assignmentFile.setUser(SecurityUtil.getCurrentUser());
        assignmentFile.setAssignment(assignment);
        return assignmentFile;
    }

//    @Override
//    public Boolean deleteActivity(Long id) {
//        Activity activity = activityRepository.findById(id)
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.ACTIVITY_NOT_FOUND_BY_ID) + id));
//        User subCourseMentor = activity.getClassSection().getClazz().getSubCourse().getMentor();
//        User currentUser = SecurityUtil.getCurrentUser();
//        if (!Objects.equals(subCourseMentor.getId(), currentUser.getId()) || !SecurityUtil.isHasAnyRole(currentUser, EUserRole.MANAGER, EUserRole.ADMIN)) {
//            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.FORBIDDEN));
//        }
//        activityRepository.delete(activity);
//        return true;
//    }

//    @Override
//    public Boolean changeActivityVisible(Long id) {
//        Activity activity = activityRepository.findById(id)
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.ACTIVITY_NOT_FOUND_BY_ID) + id));
//        User subCourseMentor = activity.getClassSection().getClazz().getSubCourse().getMentor();
//        User currentUser = SecurityUtil.getCurrentUser();
//        if (!Objects.equals(subCourseMentor.getId(), currentUser.getId()) || !SecurityUtil.isHasAnyRole(currentUser, EUserRole.MANAGER, EUserRole.ADMIN)) {
//            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.FORBIDDEN));
//        }
//        activity.setVisible(!activity.getVisible());
//        return true;
//    }

//    @Override
//    public Boolean editActivity(Long id, ActivityRequest activityRequest) throws IOException {
//        User currentUser = SecurityUtil.getCurrentUser();
//        ClassSection classSection = classSectionRepository.findById(activityRequest.getClassSectionId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
//                .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.SECTION_NOT_FOUND_BY_ID) + activityRequest.getClassSectionId()));
//        Class clazz = classSection.getClazz();
//        User mentor = clazz.getSubCourse().getMentor();
//        if (!SecurityUtil.isHasAnyRole(currentUser, EUserRole.MANAGER) && !Objects.equals(currentUser.getId(), mentor.getId())) {
//            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.FORBIDDEN));
//        }
//        ActivityType activityType = activityTypeRepository.findById(activityRequest.getActivityTypeId())
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
//                        .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.ACTIVITY_TYPE_NOT_FOUND_BY_ID) + activityRequest.getActivityTypeId()));
//
//        Activity activity = activityRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.ACTIVITY_NOT_FOUND_BY_ID) + id));
//        activity.setName(activityRequest.getName());
//        activity.setVisible(activityRequest.getIsVisible());
//        activity.setClassSection(classSection);
//        String code = activityType.getCode();
//        switch (code) {
//            case "QUIZ":
//                break; // Xử lý tương tự cho quiz activity ở đây
//            case "ASSIGNMENT":
//                Assignment assignment = editAssignment((AssignmentRequest) activityRequest, activity);
//                activity.setAssignment(assignment);
//                activityRepository.save(activity);
//                return true;
//            default:
//                throw ApiException.create(HttpStatus.NO_CONTENT).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Invalid.INVALID_ACTIVITY_TYPE));
//        }
//        return false;
//    }

    private Assignment editAssignment(AssignmentRequest request, Activity activity) throws IOException {
        Instant now = Instant.now();
        Instant startDate = request.getStartDate();
        Instant endDate = request.getEndDate();

        if (startDate.isBefore(now) || endDate.isBefore(now) || startDate.isAfter(endDate)) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Invalid.INVALID_DAY));
        }
        Assignment assignment = activity.getAssignment();
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
        Map<String, AssignmentFile> assignmentMapByName = existedAssignmentFiles.stream().collect(Collectors.toMap(AssignmentFile::getName, Function.identity()));
        for (MultipartFile attachFile : attachFiles) {
            AssignmentFile newAssignmentFile = createAssignmentFile(attachFile, assignment, FileType.ATTACH);
            AssignmentFile existedAssignment = assignmentMapByName.get(newAssignmentFile.getName());
            if (existedAssignment != null) {
                if (request.getIsOverWriteAttachFile()) {
                    existedAssignmentFiles.remove(existedAssignment);
                    existedAssignmentFiles.add(newAssignmentFile);
                } else {
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(Constants.ErrorMessage.CAN_NOT_UPLOAD_ASSIGNMENT);
                }
            } else {
                existedAssignmentFiles.add(newAssignmentFile);
            }
        }
        return assignment;
    }

//    @Override
//    public ActivityDto getDetailActivity(Long id) {
//        Activity activity = activityRepository.findById(id)
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.ACTIVITY_NOT_FOUND_BY_ID) + id));
//        Class clazz = activity.getClassSection().getClazz();
//        User currentUser = SecurityUtil.getCurrentUser();
//
//        if (SecurityUtil.isHasAnyRole(currentUser, EUserRole.MANAGER, EUserRole.ADMIN)) {
//            return ConvertUtil.convertActivityToDto(activity);
//        }
//
//        ActivityDto activityDto = ConvertUtil.convertActivityToDto(activity);
//        if (SecurityUtil.isHasAnyRole(currentUser, EUserRole.TEACHER) && Objects.equals(clazz.getSubCourse().getMentor().getId(), currentUser.getId())) {
//            return activityDto;
//        }
//
//        boolean isStudentOfClass = clazz.getStudentClasses().stream().anyMatch(studentClass -> Objects.equals(studentClass.getStudent().getId(), currentUser.getId()));
//        if (SecurityUtil.isHasAnyRole(currentUser, EUserRole.STUDENT) && isStudentOfClass) {
//            return activityDto;
//        } else {
//            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.FORBIDDEN));
//        }
//    }

//    private Quiz findQuizById(Long id) {
//        return quizRepository.findById(id)
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("")));
//    }
//
//    private User validateUser(Long classSectionId) {
//        User user = SecurityUtil.getCurrentUser();
//        boolean isStudent = SecurityUtil.isHasAnyRole(user, EUserRole.STUDENT);
//        if (!isStudent) {
//            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.FORBIDDEN));
//        }
//        ClassSection classSection = classSectionRepository.findById(classSectionId)
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.SECTION_NOT_FOUND_BY_ID) + classSectionId));
//        List<StudentClass> studentClass = classSection.getClazz().getStudentClasses();
//        boolean isStudentBelongToClass = studentClass.stream().anyMatch(x -> user.equals(x.getStudent()));
//        if (!isStudentBelongToClass) {
//            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.FORBIDDEN));
//        }
//        return user;
//    }

    private void isAvailableToAttempt(Quiz quiz, User user) {
        Instant currentTime = Instant.now();
        if (currentTime.isBefore(quiz.getStartDate()) || currentTime.isAfter(quiz.getEndDate())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }
        if (!quiz.getStatus().equals(QuizStatus.OPENING)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }
        if (!quiz.getIsUnlimitedAttempt()) {
            int submitTimes = quiz.getQuizSubmittions().stream()
                    .filter(x -> x.getSubmittedBy().equals(user))
                    .collect(Collectors.toList()).size();
            boolean isAvailableToAttempt = submitTimes < quiz.getAttemptNumber();
            if (!isAvailableToAttempt) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
            }
        }
    }

//    public QuizDto studentAttemptQuiz(StudentAttemptQuizRequest request) {
//        User user = validateUser(request.getClassSectionId());
//        Quiz quiz = findQuizById(request.getQuizId());
//        isAvailableToAttempt(quiz, user);
//
//        if (request.getPassword().trim().isEmpty()) {
//            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
//        }
//        boolean isMatchPassword = encoder.matches(request.getPassword(), quiz.getPassword());
//        if (!isMatchPassword) {
//            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
//        }
//        QuizDto quizDto = ConvertUtil.convertQuizToQuizDto(quiz);
//        quizDto.setDefaultPoint(null);
//        quizDto.setSuffleQuestion(null);
//        quizDto.setPassword(null);
//        List<QuizQuestionDto> questions = quizDto.getQuizQuestions();
//        for (QuizQuestionDto quizQuestionDto : questions) {
//            quizQuestionDto.getAnswers().stream().forEach(x -> x.setRight(false));
//        }
//        if (quiz.getIsSuffleQuestion()) {
//            Collections.shuffle(questions);
//        }
//        quizDto.setQuizQuestions(questions);
//        return quizDto;
//    }

    public QuizSubmittionDto studentReviewQuiz(Long id) {
        User user = SecurityUtil.getCurrentUser();
        QuizSubmittion quizSubmittion = quizSubmissionRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("")));
        boolean isProposer = quizSubmittion.getSubmittedBy().equals(user);
        if (!isProposer) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }
        if (!quizSubmittion.getQuiz().getIsAllowReview()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }
        long reviewAfter = quizSubmittion.getQuiz().getAllowReviewAfterMin();
        Instant endTime = quizSubmittion.getCreated().plus(reviewAfter, ChronoUnit.MINUTES);
        Instant now = Instant.now();
        if (endTime.isAfter(now)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
        }
        return null;
    }


//    public boolean studentSubmitQuiz(SubmitQuizRequest request) {
//        User user = validateUser(request.getClassSectionId());
//        Quiz quiz = findQuizById(request.getQuizId());
//        isAvailableToAttempt(quiz, user);
//        List<SubmittedQuestionRequest> submittedQuestions = request.getSubmittedQuestions();
//        List<QuizQuestion> quizQuestions = quiz.getQuizQuestions();
//        QuizSubmittion quizSubmittion = new QuizSubmittion();
//        List<QuizSubmitQuestion> submitQuestions = new ArrayList<>();
//        for (QuizQuestion quizQuestion : quizQuestions) {
//            SubmittedQuestionRequest submittedQuestion = submittedQuestions.stream()
//                    .filter(x -> x.getQuestionId().equals(quizQuestion.getId())).findFirst()
//                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("")));
//            QuizSubmitQuestion submitQuestion = new QuizSubmitQuestion();
//            submitQuestion.setQuizSubmittion(quizSubmittion);
//            submitQuestion.setQuizQuestion(quizQuestion);
//
//            List<QuizSubmitAnswer> quizSubmitAnswers;
//            List<QuizAnswer> quizAnswers = quizQuestion.getAnswers();
//            List<Long> submittedAnswers = submittedQuestion.getAnswerId();
//            switch (quizQuestion.getType()) {
//                case SINGLE:
//                    quizSubmitAnswers = handleSingleChoice(submittedAnswers, quizAnswers, submitQuestion);
//                    break;
//                case MULTIPLE:
//                    quizSubmitAnswers = handleMultipleChoice(submittedAnswers, quizAnswers, submitQuestion);
//                    break;
//                default:
//                    throw ApiException.create(HttpStatus.NO_CONTENT).withMessage(messageUtil.getLocalMessage(""));
//            }
//            submitQuestion.setQuizSubmitAnswers(quizSubmitAnswers);
//            submitQuestions.add(submitQuestion);
//        }
//        int correctNumber = getCorrectNumberFromSubmission(submitQuestions);
//        quizSubmittion.setQuiz(quiz);
//        quizSubmittion.setStatus(request.getStatus());
//        quizSubmittion.setSubmittedBy(user);
//        quizSubmittion.setSubmitQuestions(submitQuestions);
//        quizSubmittion.setCorrectNumber(correctNumber);
//        quizSubmittion.setIncorrectNumber(quiz.getQuizQuestions().size() - correctNumber);
//        quizSubmissionRepository.save(quizSubmittion);
//        return true;
//    }

    private int getCorrectNumberFromSubmission(List<QuizSubmitQuestion> quizSubmitQuestions) {
        int correctNumber = 0;
        for (QuizSubmitQuestion quizSubmitQuestion : quizSubmitQuestions) {
            List<QuizSubmitAnswer> quizSubmitAnswers = quizSubmitQuestion.getQuizSubmitAnswers();
            if (!quizSubmitAnswers.isEmpty()) {
                if (quizSubmitQuestion.getQuizQuestion().getType().equals(QuestionType.SINGLE)) {
                    if (quizSubmitAnswers.get(0).getQuizAnswer().getIsRight()) {
                        correctNumber++;
                    }
                } else {
                    List<QuizAnswer> quizAnswers = quizSubmitQuestion.getQuizQuestion().getAnswers();
                    long submittedCorrectAnswerCount = quizSubmitAnswers.stream()
                            .filter(x -> x.getQuizAnswer().getIsRight())
                            .count();
                    long quizCorrectAnswerCount = quizAnswers.stream()
                            .filter(x -> x.getIsRight())
                            .count();
                    if (submittedCorrectAnswerCount == quizCorrectAnswerCount) {
                        correctNumber++;
                    }
                }
            }
        }
        return correctNumber;
    }

    private List<QuizSubmitAnswer> handleSingleChoice(List<Long> submittedAnswers, List<QuizAnswer> quizAnswers, QuizSubmitQuestion quizSubmitQuestion) {
        List<QuizSubmitAnswer> quizSubmitAnswers = new ArrayList<>();
        if (!submittedAnswers.isEmpty()) {
            boolean isOnlyOneAnswer = submittedAnswers.size() == 1;
            Long submittedAnswerId = submittedAnswers.get(0);
            if (!isOnlyOneAnswer) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(""));
            }
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
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("")));
        quizSubmitAnswer.setQuizAnswer(quizAnswer);
        quizSubmitAnswer.setQuizSubmitQuestion(quizSubmitQuestion);
        return quizSubmitAnswer;
    }


//    @Override
//    public Boolean submitAssignment(Long id, SubmitAssignmentRequest request) {
//        User currentUser = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
//        Activity activity = activityRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy Assignment với id:" + id));
//        Class clazz = activity.getClassSection().getClazz();
//        EUserRole userRole = ClassValidator.isMemberOfClassAsRole(clazz, currentUser);
//        if (userRole == null){
//            throw ApiException.create(HttpStatus.CONFLICT).withMessage("Người dùng không phải thành viên của lớp này");
//        } else if (userRole != EUserRole.STUDENT) {
//            throw ApiException.create(HttpStatus.CONFLICT).withMessage("Người dùng không phải học sinh của lớp này");
//        }
//        Assignment assignment = activity.getAssignment();
//        if (assignment == null){
//            throw ApiException.create(HttpStatus.CONFLICT).withMessage("Không tìm thấy Assignment của Activity này");
//        }

//        createAssignmentFile(request.getSubmittedFiles(), assignment, FileType.SUBMIT)

//        return null;
//    }
}
