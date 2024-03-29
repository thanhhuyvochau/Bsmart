package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ValidationErrors;
import fpt.project.bsmart.entity.common.ValidationErrorsException;
import fpt.project.bsmart.entity.constant.*;
import fpt.project.bsmart.entity.dto.ActivityDto;
import fpt.project.bsmart.entity.dto.ImageDto;
import fpt.project.bsmart.entity.dto.feedback.FeedbackTemplateDto;
import fpt.project.bsmart.entity.request.ClassFilterRequest;
import fpt.project.bsmart.entity.request.CreateClassInformationRequest;
import fpt.project.bsmart.entity.request.MentorCreateClassRequest;
import fpt.project.bsmart.entity.request.TimeInWeekRequest;
import fpt.project.bsmart.entity.request.clazz.GetPointStudentClassRequest;
import fpt.project.bsmart.entity.request.clazz.MentorCreateClass;
import fpt.project.bsmart.entity.request.timetable.MentorCreateScheduleRequest;
import fpt.project.bsmart.entity.response.Class.BaseClassResponse;
import fpt.project.bsmart.entity.response.Class.ManagerGetClassDetailResponse;
import fpt.project.bsmart.entity.response.Class.ManagerGetCourseClassResponse;
import fpt.project.bsmart.entity.response.Class.MentorGetClassDetailResponse;
import fpt.project.bsmart.entity.response.*;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.IClassService;
import fpt.project.bsmart.util.*;
import fpt.project.bsmart.util.specification.ClassSpecificationBuilder;
import fpt.project.bsmart.util.specification.FeedbackSubmissionSpecificationBuilder;
import fpt.project.bsmart.validator.ClassValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static fpt.project.bsmart.entity.constant.ECourseClassStatus.*;
import static fpt.project.bsmart.util.Constants.ErrorMessage.*;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Invalid.INVALID_FEEDBACK_TYPE;


@Service
@Transactional
public class ClassServiceImpl implements IClassService {

    private final MessageUtil messageUtil;
    private final CategoryRepository categoryRepository;

    private final ClassRepository classRepository;

    private final DayOfWeekRepository dayOfWeekRepository;
    private final SlotRepository slotRepository;

    private final TimeInWeekRepository timeInWeekRepository;
    private final CourseRepository courseRepository;

    private final ClassImageRepository classImageRepository;
    private final ActivityAuthorizeRepository activityAuthorizeRepository;
    private final FeedbackTemplateRepository feedbackTemplateRepository;
    private final FeedbackSubmissionRepository feedbackSubmissionRepository;
    private final TimeTableRepository timeTableRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    private final StudentClassRepository studentClassRepository;

    private final AssignmentSubmittionRepository assignmentSubmittionRepository;

    private final QuizSubmissionRepository quizSubmissionRepository;
    private final ClassUtil classUtil;


    public ClassServiceImpl(MessageUtil messageUtil, CategoryRepository categoryRepository, ClassRepository classRepository, DayOfWeekRepository dayOfWeekRepository, SlotRepository slotRepository, TimeInWeekRepository timeInWeekRepository, CourseRepository courseRepository, ClassImageRepository classImageRepository, ActivityAuthorizeRepository activityAuthorizeRepository, FeedbackTemplateRepository feedbackTemplateRepository, FeedbackSubmissionRepository feedbackSubmissionRepository, TimeTableRepository timeTableRepository, SubjectRepository subjectRepository, UserRepository userRepository, StudentClassRepository studentClassRepository, AssignmentSubmittionRepository assignmentSubmittionRepository, QuizSubmissionRepository quizSubmissionRepository, ClassUtil classUtil) {
        this.messageUtil = messageUtil;
        this.categoryRepository = categoryRepository;
        this.classRepository = classRepository;
        this.dayOfWeekRepository = dayOfWeekRepository;
        this.slotRepository = slotRepository;
        this.timeInWeekRepository = timeInWeekRepository;
        this.courseRepository = courseRepository;
        this.classImageRepository = classImageRepository;
        this.activityAuthorizeRepository = activityAuthorizeRepository;
        this.feedbackTemplateRepository = feedbackTemplateRepository;
        this.feedbackSubmissionRepository = feedbackSubmissionRepository;
        this.timeTableRepository = timeTableRepository;
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
        this.studentClassRepository = studentClassRepository;
        this.assignmentSubmittionRepository = assignmentSubmittionRepository;
        this.quizSubmissionRepository = quizSubmissionRepository;
        this.classUtil = classUtil;
    }

    /**
     * mentor create class for course (private)
     * Step 1 : create course information
     * - load subject from skill of mentor to set value for course
     * Step 2 : create class information and time in week
     *
     * @return List<Long> list id of class created
     */
    @Override
    public List<String> mentorCreateCoursePrivate(MentorCreateClassRequest mentorCreateClassRequest) {
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();
        // Step 1
        Course course = createCourseFromRequest(currentUserAccountLogin, mentorCreateClassRequest);
        // Step 2
        return createClassAndTimeInWeek(currentUserAccountLogin, course, mentorCreateClassRequest);
    }

