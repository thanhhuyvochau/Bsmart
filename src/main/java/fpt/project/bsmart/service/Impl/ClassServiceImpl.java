package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EDayOfWeekCode;
import fpt.project.bsmart.entity.request.CreateClassInformationRequest;
import fpt.project.bsmart.entity.request.MentorCreateClassRequest;
import fpt.project.bsmart.entity.request.TimeInWeekRequest;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.IClassService;
import fpt.project.bsmart.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static fpt.project.bsmart.entity.constant.ECourseStatus.REQUESTING;
import static fpt.project.bsmart.util.Constants.ErrorMessage.*;


@Service
@Transactional
public class ClassServiceImpl implements IClassService {

    private final MessageUtil messageUtil;
    private final CategoryRepository categoryRepository;

    private final ClassRepository classRepository;

    private final DayOfWeekRepository dayOfWeekRepository;
    private final SlotRepository slotRepository;


    private final CourseRepository courseRepository;

    private final ClassImageRepository classImageRepository;

    public ClassServiceImpl(MessageUtil messageUtil, CategoryRepository categoryRepository, ClassRepository classRepository, DayOfWeekRepository dayOfWeekRepository, SlotRepository slotRepository, CourseRepository courseRepository, ClassImageRepository classImageRepository) {
        this.messageUtil = messageUtil;
        this.categoryRepository = categoryRepository;
        this.classRepository = classRepository;
        this.dayOfWeekRepository = dayOfWeekRepository;
        this.slotRepository = slotRepository;
        this.courseRepository = courseRepository;
        this.classImageRepository = classImageRepository;
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
    public List<Long> mentorCreateCoursePrivate(MentorCreateClassRequest mentorCreateClassRequest) {
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();
        // Step 1
        Course course = createCourseFromRequest(currentUserAccountLogin, mentorCreateClassRequest);
        // Step 2
        return createClassAndTimeInWeek(currentUserAccountLogin, course, mentorCreateClassRequest);
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

        Course course = new Course();
        course.setName(mentorCreateClassRequest.getName());
        course.setCode(CourseUtil.generateRandomCode(8));
        course.setDescription(mentorCreateClassRequest.getDescription());
        course.setSubject(subject);
        course.setStatus(REQUESTING);
        course.setCreator(currentUserAccountLogin);

        return course;
    }

    private List<Long> createClassAndTimeInWeek(User currentUserAccountLogin, Course course, MentorCreateClassRequest mentorCreateClassRequest) {
        // check mentor account is valid
        MentorUtil.checkIsMentor();


        List<Long> classIds = new ArrayList<>();
        List<CreateClassInformationRequest> createClassInformationRequests = mentorCreateClassRequest.getCreateClassRequest();
        List<Class> classes = new ArrayList<>();
        createClassInformationRequests.forEach(createClassInformationRequest -> {
            List<TimeInWeekRequest> timeInWeekRequests = createClassInformationRequest.getTimeInWeekRequests();

            // create time in week for subCourse
            List<TimeInWeek> timeInWeeksFromRequest = createTimeInWeeksFromRequest(timeInWeekRequests);

            // create subCourse for course
            Class classFromRequest = createClassFromRequest(createClassInformationRequest, course , currentUserAccountLogin, timeInWeeksFromRequest);
            classFromRequest.setCourse(course);

            classes.add(classFromRequest);

        });
        course.setClasses(classes);
        courseRepository.save(course);
        // ghi log
        classes.forEach(aClass -> {
                    classIds.add(aClass.getId());
                    ActivityHistoryUtil.logHistoryForCourseCreated(currentUserAccountLogin.getId(), aClass);
                }
        );
        return classIds;
    }

    private Class createClassFromRequest(CreateClassInformationRequest subCourseRequest,Course course , User currentUserAccountLogin, List<TimeInWeek> timeInWeeks) {
        if (subCourseRequest.getPrice() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(PLEASE_ENTER_THE_PRICE_FOR_THE_COURSE));
        }
        Class aClass = new Class();
        aClass.setNumberOfSlot(subCourseRequest.getNumberOfSlot());
        aClass.setMinStudent(subCourseRequest.getMinStudent());
        aClass.setMaxStudent(subCourseRequest.getMaxStudent());
        aClass.setStartDateExpected(subCourseRequest.getStartDateExpected());
        aClass.setStatus(REQUESTING);
        aClass.setPrice(subCourseRequest.getPrice());
        aClass.setLevel(subCourseRequest.getLevel());
        aClass.setMentor(currentUserAccountLogin);
        String codeRandom = ClassUtil.generateCode(course.getSubject().getCode());
        aClass.setCode(codeRandom);

        Long imageId = subCourseRequest.getImageId();
        ClassImage classImage = classImageRepository.findById(imageId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(IMAGE_NOT_FOUND_BY_ID) + imageId));
        aClass.setClassImage(classImage);
        aClass.setTimeInWeeks(timeInWeeks);