    @Override
    public MentorGetCourseClassResponse getAllClassOfCourse(Long id) {
        User currentUserAccountLogin = SecurityUtil.hasCurrentUser();
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));
        MentorGetCourseClassResponse response = CourseUtil.convertCourseToCourseClassResponsePage(course);

        List<Activity> sectionActivities = course.getActivities().stream()
                .filter(activity -> Objects.equals(activity.getType(), ECourseActivityType.SECTION) && activity.getFixed())
                .collect(Collectors.toList());

        ResponseUtil.responseForRole(EUserRole.TEACHER);
        List<ActivityDto> activityDtos = ConvertUtil.convertActivityAsTree(sectionActivities, true);
        response.setActivities(activityDtos);
        List<Class> classList = classRepository.findByCourseAndStatus(course, ECourseClassStatus.NOTSTART);

        List<ClassDetailResponse> classDetailResponses = new ArrayList<>();
        for (Class aClass : classList) {
            ClassDetailResponse classDetailResponse;
            if (currentUserAccountLogin != null) {
                classDetailResponse = ClassUtil.convertClassToClassDetailResponse(currentUserAccountLogin, aClass);
            } else {
                classDetailResponse = ClassUtil.convertClassToClassDetailResponseNoLogin(aClass);
            }

            classDetailResponses.add(classDetailResponse);

        }

        response.setClasses(classDetailResponses);
        FeedbackSubmissionSpecificationBuilder builder = FeedbackSubmissionSpecificationBuilder.feedbackSubmissionSpecificationBuilder()
                .filterByCourse(course.getId());
        List<FeedbackSubmission> feedbackSubmissions = feedbackSubmissionRepository.findAll(builder.build());
        List<Integer> rates = feedbackSubmissions.stream().map(FeedbackSubmission::getCourseRate).collect(Collectors.toList());
        Map<Integer, Long> rateCount = FeedbackUtil.getRateCount(rates);
        response.setAverageRate(FeedbackUtil.calculateAverageRate(rateCount));
        response.setRateCount(rateCount);
        response.setSubmissionCount(feedbackSubmissions.size());
        return response;

    }

    @Override
    public ApiPage<MentorGetClassDetailResponse> mentorGetClassOfCourse(Long id, Pageable pageable) {
        User currentUser = SecurityUtil.getCurrentUser();
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));

        if (!course.getCreator().equals(currentUser)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(YOU_DO_NOT_HAVE_PERMISSION_TO_VIEW_CLASS_FOR_THIS_COURSE));
        }
        Page<Class> classPage = classRepository.findByCourse(course, pageable);
        List<MentorGetClassDetailResponse> classResponses = classPage.getContent().stream()
                .map(ClassUtil::convertClassToMentorClassDetailResponse)
                .collect(Collectors.toList());
        return PageUtil.convert(new PageImpl<>(classResponses, pageable, classPage.getTotalElements()));

    }


    @Override
    public Long mentorCreateClassForCourse(Long id, MentorCreateClass mentorCreateClassRequest) throws Exception {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));

        User currentUserAccountLogin = SecurityUtil.getCurrentUser();

        User creator = course.getCreator();
        ClassUtil.checkMentorOfClass(creator, currentUserAccountLogin);


        Class classAndTimeInWeek = createClassAndTimeInWeek(currentUserAccountLogin, course, mentorCreateClassRequest);