        if (subCourseRequest.getEndDateExpected() != null) {
            Instant endDateExpected = subCourseRequest.getEndDateExpected();
            int numberOfSlot = calNumberOfSlotByEndDate(subCourseRequest.getStartDateExpected(), endDateExpected, timeInWeeks);
            aClass.setNumberOfSlot(numberOfSlot);
            aClass.setEndDateExpected(endDateExpected);
        } else if (subCourseRequest.getNumberOfSlot() != null) {
            Integer numberOfSlot = subCourseRequest.getNumberOfSlot();
            Instant endDateExpected = calEndDateByNumberOfSlot(subCourseRequest.getStartDateExpected(), numberOfSlot, timeInWeeks);
            aClass.setNumberOfSlot(numberOfSlot);
            aClass.setEndDateExpected(endDateExpected);
        } else {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Lỗi không tìm thấy số lượng slot học hoặc ngày kết thúc");
        }
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
}
//    private final MessageUtil messageUtil;
//    private final CourseRepository courseRepository;
//    private final OrderDetailRepository orderDetailRepository;
//
//    private final ClassRepository classRepository;
//    private final SubCourseRepository subCourseRepository;
//    private final ClassSectionRepository classSectionRepository;
//
//    public ClassServiceImpl(MessageUtil messageUtil, CourseRepository courseRepository, OrderDetailRepository orderDetailRepository, ClassRepository classRepository, SubCourseRepository subCourseRepository, ClassSectionRepository classSectionRepository) {
//        this.messageUtil = messageUtil;
//        this.courseRepository = courseRepository;
//        this.orderDetailRepository = orderDetailRepository;
//        this.classRepository = classRepository;
//        this.subCourseRepository = subCourseRepository;
//        this.classSectionRepository = classSectionRepository;
//    }
//
//    @Override
//    public Boolean createClass(CreateClassRequest request) {
//        SubCourse subCourse = subCourseRepository.findById(request.getSubCourseId())
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(SUB_COURSE_NOT_FOUND_BY_ID) + request.getSubCourseId()));
//        Integer numberOfSlot = subCourse.getNumberOfSlot();
//        Instant startDate = request.getNowIsStartDate() ? Instant.now() : request.getStartDate();
//        Class clazz = new Class();
//        clazz.setStartDate(startDate);
//        clazz.setSubCourse(subCourse);
//        List<TimeTable> timeTables = TimeInWeekUtil.generateTimeTable(subCourse.getTimeInWeeks(), numberOfSlot, startDate, clazz);
//        clazz.getTimeTables().addAll(timeTables);
//        // Thêm học sinh thanh toán thành công vào lớp
//        List<OrderDetail> successOrderDetails = orderDetailRepository.findAllBySubCourse(subCourse).stream()
//                .filter(orderDetail -> Objects.equals(orderDetail.getOrder().getStatus(), EOrderStatus.SUCCESS))
//                .collect(Collectors.toList());
//        List<User> successRegisterUsers = successOrderDetails.stream().map(orderDetail -> orderDetail.getOrder().getUser()).collect(Collectors.toList());
//        for (User successRegisterUser : successRegisterUsers) {
//            StudentClass studentClass = new StudentClass();
//            studentClass.setStudent(successRegisterUser);
//            studentClass.setClazz(clazz);
//            clazz.getStudentClasses().add(studentClass);
//        }
//        // Copy module từ course qua làm base chương trình cho lớp học
//        List<Section> sections = subCourse.getCourse().getSections();
//        for (Section section : sections) {
//            ClassSection classSection = new ClassSection(section.getName(), clazz);
//            for (Module module : section.getModules()) {
//                ClassModule classModule = new ClassModule(module.getName(), classSection);
//                classSection.getClassModules().add(classModule);
//            }
//            clazz.getClassSections().add(classSection);
//        }
//        classRepository.save(clazz);
//        return true;
//    }
//
//    @Override
//    public ApiPage<SimpleClassResponse> getClassFeedbacks(ClassFeedbackRequest classFeedbackRequest, Pageable pageable) {
//        ClassSpecificationBuilder classSpecificationBuilder = ClassSpecificationBuilder.classSpecificationBuilder()
//                .searchBySubCourseName(classFeedbackRequest.getSubCourseName())
//                .searchByMentorName(classFeedbackRequest.getMentorName())
//                .filterByStartDay(classFeedbackRequest.getStartDate())
//                .filterByEndDate(classFeedbackRequest.getEndDate());
//        Page<Class> classes = classRepository.findAll(classSpecificationBuilder.build(), pageable);
//        List<SimpleClassResponse> simpleClassRespons = classes.stream()
//                .map(ConvertUtil::convertClassToSimpleClassResponse)
//                .collect(Collectors.toList());
//        PageImpl<SimpleClassResponse> classResponsePage = new PageImpl<>(simpleClassRespons);
//        return PageUtil.convert(classResponsePage);
//
//    }
//
//    @Override
//    public ClassProgressTimeDto getClassProgression(Long clazzId) {
//        Class clazz = classRepository.findById(clazzId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CLASS_NOT_FOUND_BY_ID) + clazzId));
//        if (clazz.getStartDate().isBefore(Instant.now())) {
//            throw ApiException.create(HttpStatus.CONFLICT).withMessage(messageUtil.getLocalMessage(BEFORE_CLASS_START_TIME));
//        }
//        return Optional.ofNullable(ClassUtil.getPercentageOfClassTime(clazz)).orElseThrow(() -> ApiException.create(HttpStatus.CONFLICT).withMessage(messageUtil.getLocalMessage(INTERNAL_SERVER_ERROR)));
//    }
//
//    @Override
//    public ClassResponse getDetailClass(Long id) {
//        Class clazz = classRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CLASS_NOT_FOUND_BY_ID) + id));
//        User currentUser = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
//        if (ClassValidator.isMentorOfClass(currentUser, clazz) || ClassValidator.isStudentOfClass(clazz, currentUser)) {
//            return ConvertUtil.convertClassToClassResponse(clazz);
//        }
//        throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(STUDENT_NOT_BELONG_TO_CLASS));
//    }
//
//    @Override
//    public ApiPage<SimpleClassResponse> getUserClasses(ClassFilterRequest request, Pageable pageable) {
//        User user = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
//        ClassSpecificationBuilder builder = ClassSpecificationBuilder.classSpecificationBuilder();
//        builder.searchByClassName(request.getQ())
//                .filterByStartDay(request.getStartDate())
//                .filterByEndDate(request.getEndDate())
//                .filterByStatus(request.getStatus());
//
//        if (request.getAsRole() == 2) {
//            builder.byMentor(user);
//        } else if (request.getAsRole() == 1) {
//            builder.byStudent(user);
//        }
//        Specification<Class> specification = builder.build();
//        Page<Class> classes = classRepository.findAll(specification, pageable);
//        return PageUtil.convert(classes.map(ConvertUtil::convertClassToSimpleClassResponse));
//    }
//
//    @Override
//    public ClassSectionDto createClassSection(ClassSectionCreateRequest request, Long classId) {
//        ClassSection classSection = new ClassSection();
//        classSection.setName(request.getName());
//        Optional<Class> optionalClass = classRepository.findById(classId);
//        if (optionalClass.isPresent()) {
//            Class clazz = optionalClass.get();
//            if (!ClassValidator.isMentorOfClass(SecurityUtil.getCurrentUser(), clazz)) {
//                throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(MENTOR_NOT_BELONG_TO_CLASS));
//            }
//            classSection.setClazz(clazz);
//        } else {
//            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(INVALID_CLASS_ID) + classId);
//        }
//        ClassSection savedClassSection = classSectionRepository.save(classSection);
//        return ConvertUtil.convertClassSectionToDto(savedClassSection);
//    }
//
//    @Override
//    public ClassSectionDto getClassSection(Long classSectionId, Long classId) {
//        Optional<ClassSection> optionalClassSection = classSectionRepository.findById(classSectionId);
//        if (optionalClassSection.isPresent()) {
//            ClassSection classSection = optionalClassSection.get();
//            Class clazz = classSection.getClazz();
//            if (!Objects.equals(clazz.getId(), classId)) {
//                throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(INVALID_PARAMETER_VALUE));
//            } else if (!ClassValidator.isMentorOfClass(SecurityUtil.getCurrentUser(), clazz)) {
//                throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(MENTOR_NOT_BELONG_TO_CLASS));
//            }
//            return ConvertUtil.convertClassSectionToDto(classSection);
//        } else {
//            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CLASS_SECTION_NOT_FOUND_BY_ID) + classSectionId);
//        }
//    }
//
//    @Override
//    public ClassSectionDto updateClassSection(Long classId, Long classSectionId, ClassSectionUpdateRequest request) {
//        Optional<ClassSection> optionalClassSection = classSectionRepository.findById(classSectionId);
//        if (optionalClassSection.isPresent()) {
//            ClassSection classSection = optionalClassSection.get();
//            classSection.setName(request.getName());
//            Class clazz = classSection.getClazz();
//            if (!Objects.equals(clazz.getId(), classId)) {
//                throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(INVALID_PARAMETER_VALUE));
//            } else if (!ClassValidator.isMentorOfClass(SecurityUtil.getCurrentUser(), clazz)) {
//                throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(MENTOR_NOT_BELONG_TO_CLASS));
//            }
//            ClassSection updatedClassSection = classSectionRepository.save(classSection);
//            return ConvertUtil.convertClassSectionToDto(updatedClassSection);
//        } else {
//            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CLASS_SECTION_NOT_FOUND_BY_ID) + classSectionId);
//        }
//    }
//
//    @Override
//    public Boolean deleteClassSection(Long classSectionId, Long classId) {
//        Optional<ClassSection> optionalClassSection = classSectionRepository.findById(classSectionId);
//        if (optionalClassSection.isPresent()) {
//            ClassSection classSection = optionalClassSection.get();
//            Class clazz = classSection.getClazz();
//            if (!Objects.equals(clazz.getId(), classId)) {
//                throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(INVALID_PARAMETER_VALUE));
//            } else if (!ClassValidator.isMentorOfClass(SecurityUtil.getCurrentUser(), clazz)) {
//                throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(MENTOR_NOT_BELONG_TO_CLASS));
//            }
//            classSectionRepository.delete(classSection);
//        } else {
//            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CLASS_SECTION_NOT_FOUND_BY_ID) + classSectionId);
//        }
//        return true;
//    }