//        mentorCreateScheduleForClass(classAndTimeInWeek, mentorCreateClassRequest.getTimeTableRequest());
        return classAndTimeInWeek.getId();
    }

    public Boolean mentorCreateScheduleForClass(Class clazz, List<MentorCreateScheduleRequest> request) throws ValidationErrorsException {

        List<Slot> allSlot = slotRepository.findAll();
        Map<Long, Slot> slotMap = new HashMap<>();
        for (Slot slot : allSlot) {
            slotMap.put(slot.getId(), slot);
        }

        List<TimeTable> timeTables = new ArrayList<>();
        ArrayList<MentorCreateScheduleRequest> duplicateDate = new ArrayList<>();
        ValidationErrors<MentorCreateScheduleRequest> vaErr = new ValidationErrors<>();
        for (MentorCreateScheduleRequest mentorCreateScheduleRequest : request) {

            Long checkDuplicate = timeTableRepository.countByClassIdAndDateAndSlotId(clazz.getId(), mentorCreateScheduleRequest.getDate(), mentorCreateScheduleRequest.getSlot().getId());
            if (checkDuplicate > 0) {
                ValidationErrors.ValidationError<MentorCreateScheduleRequest> validationError = new ValidationErrors.ValidationError<>();
                validationError.setMessage("Trùng ngày dạy ");

                duplicateDate.add(mentorCreateScheduleRequest);
                validationError.setInvalidParams(duplicateDate);
                vaErr.setError(validationError);

            }
            // Check for duplicate entries
            TimeTable timeTable = new TimeTable();
            timeTable.setDate(mentorCreateScheduleRequest.getDate());
            timeTable.setCurrentSlotNum(mentorCreateScheduleRequest.getNumberOfSlot());
            Slot slot = slotMap.get(mentorCreateScheduleRequest.getSlot().getId());
            timeTable.setSlot(slot);
            timeTable.setClazz(clazz);
            timeTables.add(timeTable);

        }
        if (!duplicateDate.isEmpty()) {
            throw new ValidationErrorsException(vaErr.getError().getInvalidParams(), vaErr.getError().getMessage());
        }
        timeTableRepository.saveAll(timeTables);
        return true;
    }

    @Override
    public Boolean mentorUpdateClassForCourse(Long id, MentorCreateClass mentorCreateClassRequest) {
        Class aClass = classRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                .withMessage(messageUtil.getLocalMessage(CLASS_NOT_FOUND_BY_ID) + id));
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();

        Course course = aClass.getCourse();
        List<TimeInWeekRequest> timeInWeekRequests = mentorCreateClassRequest.getTimeInWeekRequests();

        // create time in week for subCourse
        aClass.getTimeInWeeks().clear();
        List<TimeInWeek> timeInWeeksFromRequest = updateTimeInWeeksFromRequest(timeInWeekRequests);

        // create subCourse for course
        updateClassFromRequest(aClass, mentorCreateClassRequest, course, currentUserAccountLogin, timeInWeeksFromRequest);
        return true;
    }

    @Override
    public Boolean mentorDeleteClassForCourse(Long id) {
        Class aClass = classRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                .withMessage(messageUtil.getLocalMessage(CLASS_NOT_FOUND_BY_ID) + id));
        User mentor = aClass.getMentor();
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();
        ClassUtil.checkMentorOfClass(mentor, currentUserAccountLogin);
        aClass.setCourse(null);
        classRepository.delete(aClass);

        return true;
    }

    @Override
    public ManagerGetCourseClassResponse getAllClassOfCourseForManager(Long id, ECourseClassStatus status) {
//        Course course = courseRepository.findByIdAndStatus(id, WAITING)
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
//                        .withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));

        ManagerGetCourseClassResponse response = CourseUtil.convertCourseToCourseClassResponseManager(course, status);
        List<Activity> sectionActivities = course.getActivities().stream()
                .filter(activity -> Objects.equals(activity.getType(), ECourseActivityType.SECTION))
                .collect(Collectors.toList());
        ResponseUtil.responseForRole(EUserRole.MANAGER);
        List<ActivityDto> activityDtoList = ConvertUtil.convertActivityAsTree(sectionActivities, false);
        response.setActivities(activityDtoList);
        return response;
    }

    public ApiPage<BaseClassResponse> getAllClassesForManager(Pageable pageable) {
        ClassSpecificationBuilder builder = ClassSpecificationBuilder.classSpecificationBuilder()
                .getPendingClass()
                .getStartingClass();
        Page<Class> classes = classRepository.findAll(builder.build(), pageable);
        List<BaseClassResponse> classDetailResponses = classes.getContent().stream()
                .map(ClassUtil::convertClassToBaseclassResponse)
                .collect(Collectors.toList());
        return PageUtil.convert(new PageImpl<>(classDetailResponses, pageable, classes.getTotalElements()));
    }

    public ManagerGetClassDetailResponse managerGetClassDetail(Long classId) {
        Class clazz = classRepository.findById(classId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CLASS_NOT_FOUND_BY_ID) + classId));
        return ClassUtil.convertClassToManagerGetClassResponse(clazz);
    }

    private Class updateClassFromRequest(Class aClass, MentorCreateClass classRequest, Course course, User currentUserAccountLogin, List<TimeInWeek> timeInWeeks) {
        if (classRequest.getPrice() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(PLEASE_ENTER_THE_PRICE_FOR_THE_COURSE));
        }

        aClass.setNumberOfSlot(classRequest.getNumberOfSlot());
        aClass.setMinStudent(classRequest.getMinStudent());
        aClass.setMaxStudent(classRequest.getMaxStudent());
        aClass.setStartDate(classRequest.getStartDate());
        aClass.setEndDate(classRequest.getEndDate());
        aClass.setStatus(REQUESTING);
        aClass.setPrice(classRequest.getPrice());
        aClass.setMentor(currentUserAccountLogin);
        String codeRandom = ClassUtil.generateCode(course.getSubject().getCode());
        aClass.setCode(codeRandom);


        Long imageId = classRequest.getImageId();
        if (imageId != null) {

            ClassImage classImage = classImageRepository.findById(imageId)
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                            .withMessage(messageUtil.getLocalMessage(IMAGE_NOT_FOUND_BY_ID) + imageId));
            classImage.setaClass(aClass);
            aClass.setClassImage(classImage);
        }
        aClass.getTimeInWeeks().clear();

        aClass.getTimeInWeeks().addAll(timeInWeeks);
        timeInWeeks.forEach(timeInWeek -> {
            timeInWeek.setClazz(aClass);
            timeInWeekRepository.save(timeInWeek);
        });

        classRepository.save(aClass);
        return aClass;
    }

    private List<TimeInWeek> updateTimeInWeeksFromRequest(List<TimeInWeekRequest> timeInWeekRequests) {


        TimeInWeekRequest duplicateElement = ObjectUtil.isHasDuplicate(timeInWeekRequests);
        if (duplicateElement != null) {
            throw ApiException.create(HttpStatus.NOT_FOUND)
                    .withMessage(SCHEDULE_AND_SLOT_HAVE_BEEN_OVERLAPPED);
        }

        List<Long> slotIds = timeInWeekRequests.stream().map(TimeInWeekRequest::getSlotId).collect(Collectors.toList());
        List<Long> dowIds = timeInWeekRequests.stream().map(TimeInWeekRequest::getDayOfWeekId).collect(Collectors.toList());

        Map<Long, Slot> slotMap = slotRepository.findAllById(slotIds).stream()
                .collect(Collectors.toMap(Slot::getId, Function.identity()));
        Map<Long, DayOfWeek> dayOfWeekMap = dayOfWeekRepository.findAllById(dowIds).stream()
                .collect(Collectors.toMap(DayOfWeek::getId, Function.identity()));

        List<TimeInWeek> timeInWeeks = new ArrayList<>();
        for (TimeInWeekRequest timeInWeekRequest : timeInWeekRequests) {
            TimeInWeek timeInWeek = new TimeInWeek();
            DayOfWeek dayOfWeek = Optional.ofNullable(dayOfWeekMap.get(timeInWeekRequest.getDayOfWeekId()))
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                            .withMessage(DAY_OF_WEEK_COULD_NOT_BE_FOUND));
            timeInWeek.setDayOfWeek(dayOfWeek);

            Slot slot = Optional.ofNullable(slotMap.get(timeInWeekRequest.getSlotId()))
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                            .withMessage(SLOT_COULD_NOT_BE_FOUND));
            timeInWeek.setSlot(slot);

            timeInWeeks.add(timeInWeek);
        }
        return timeInWeeks;
    }


    private Class createClassAndTimeInWeek(User currentUserAccountLogin, Course course, MentorCreateClass mentorCreateClassRequest) throws Exception {
        // check mentor account is valid
        MentorUtil.checkIsMentor();


        List<Class> classes = new ArrayList<>();

        List<TimeInWeekRequest> timeInWeekRequests = mentorCreateClassRequest.getTimeInWeekRequests();

        // create time in week for subCourse
        List<TimeInWeek> timeInWeeksFromRequest = createTimeInWeeksFromRequest(timeInWeekRequests);
        Class classFromRequest = null;
        /**Kiểm tra Time In Week, Ngày bắt đầu, Ngày kết thúc, Số lượng slot của class*/
        if (ClassValidator.isValidTimeOfClass(timeInWeeksFromRequest, mentorCreateClassRequest.getNumberOfSlot(), mentorCreateClassRequest.getStartDate(), mentorCreateClassRequest.getEndDate())) {
            // create subCourse for course
            classFromRequest = createClassFromRequest(mentorCreateClassRequest, course, currentUserAccountLogin, timeInWeeksFromRequest);
            classFromRequest.setCourse(course);
            classes.add(classFromRequest);
            classFromRequest.setCourse(course);
            classRepository.save(classFromRequest);

//        classes.forEach(aClass -> {
//                    classCodes.add(aClass.getCode());
//                    ActivityHistoryUtil.logHistoryForCourseCreated(currentUserAccountLogin.getId(), aClass);
//                }
//        );
        }
        return classFromRequest;

    }

    private Class createClassFromRequest(MentorCreateClass subCourseRequest, Course course, User currentUserAccountLogin, List<TimeInWeek> timeInWeeks) throws Exception {
        if (subCourseRequest.getPrice() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(PLEASE_ENTER_THE_PRICE_FOR_THE_COURSE));
        }
        Class aClass = new Class();
        aClass.setNumberOfSlot(subCourseRequest.getNumberOfSlot());
        aClass.setMinStudent(subCourseRequest.getMinStudent());
        aClass.setMaxStudent(subCourseRequest.getMaxStudent());

        aClass.setStartDate(TimeUtil.checkDateToCreateClass(subCourseRequest.getStartDate()));
        aClass.setEndDate(TimeUtil.checkDateToStartAndEndClass(subCourseRequest.getStartDate(), subCourseRequest.getEndDate()));
        aClass.setStatus(REQUESTING);
        aClass.setPrice(subCourseRequest.getPrice());
        aClass.setMentor(currentUserAccountLogin);
        String codeRandom = ClassUtil.generateCode(course.getSubject().getCode());
        aClass.setCode(codeRandom);
        String classURL = subCourseRequest.getLink();
        validateMeetingURL(classURL);
        aClass.setClassURL(classURL);
        Long imageId = subCourseRequest.getImageId();
        ClassImage classImage = classImageRepository.findById(imageId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(IMAGE_NOT_FOUND_BY_ID) + imageId));


        if (classImage.getaClass() != null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(IMAGE_NOT_FOUND_BY_ID));
        }
        classImage.setaClass(aClass);
        aClass.setTimeInWeeks(timeInWeeks);
        timeInWeeks.forEach(timeInWeek -> {
            timeInWeek.setClazz(aClass);
        });


        return aClass;
    }

    private Course createCourseFromRequest(User currentUserAccountLogin, MentorCreateClassRequest mentorCreateClassRequest) {
        Long categoryId = mentorCreateClassRequest.getCategoryId();
        if (categoryId == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(PLEASE_SELECT_THE_CATEGORY_FOR_THE_COURSE));
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(CATEGORY_NOT_FOUND_BY_ID) + categoryId));

        Long subjectId = mentorCreateClassRequest.getSubjectId();
        if (subjectId == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(PLEASE_SELECT_THE_SUBJECT_FOR_THE_COURSE));
        }
        Optional<Subject> optionalSubject = category.getSubjects().stream().filter(s -> s.getId().equals(subjectId)).findFirst();
        Subject subject = optionalSubject.orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                .withMessage(messageUtil.getLocalMessage(SUBJECT_NOT_FOUND_BY_ID) + subjectId));

        // check skill of mentor is match with subject input
        List<Subject> skillOfMentor = currentUserAccountLogin.getMentorProfile().getSkills().stream().map(MentorSkill::getSkill).collect(Collectors.toList());

        List<String> skillNames = skillOfMentor.stream().map(Subject::getName).collect(Collectors.toList());
        if (!skillOfMentor.contains(subject)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(YOU_ONLY_HAVE_PERMISSION_TO_CREATE_THIS_SUBJECT_MATCH_TO_YOUR_SKILL) + skillNames);
        }

        Course course = new Course();
        course.setName(mentorCreateClassRequest.getName());
        course.setCode(CourseUtil.generateRandomCode(subject.getCode()));
        course.setDescription(mentorCreateClassRequest.getDescription());
        course.setSubject(subject);
        course.setStatus(REQUESTING);
        course.setCreator(currentUserAccountLogin);

        return course;
    }


    private List<String> createClassAndTimeInWeek(User currentUserAccountLogin, Course course, MentorCreateClassRequest mentorCreateClassRequest) {
        // check mentor account is valid
        MentorUtil.checkIsMentor();


        List<String> classCodes = new ArrayList<>();
        List<CreateClassInformationRequest> createClassInformationRequests = mentorCreateClassRequest.getCreateClassRequest();
        List<Class> classes = new ArrayList<>();
        createClassInformationRequests.forEach(createClassInformationRequest -> {

            List<TimeInWeekRequest> timeInWeekRequests = createClassInformationRequest.getTimeInWeekRequests();

            // create time in week for subCourse
            List<TimeInWeek> timeInWeeksFromRequest = createTimeInWeeksFromRequest(timeInWeekRequests);

            // create subCourse for course
            Class classFromRequest = createClassFromRequest(createClassInformationRequest, course, currentUserAccountLogin, timeInWeeksFromRequest);
            classFromRequest.setCourse(course);

            classes.add(classFromRequest);

        });

        course.setClasses(classes);
        courseRepository.save(course);
        // ghi log
//        classes.forEach(aClass -> {
//                    classCodes.add(aClass.getCode());
//                    ActivityHistoryUtil.logHistoryForCourseCreated(currentUserAccountLogin.getId(), aClass);
//                }
//        );
        return classCodes;
    }


    private Class createClassFromRequest(CreateClassInformationRequest subCourseRequest, Course course, User currentUserAccountLogin, List<TimeInWeek> timeInWeeks) {
        if (subCourseRequest.getPrice() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(PLEASE_ENTER_THE_PRICE_FOR_THE_COURSE));
        }
        Class aClass = new Class();
        aClass.setNumberOfSlot(subCourseRequest.getNumberOfSlot());
        aClass.setMinStudent(subCourseRequest.getMinStudent());
        aClass.setMaxStudent(subCourseRequest.getMaxStudent());
        aClass.setStartDate(subCourseRequest.getStartDate());
        aClass.setEndDate(subCourseRequest.getEndDate());
        aClass.setStatus(REQUESTING);
        aClass.setPrice(subCourseRequest.getPrice());
        aClass.setMentor(currentUserAccountLogin);
        String codeRandom = ClassUtil.generateCode(course.getSubject().getCode());
        aClass.setCode(codeRandom);

        Long imageId = subCourseRequest.getImageId();
        ClassImage classImage = classImageRepository.findById(imageId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(IMAGE_NOT_FOUND_BY_ID) + imageId));
        classImage.setaClass(aClass);
        classImageRepository.save(classImage);

        aClass.setTimeInWeeks(timeInWeeks);
        timeInWeeks.forEach(timeInWeek -> {
            timeInWeek.setClazz(aClass);
            timeInWeekRepository.save(timeInWeek);
        });
        return aClass;
    }

    private List<TimeInWeek> createTimeInWeeksFromRequest(List<TimeInWeekRequest> timeInWeekRequests) {
        TimeInWeekRequest duplicateElement = ObjectUtil.isHasDuplicate(timeInWeekRequests);
        if (duplicateElement != null) {
            throw ApiException.create(HttpStatus.NOT_FOUND)
                    .withMessage(SCHEDULE_AND_SLOT_HAVE_BEEN_OVERLAPPED);
        }

        List<Long> slotIds = timeInWeekRequests.stream().map(TimeInWeekRequest::getSlotId).collect(Collectors.toList());
        List<Long> dowIds = timeInWeekRequests.stream().map(TimeInWeekRequest::getDayOfWeekId).collect(Collectors.toList());

        Map<Long, Slot> slotMap = slotRepository.findAllById(slotIds).stream()
                .collect(Collectors.toMap(Slot::getId, Function.identity()));
        Map<Long, DayOfWeek> dayOfWeekMap = dayOfWeekRepository.findAllById(dowIds).stream()
                .collect(Collectors.toMap(DayOfWeek::getId, Function.identity()));

        List<TimeInWeek> timeInWeeks = new ArrayList<>();
        for (TimeInWeekRequest timeInWeekRequest : timeInWeekRequests) {
            TimeInWeek timeInWeek = new TimeInWeek();
            DayOfWeek dayOfWeek = Optional.ofNullable(dayOfWeekMap.get(timeInWeekRequest.getDayOfWeekId()))
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                            .withMessage(messageUtil.getLocalMessage(DAY_OF_WEEK_COULD_NOT_BE_FOUND)));
            timeInWeek.setDayOfWeek(dayOfWeek);

            Slot slot = Optional.ofNullable(slotMap.get(timeInWeekRequest.getSlotId()))
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                            .withMessage(messageUtil.getLocalMessage(SLOT_COULD_NOT_BE_FOUND)));
            timeInWeek.setSlot(slot);

            timeInWeeks.add(timeInWeek);
        }
        return timeInWeeks;
    }

    private int calNumberOfSlotByEndDate(Instant startDate, Instant endDate, List<TimeInWeek> timeInWeeks) {
        List<EDayOfWeekCode> availableDOW = timeInWeeks.stream().map(timeInWeek -> timeInWeek.getDayOfWeek().getCode()).distinct().collect(Collectors.toList());
        Instant date = startDate;
        int numberOfSlot = 0;
        while (date.isBefore(endDate) || date.equals(endDate)) {
            EDayOfWeekCode dayOfWeekCode = TimeUtil.getDayOfWeek(date);
            if (availableDOW.contains(dayOfWeekCode)) {
                List<TimeInWeek> dateOfWeeks = timeInWeeks.stream().filter(timeInWeek -> Objects.equals(timeInWeek.getDayOfWeek().getCode(), dayOfWeekCode)).collect(Collectors.toList());
                numberOfSlot = numberOfSlot + dateOfWeeks.size();
            }
            date = date.plus(1, ChronoUnit.DAYS);
        }
        return numberOfSlot;
    }

    private Instant calEndDateByNumberOfSlot(Instant startDate, int numberOfSlot, List<TimeInWeek> timeInWeeks) {
        List<EDayOfWeekCode> availableDOW = timeInWeeks.stream().map(timeInWeek -> timeInWeek.getDayOfWeek().getCode()).distinct().collect(Collectors.toList());
        Instant endDate = startDate;
        int i = numberOfSlot;
        while (i > 0) {
            EDayOfWeekCode dayOfWeekCode = TimeUtil.getDayOfWeek(endDate);
            if (dayOfWeekCode == null) {
                throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không thể nhận diện thứ trong tuần, lỗi hệ thống vui lòng liên hệ Admin!");
            }
            if (availableDOW.contains(dayOfWeekCode)) {
                List<TimeInWeek> dateOfWeeks = timeInWeeks.stream().filter(timeInWeek -> Objects.equals(timeInWeek.getDayOfWeek().getCode(), dayOfWeekCode)).collect(Collectors.toList());
                for (TimeInWeek dow : dateOfWeeks) {
                    if (i <= 0) break;
                    i--;
                }
            }
            endDate = endDate.plus(1, ChronoUnit.DAYS);
        }
        return endDate;
    }

    @Override
    public ClassResponse getDetailClass(Long id) {
        Class clazz = classRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CLASS_NOT_FOUND_BY_ID) + id));
        if (!Objects.equals(clazz.getStatus(), ECourseClassStatus.STARTING)) {
            // throw error later
        }
        User currentUser = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        EUserRole memberOfClassAsRole = ClassValidator.isMemberOfClassAsRole(clazz, currentUser);
        Boolean isManager = SecurityUtil.isHasAnyRole(currentUser, EUserRole.MANAGER);
        if (memberOfClassAsRole == null && !isManager) {
            throw ApiException.create(HttpStatus.INTERNAL_SERVER_ERROR).withMessage("Bạn không có quyền xem thông tin lớp học này !");
        }
//        List<Activity> sectionActivities = clazz.getCourse().getActivities().stream()
//                .filter(activity -> Objects.equals(activity.getType(), ECourseActivityType.SECTION))
//                .collect(Collectors.toList());

        List<Activity> sectionActivities = clazz.getCourse().getActivities().stream()
                .filter(activity -> activity.getType().equals(ECourseActivityType.SECTION))
                .collect(Collectors.toList());

        if (isManager || memberOfClassAsRole.equals(EUserRole.TEACHER)) {
            if (isManager) {
                ResponseUtil.responseForRole(EUserRole.TEACHER);
            } else {
                ResponseUtil.responseForRole(EUserRole.TEACHER);
            }
            return ConvertUtil.convertClassToClassResponse(clazz, sectionActivities);
        } else if (memberOfClassAsRole.equals(EUserRole.STUDENT)) {
            List<Activity> authorizeActivities = sectionActivities.stream().filter(activity -> {
                if (activity.getFixed()) {
                    return true;
                }
                long isAuthorized = activity.getActivityAuthorizes().stream().filter(activityAuthorize -> {
                    Class authorizeClass = activityAuthorize.getAuthorizeClass();
                    return Objects.equals(authorizeClass.getId(), clazz.getId());
                }).count();
                return isAuthorized > 0;
            }).collect(Collectors.toList());
            ClassResponse classResponse = ConvertUtil.convertClassToClassResponse(clazz, authorizeActivities);
            ResponseUtil.responseForRole(memberOfClassAsRole);

            FeedbackTemplate feedbackTemplate = clazz.getFeedbackTemplate();
            if (feedbackTemplate != null) {
                FeedbackTemplateDto feedbackTemplateDto = ConvertUtil.convertFeedbackToFeedbackTemplateDto(feedbackTemplate);
                classResponse.setFeedback(feedbackTemplateDto);
            }

            ImageDto imageDto = ConvertUtil.convertClassImageToImageDto(clazz.getClassImage());
            classResponse.setImage(imageDto);

            return classResponse;
        }
        throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(STUDENT_NOT_BELONG_TO_CLASS));
    }

    @Override
    public ApiPage<SimpleClassResponse> getUserClasses(ClassFilterRequest request, Pageable pageable) {
        User user = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        ClassSpecificationBuilder builder = ClassSpecificationBuilder.classSpecificationBuilder();
        builder.searchByCourseName(request.getQ())
                .filterByStartDay(request.getStartDate())
                .filterByEndDate(request.getEndDate())
                .filterByStatus(request.getStatus());
        if (request.getAsRole() == 2) {
            builder.byMentor(user);
        } else if (request.getAsRole() == 1) {
            builder.byStudent(user);
        } else {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Invalid.INVALID_CLASS_FILTER_ROLE));
        }
        if (!request.getCategoryId().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(request.getCategoryId());
            builder.filterByCategories(categories);
        }
        if (!request.getSubjectId().isEmpty()) {
            List<Subject> subjects = subjectRepository.findAllById(request.getSubjectId());
            builder.filterBySubjects(subjects);

        }
        Specification<Class> specification = builder.build();
        Page<Class> classes = classRepository.findAll(specification, pageable);
        return PageUtil.convert(classes.map(ConvertUtil::convertClassToSimpleClassResponse));
    }

    @Override
    public List<WorkTimeResponse> getWorkingTime() {
        User user = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        Map<EUserRole, Role> userRoles = user.getRoles().stream().collect(Collectors.toMap(Role::getCode, Function.identity()));
        List<WorkTimeResponse> workTimeResponses = new ArrayList<>();

        if (userRoles.get(EUserRole.STUDENT) != null) {
            List<Class> enrolledClasses = user.getStudentClasses().stream().map(StudentClass::getClazz).collect(Collectors.toList());
            for (Class clazz : enrolledClasses) {
                List<TimeTableResponse> timeTables = clazz.getTimeTables().stream()
                        .map(timeTable -> {
                            TimeTableResponse timeTableResponse = ConvertUtil.convertTimeTableToResponse(timeTable);
                            timeTableResponse.setClassURL(clazz.getClassURL());
                            StudentClass userInClass = ClassUtil.findUserInClass(clazz, user);
                            Optional<Attendance> attendanceOpt = timeTable.getAttendances().stream()
                                    .filter(attendance -> Objects.equals(attendance.getStudentClass().getId(), userInClass.getId()))
                                    .findFirst();
                            if (attendanceOpt.isPresent()) {
                                Attendance attendance = attendanceOpt.get();
                                timeTableResponse.setPresent(attendance.getPresent());
                            }
                            return timeTableResponse;
                        })
                        .collect(Collectors.toList());
                SimpleClassResponse classResponse = ConvertUtil.convertClassToSimpleClassResponse(clazz);
                workTimeResponses.add(new WorkTimeResponse(classResponse, EUserRole.STUDENT, timeTables));
            }
            ResponseUtil.responseForRole(EUserRole.STUDENT);
        }


        if (userRoles.get(EUserRole.TEACHER) != null) {
            /**Không xóa những code này*/
            List<Class> workingClasses = user.getCourses().stream()
//                    .filter(course -> Objects.equals(course.getStatus(), ECourseClassStatus.STARTING) || Objects.equals(course.getStatus(), ECourseClassStatus.ENDED) || Objects.equals(course.getStatus(), NOTSTART))
                    .flatMap(course -> course.getClasses().stream())
                    .filter(aClass -> Objects.equals(aClass.getStatus(), ECourseClassStatus.STARTING) || Objects.equals(aClass.getStatus(), ECourseClassStatus.ENDED) || Objects.equals(aClass.getStatus(), NOTSTART))
                    .collect(Collectors.toList());
            /**Tạm thời cho dev, khi run thực sự sẽ dùng dòng trên*/
//            List<Class> workingClasses = user.getCourses().stream()
//                    .flatMap(course -> course.getClasses().stream())
//                    .collect(Collectors.toList());
            /**-------------------------------------------------------*/
            for (Class clazz : workingClasses) {
                List<TimeTableResponse> timeTables = clazz.getTimeTables().stream()
                        .map(timeTable -> {
                            TimeTableResponse timeTableResponse = ConvertUtil.convertTimeTableToResponse(timeTable);
                            timeTableResponse.setClassURL(clazz.getClassURL());
                            return timeTableResponse;
                        })
                        .collect(Collectors.toList());
                SimpleClassResponse classResponse = ConvertUtil.convertClassToSimpleClassResponse(clazz);
                workTimeResponses.add(new WorkTimeResponse(classResponse, EUserRole.TEACHER, timeTables));
            }
            ResponseUtil.responseForRole(EUserRole.TEACHER);
        }
        return workTimeResponses;
    }

    @Override
    public ApiPage<MentorGetClassDetailResponse> mentorGetClass(ECourseClassStatus status, Pageable pageable) {
        User user = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        Page<Class> byMentorAndStatus = classRepository.findByMentorAndStatus(user, status, pageable);
        List<MentorGetClassDetailResponse> classResponses = byMentorAndStatus.getContent().stream()
                .map(ClassUtil::convertClassToMentorClassDetailResponse)
                .collect(Collectors.toList());
        return PageUtil.convert(new PageImpl<>(classResponses, pageable, byMentorAndStatus.getTotalElements()));
    }

    @Override
    public Boolean mentorOpenClass(Long id, List<MentorCreateScheduleRequest> timeTableRequest) throws ValidationErrorsException {

        User user = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        Class clazz = classRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CLASS_NOT_FOUND_BY_ID) + id));
        ClassValidator.isMentorOfClass(user, clazz);
        if (!ClassValidator.isMentorOfClass(user, clazz)) {
            throw ApiException.create(HttpStatus.NOT_FOUND)
                    .withMessage(messageUtil.getLocalMessage(MENTOR_NOT_BELONG_TO_CLASS));
        }
        /**Không xóa block này => chỉ thực sự cho phép mở lớp thủ công khi lớp không đủ số lượng min students*/
//        if (!clazz.getStatus().equals(UNSATISFY)) {
//            throw ApiException.create(HttpStatus.NOT_FOUND)
//                    .withMessage(CLASS_STATUS_NOT_ALLOW);
//        }
        FeedbackTemplate feedbackTemplate = feedbackTemplateRepository.findByTypeAndIsDefault(EFeedbackType.COURSE, true);
        if (feedbackTemplate == null) {
            throw ApiException.create(HttpStatus.INTERNAL_SERVER_ERROR).withMessage(messageUtil.getLocalMessage(""));
        }
        mentorCreateScheduleForClass(clazz, timeTableRequest);
        clazz.setStatus(STARTING);
        clazz.setFeedbackTemplate(feedbackTemplate);
        classRepository.save(clazz);
        return true;
    }

    @Override
    public ApiPage<StudentClassResponse> getClassMembers(Long id, Pageable pageable) {
        Class clazz = classRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CLASS_NOT_FOUND_BY_ID) + id));
        ECourseClassStatus status = clazz.getStatus();
        boolean mentorOfClass = ClassValidator.isMentorOfClass(SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional()), clazz);
        if (!mentorOfClass) {
            throw ApiException.create(HttpStatus.NOT_FOUND)
                    .withMessage(messageUtil.getLocalMessage(MENTOR_NOT_BELONG_TO_CLASS));
        }
        if (!Objects.equals(status, NOTSTART) && !Objects.equals(status, STARTING) && !Objects.equals(status, ENDED)) {
            throw ApiException.create(HttpStatus.NOT_FOUND)
                    .withMessage(messageUtil.getLocalMessage(CLASS_STATUS_NOT_ALLOW));
        }
        List<StudentClass> studentClasses = clazz.getStudentClasses();
        Page<StudentClass> studentClassPage = new PageImpl<>(studentClasses, pageable, studentClasses.size());
        return PageUtil.convert(studentClassPage.map(ConvertUtil::convertStudentClassToResponse));
    }

    @Override
    public ApiPage<MentorGetClassDetailResponse> managerGetClass(ECourseClassStatus status, Pageable pageable) {

        Page<Class> byStatus = classRepository.findByStatus(status, pageable);
        List<MentorGetClassDetailResponse> classResponses = byStatus.getContent().stream()
                .map(ClassUtil::convertClassToMentorClassDetailResponse)
                .collect(Collectors.toList());
        return PageUtil.convert(new PageImpl<>(classResponses, pageable, byStatus.getTotalElements()));

    }

    @Override
    public ApiPage<MentorGetClassDetailResponse> getAllClassForSetTemplateFeedback(Pageable pageable) {

        List<ECourseClassStatus> statuses = new ArrayList<>(Arrays.asList(NOTSTART, STARTING));
        List<Class> byStatusIn = classRepository.findByStatus_In(statuses);

        List<MentorGetClassDetailResponse> classResponses = new ArrayList<>();
        byStatusIn.forEach(aClass -> {
            classResponses.add(ClassUtil.convertClassToMentorClassDetailResponse(aClass));
        });
        return PageUtil.convert(new PageImpl<>(classResponses, pageable, classResponses.size()));
    }

    public Boolean simulateCloseClassEvent(Long classId) {
        Class clazz = classRepository.findById(classId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CLASS_NOT_FOUND_BY_ID) + classId));
        if (!clazz.getStatus().equals(STARTING)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Trạng thái lớp không hợp lệ để giả lập: " + clazz.getStatus());
        }
        ClassUtil.handleCloseClassEvent(clazz);
        return true;
    }

    @Override
    public List<GetPointStudentClassResponse> getStudentPoint(GetPointStudentClassRequest request) {

        Class clazz = classRepository.findById(request.getClassId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(CLASS_NOT_FOUND_BY_ID) + request.getClassId()));

        User user = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(CLASS_NOT_FOUND_BY_ID) + request.getStudentId()));

        StudentClass studentClass = studentClassRepository.findByClazzAndStudent(clazz, user)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage("Không tìm thấy học sinh trong lớp học này "));

        List<GetPointStudentClassResponse> pointStudentClassResponses = new ArrayList<>();

        Optional<AssignmentSubmition> byStudentClass = assignmentSubmittionRepository.findByStudentClass(studentClass);
        if (byStudentClass.isPresent()) {
            AssignmentSubmition assignmentSubmition = byStudentClass.get();
            Activity activity = assignmentSubmition.getAssignment().getActivity();
            GetPointStudentClassResponse response = new GetPointStudentClassResponse();
            response.setPoint(assignmentSubmition.getPoint());
            response.setActivityName(activity.getName());
            response.setType(ECourseActivityType.ASSIGNMENT);
            response.setCreated(activity.getCreated());
            response.setLastUpdated(activity.getLastModified());
            pointStudentClassResponses.add(response);
        }


        Course course = clazz.getCourse();
        List<Activity> activities = course.getActivities();
        List<Activity> activitiesOfClasses = getActivitiesOfClasses(clazz, activities);
        List<Activity> collectQuiz = activitiesOfClasses.stream().filter(activity
                        -> activity.getType().equals(ECourseActivityType.QUIZ))
                .collect(Collectors.toList());

        for (Activity activity : collectQuiz) {
            Quiz quiz = activity.getQuiz();
            List<QuizSubmittion> quizSubmissions = quiz.getQuizSubmittions();
            for (QuizSubmittion quizSubmittion : quizSubmissions) {
                if (quizSubmittion.getSubmittedBy().equals(user)) {
                    GetPointStudentClassResponse response = new GetPointStudentClassResponse();
                    response.setPoint(quizSubmittion.getPoint());
                    response.setActivityName(activity.getName());
                    response.setType(ECourseActivityType.QUIZ);
                    response.setCreated(activity.getCreated());
                    response.setLastUpdated(activity.getLastModified());
                    pointStudentClassResponses.add(response);
                }
            }
        }
        return pointStudentClassResponses;
    }

    @NotNull
    private static List<Activity> getActivitiesOfClasses(Class clazz, List<Activity> activities) {
        List<Activity> sectionActivities = activities.stream()
                .filter(activity -> Objects.equals(activity.getType(), ECourseActivityType.QUIZ) || Objects.equals(activity.getType(), ECourseActivityType.ASSIGNMENT))
                .collect(Collectors.toList());
        return sectionActivities.stream().filter(activity -> {
            if (activity.getFixed()) {
                return true;
            }
            long isAuthorized = activity.getActivityAuthorizes().stream().filter(activityAuthorize -> {
                Class authorizeClass = activityAuthorize.getAuthorizeClass();
                return Objects.equals(authorizeClass.getId(), clazz.getId());
            }).count();
            return isAuthorized > 0;
        }).collect(Collectors.toList());
    }

    @Override
    public List<BaseClassResponse> getDuplicateTimeClassOfStudent(Long id) {
        User student = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        Class clazz = classRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(CLASS_NOT_FOUND_BY_ID) + id));


        Set<EDayOfWeekCode> checkedClassEDayCodes = clazz.getTimeInWeeks()
                .stream()
                .map(TimeInWeek::getDayOfWeek)
                .map(DayOfWeek::getCode)
                .collect(Collectors.toSet());

        List<Class> possibleDuplicateClasses = classRepository.findByStudentAndStartDate(student, clazz.getStartDate());
        List<Class> duplicateClasses = possibleDuplicateClasses.stream()
                .filter(possibleDuplicateClass -> possibleDuplicateClass.getTimeInWeeks()
                        .stream()
                        .anyMatch(timeInWeek -> checkedClassEDayCodes.contains(timeInWeek.getDayOfWeek().getCode())))
                .filter(possibleDuplicateClass -> possibleDuplicateClass.getTimeInWeeks()
                        .stream()
                        .anyMatch(timeInWeek -> clazz.getTimeInWeeks().stream()
                                .anyMatch(checkedTimeInWeek -> checkedTimeInWeek.getDayOfWeek().getCode().equals(timeInWeek.getDayOfWeek().getCode())
                                        && checkedTimeInWeek.getSlot().getId().equals(timeInWeek.getSlot().getId()))))
                .collect(Collectors.toList());

        List<BaseClassResponse> baseClassResponses = duplicateClasses.stream()
                .map(ClassUtil::convertClassToBaseclassResponse)
                .collect(Collectors.toList());

        return baseClassResponses;
    }

    public HashMap<String, List<MentorGetClassDetailResponse>> getClassesNotUseTemplate(Long templateId) {
        FeedbackTemplate feedbackTemplate = feedbackTemplateRepository.findById(templateId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(FEEDBACK_QUESTION_NOT_FOUND_BY_ID) + templateId));
        if (!feedbackTemplate.getType().equals(EFeedbackType.COURSE)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_FEEDBACK_TYPE));
        }
        List<ECourseClassStatus> statuses = new ArrayList<>(Arrays.asList(NOTSTART, STARTING));
        List<Class> classes = classRepository.findByStatus_In(statuses);
        List<Class> classNotUseTemplate = classes.stream()
                .filter(aClass -> !aClass.getFeedbackTemplate().equals(feedbackTemplate))
                .collect(Collectors.toList());
        classes.removeAll(classNotUseTemplate);
        List<MentorGetClassDetailResponse> notUseClass = classNotUseTemplate.stream()
                .map(ClassUtil::convertClassToMentorClassDetailResponse)
                .collect(Collectors.toList());
        List<MentorGetClassDetailResponse> useClass = classes.stream()
                .map(ClassUtil::convertClassToMentorClassDetailResponse)
                .collect(Collectors.toList());
        HashMap<String, List<MentorGetClassDetailResponse>> map = new HashMap<>();
        map.put("use", useClass);
        map.put("notUse", notUseClass);
        return map;
    }

    @Override
    public boolean setClassURL(Long id, String url) {
        validateMeetingURL(url);
        Class clazz = classRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(CLASS_NOT_FOUND_BY_ID) + id));
        User mentor = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        if (!ClassValidator.isMentorOfClass(mentor, clazz)) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(MENTOR_NOT_BELONG_TO_CLASS));
        }
        clazz.setClassURL(url);
        classRepository.save(clazz);
        return true;
    }

    private void validateMeetingURL(String url) {
        List<String> allowedMeetingUrls = classUtil.getAllowedMeetingUrl();
        boolean isValidMeeting = ClassValidator.checkAllowMeetingLink(url, allowedMeetingUrls);
        if (!isValidMeeting) {
            throw ApiException.create(HttpStatus.NOT_FOUND)
                    .withMessage("Liên kết không hợp lệ, vui lòng chỉ sử dụng liên kết được cho phép");
        }
    }
}