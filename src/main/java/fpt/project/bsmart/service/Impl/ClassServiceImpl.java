package fpt.project.bsmart.service.Impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.Module;
import fpt.project.bsmart.entity.common.*;
import fpt.project.bsmart.entity.dto.*;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.response.*;
import fpt.project.bsmart.moodle.repository.MoodleCourseRepository;
import fpt.project.bsmart.moodle.request.CreateCourseRequest;
import fpt.project.bsmart.moodle.request.GetMoodleCourseRequest;
import fpt.project.bsmart.moodle.request.S1CourseRequest;
import fpt.project.bsmart.moodle.response.*;
import fpt.project.bsmart.moodle.response.MoodleCourseResponse;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.IClassService;
import fpt.project.bsmart.service.IMoodleService;
import fpt.project.bsmart.util.*;
import fpt.project.bsmart.util.specification.ClassSpecificationBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class ClassServiceImpl implements IClassService {


    private final AccountRepository accountRepository;
    private final SubjectRepository subjectRepository;
    private final ClassRepository classRepository;

    private final CourseRepository courseRepository;

    private final MoodleCourseRepository moodleCourseRepository;

    private final ClassLevelRepository classLevelRepository;

    private final MessageUtil messageUtil;

    private final AccountUtil accountUtil;

    private final SecurityUtil securityUtil;

    private final AttendanceRepository attendanceRepository;


    private final InfoFindTutorRepository infoFindTutorRepository;
    private final IMoodleService moodleService;
    protected final ClassTeacherCandicateRepository classTeacherCandicateRepository;
    private final TeachingConfirmationRepository teachingConfirmationRepository;

    private final FeedbackClassLogRepository feedbackClassLogRepository;
    @Value("${teaching-confirmation-url}")
    private String confirmLink;

    public ClassServiceImpl(AccountRepository accountRepository
            , SubjectRepository subjectRepository, ClassRepository classRepository,
                            CourseRepository courseRepository, MoodleCourseRepository moodleCourseRepository, ClassLevelRepository classLevelRepository,
                            MessageUtil messageUtil, AccountUtil accountUtil, SecurityUtil securityUtil, AttendanceRepository attendanceRepository,
                            InfoFindTutorRepository infoFindTutorRepository, IMoodleService moodleService, ClassTeacherCandicateRepository classTeacherCandicateRepository, TeachingConfirmationRepository teachingConfirmationRepository, FeedbackClassLogRepository feedbackClassLogRepository) {
        this.accountRepository = accountRepository;
        this.subjectRepository = subjectRepository;
        this.classRepository = classRepository;
        this.courseRepository = courseRepository;
        this.moodleCourseRepository = moodleCourseRepository;
        this.classLevelRepository = classLevelRepository;

        this.messageUtil = messageUtil;
        this.accountUtil = accountUtil;
        this.securityUtil = securityUtil;
        this.attendanceRepository = attendanceRepository;
        this.infoFindTutorRepository = infoFindTutorRepository;
        this.moodleService = moodleService;
        this.classTeacherCandicateRepository = classTeacherCandicateRepository;
        this.teachingConfirmationRepository = teachingConfirmationRepository;
        this.feedbackClassLogRepository = feedbackClassLogRepository;
    }


    @Override
    public Long teacherRequestCreateClass(TeacherCreateClassRequest createClassRequest) throws JsonProcessingException, ParseException {

        Account teacher = securityUtil.getCurrentUserThrowNotFoundException();

        // set class bên vườn đậu
        Class clazz = new Class();
        clazz.setName(createClassRequest.getName());
        if (classRepository.existsByCode(createClassRequest.getCode())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Class code existed!"));
        }
        clazz.setCode(createClassRequest.getCode());
        Course course = courseRepository.findById(createClassRequest.getCourseId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Course not found by id:" + createClassRequest.getCourseId()));


        Instant now = DayUtil.convertDayInstant(Instant.now().toString());
        if (!DayUtil.checkTwoDateBigger(now.toString(), createClassRequest.getStartDate().toString(), 10)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Ngày bắt đâu mở lơp phải sớm hơn ngày hiện tại la 10 ngay"));
        }

        if (!DayUtil.checkTwoDateBigger(createClassRequest.getStartDate().toString(), createClassRequest.getEndDate().toString(), 30)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Ngày bắt đâu mở lơp phải sớm hơn ngày kêt thúc lớp ít nhất la 30 ngay"));
        }


        clazz.setStartDate(DayUtil.convertDayInstant(createClassRequest.getStartDate().toString()));
        clazz.setEndDate(DayUtil.convertDayInstant(createClassRequest.getEndDate().toString()));
        clazz.setMinNumberStudent(createClassRequest.getMinNumberStudent());
        clazz.setMaxNumberStudent(createClassRequest.getMaxNumberStudent());
        ClassLevel classLevel = classLevelRepository.findByCode(createClassRequest.getClassLevel()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Course not found by id:" + createClassRequest.getClassLevel()));
        clazz.setClassLevel(classLevel);

        clazz.setActive(false);
        clazz.setAccount(teacher);
        clazz.setCourse(course);
        clazz.setStatus(EClassStatus.REQUESTING);
        clazz.setClassType(createClassRequest.getClassType());
        clazz.setUnitPrice(createClassRequest.getEachStudentPayPrice());

        Class save = classRepository.save(clazz);
        Boolean synchronizedAccount = accountUtil.synchronizedCurrentAccountInfo();

        if (synchronizedAccount) {
            Boolean moodleCourse = createMoodleCourse(save, course);
        }


        return save.getId();
    }

    @Override
    public Long teacherSubmitRequestCreateClass(Long id) throws JsonProcessingException {
        Account teacher = securityUtil.getCurrentUserThrowNotFoundException();
        Class clazz = classRepository.findByIdAndAccount(id, teacher);
        if (clazz == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Không tim thấy lớp"));
        }

        if (clazz.getMoodleClassId() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Lớp chưa được tạo nội ở Moodle, vui lòng liên hệ quản lý để được hỗ trợ!!"));
        }

        moodleService.synchronizedClassDetailFromMoodle(clazz);
        if (clazz.getSections().isEmpty() || clazz.getSections().size() < 2) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Lớp chưa được tạo nội dụng bài học , vui lòng cập nhật!!"));
        }

        if (clazz.getTimeTables() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Lớp chưa được tạo thời khóa bểu , vui lòng cập nhật!!"));
        }
        clazz.setStatus(EClassStatus.WAITING);
        Class save = classRepository.save(clazz);
        return save.getId();
    }


    @Override
    public Long teacherRequestCreateClassSubjectCourse(Long id, CreateClassSubjectRequest createClassRequest) {
        Class aClass = new Class();
        aClass = classRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay class" + id));
        if (!aClass.getStatus().equals(EClassStatus.REQUESTING) && !aClass.getStatus().equals(EClassStatus.RECRUITING)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Bạn không thể cập nhật topic cho khác này"));
        }
        Course course = courseRepository.findById(createClassRequest.getCourseId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Course not found by id:" + createClassRequest.getCourseId()));
        aClass.setCourse(course);
        classRepository.save(aClass);
        return aClass.getId();
    }

//    @Override
//    public Boolean synchronizedClassToMoodle(CreateCourseRequest createCourseRequest) throws JsonProcessingException {
//
//
//        S1CourseRequest s1CourseRequest = new S1CourseRequest();
//        List<CreateCourseRequest.CreateCourseBody> createCourseBodyList = new ArrayList<>();
//        for (CreateCourseRequest.CreateCourseBody request : createCourseRequest.getCourses()) {
//            CreateCourseRequest.CreateCourseBody createCourseBody = new CreateCourseRequest.CreateCourseBody();
//            createCourseBody.setFullname(request.getFullname());
//            createCourseBody.setShortname(request.getShortname());
//            createCourseBody.setCategoryid(request.getCategoryid());
//            createCourseBodyList.add(createCourseBody);
//        }
//
//        s1CourseRequest.setCourses(createCourseBodyList);
//
//        List<MoodleCourseResponse> courseRespons = moodleCourseRepository.createCourse(s1CourseRequest);
//
//        return true;
//    }

    @Override
    public ClassDto adminApproveRequestCreateClass(Long id) throws JsonProcessingException {
        Class clazz = classRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay class" + id));
        if (!clazz.getStatus().equals(EClassStatus.WAITING)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("class đã được active"));
        }
        clazz.setActive(true);
        clazz.setStatus(EClassStatus.NOTSTART);
        Forum classForum = createClassForum(clazz);
        clazz.setForum(classForum);
        Class savedClass = classRepository.save(clazz);
        Course course = savedClass.getCourse();

        ClassDto classDto = ObjectUtil.copyProperties(savedClass, new ClassDto(), ClassDto.class);
        ClassLevel classLevel = savedClass.getClassLevel();
        if (classLevel != null) {
            classDto.setClassLevel(classLevel.getCode());
        }
        if (clazz.getCourse() != null) {

            classDto.setCourse(ConvertUtil.doConvertCourseToCourseResponse(course));
        }

        if (savedClass.getAccount() != null) {
            classDto.setTeacher(ConvertUtil.doConvertEntityToSimpleResponse(savedClass.getAccount()));

        }
        return classDto;
    }

    @Override
    public ChangeInfoClassRequest adminRequestChangeInfoClass(Long id, String content) {
        Account teacher = securityUtil.getCurrentUserThrowNotFoundException();
        Class clazz = classRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay class" + id));
        clazz.setStatus(EClassStatus.EDITREQUEST);
        FeedbackClassLog log = new FeedbackClassLog();
        log.setaClass(clazz);
        log.setAccount(teacher);
        log.setContent(content);
        log.setStatus(EFeedbackClassLogStatus.EDITREQUEST);

        feedbackClassLogRepository.save(log);

        classRepository.save(clazz);
        ChangeInfoClassRequest response = new ChangeInfoClassRequest();
        response.setContent(content);
        response.setId(clazz.getId());
        return response;
    }

    public Boolean createMoodleCourse(Class save, Course course) throws JsonProcessingException {
        S1CourseRequest s1CourseRequest = new S1CourseRequest();
        List<CreateCourseRequest.CreateCourseBody> createCourseBodyList = new ArrayList<>();

        CreateCourseRequest.CreateCourseBody createCourseBody = new CreateCourseRequest.CreateCourseBody();
        createCourseBody.setFullname(save.getName());
        createCourseBody.setShortname(save.getCode());
        if (course != null) {
            createCourseBody.setCategoryid(course.getSubject().getCategoryMoodleId());
        }
        if (save.getStartDate() != null) {
            createCourseBody.setStartdate(save.getStartDate().toEpochMilli());
        }
        if (save.getEndDate() != null) {
            createCourseBody.setEnddate(save.getEndDate().toEpochMilli());
        }
        createCourseBodyList.add(createCourseBody);
        s1CourseRequest.setCourses(createCourseBodyList);

        List<MoodleCourseResponse> courseResponses = moodleCourseRepository.createCourse(s1CourseRequest);
        MoodleCourseResponse moodleCourseResponse = courseResponses.get(0);
        save.setMoodleClassId(moodleCourseResponse.getId());
        return true;
    }

    @Override
    public ApiPage<ClassDto> getClassRequesting(ClassSearchRequest query, Pageable pageable) {
        ClassSpecificationBuilder builder = ClassSpecificationBuilder.specification()
                .query(query.getQ())
                .queryByClassStatus(query.getStatus())
                .queryBySubject(query.getSubjectId())
                .queryByClassType(query.getClassType())
                .queryByDate(query.getDateFrom(), query.getDateTo())
                .queryTeacherClass(query.getTeacherId());


        Page<Class> classesPage = classRepository.findAll(builder.build(), pageable);

        return PageUtil.convert(classesPage.map(ConvertUtil::doConvertEntityToResponse));


    }

    @Override
    public Boolean studentEnrollClass(Long studentId, Long classId) {
        Account student = accountRepository.findById(studentId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay student" + studentId));

        Class aClass = classRepository.findById(classId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay class" + classId));


        List<StudentClass> studentClasses = student.getStudentClasses();
        studentClasses.stream().map(studentClass -> {
            if (studentClass.getaClass().equals(aClass)) {
                throw ApiException.create(HttpStatus.BAD_REQUEST)
                        .withMessage(messageUtil.getLocalMessage("student dang trong class nay roi"));
            }
            return studentClass;
        }).collect(Collectors.toList());


        StudentClass studentClass = new StudentClass();
        studentClass.setAClass(aClass);
        studentClass.setAccount(student);
        studentClass.setIs_enrolled(false);
        student.getStudentClasses().add(studentClass);
        accountRepository.save(student);

        return true;
    }


    @Override
    public List<ClassStudentDto> getStudentWaitingIntoClass(Long classId) {
        Class aClass = classRepository.findById(classId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay class" + classId));
        List<StudentClass> studentClasses = aClass.getStudentClasses();

        ClassStudentDto classStudentDto = new ClassStudentDto();
        List<ClassStudentDto> classStudentDtos = new ArrayList<>();
        classStudentDto.setClassId(classId);
        List<StudentDto> studentList = new ArrayList<>();
        studentClasses.stream().map(studentClass -> {
            StudentDto studentDto = new StudentDto();
            Account account = studentClass.getAccount();
            if (account != null) {
                studentDto = ObjectUtil.copyProperties(account, new StudentDto(), StudentDto.class);
//                studentDto.setRole(ObjectUtil.copyProperties(account.getRole(), new RoleDto(), RoleDto.class));
//                studentDto.setPhoneNumber(account.getPhoneNumber());
//                studentDto.setBirthday();
            }

            studentList.add(studentDto);
            return studentClass;
        }).collect(Collectors.toList());

        classStudentDto.setStudents(studentList);
        classStudentDtos.add(classStudentDto);
        return classStudentDtos;
    }

    @Override
    public ApiPage<ClassDto> searchClass(ClassSearchRequest query, Pageable pageable) {
        List<Long> classIds = query.getSubjectIds();
        ClassSpecificationBuilder builder = ClassSpecificationBuilder.specification()
                .query(query.getQ())
                .queryByClassStatus(query.getStatus())
                .queryByDate(query.getDateFrom(), query.getDateTo())

                .isActive(true)
                .queryByPriceBetween(query.getMinPrice(), query.getMaxPrice());
        if (!classIds.isEmpty()) {
            List<Subject> subjects = subjectRepository.findAllById(query.getSubjectIds());
            builder.querySubjectClass(subjects);
        }
        Page<Class> classes = classRepository.findAll(builder.build(), pageable);
        return PageUtil.convert(classes.map(aClass -> {
            ClassDto classDto = ObjectUtil.copyProperties(aClass, new ClassDto(), ClassDto.class);
            ClassLevel classLevel = aClass.getClassLevel();
            if (classLevel != null) {
                classDto.setClassLevel(classLevel.getCode());
            }


            if (aClass.getAccount() != null) {
                classDto.setTeacher(ConvertUtil.doConvertEntityToSimpleResponse(aClass.getAccount()));
            }
            if (aClass.getCourse() != null) {
                classDto.setCourse(ConvertUtil.doConvertCourseToCourseResponse(aClass.getCourse()));
            }
            return classDto;
        }));
    }


    @Override
    public ClassDto classDetail(Long id) {
        Class aClass = classRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay class" + id));
        ClassDto classDto = ConvertUtil.doConvertEntityToResponse(aClass);
        Account currentUser = securityUtil.getCurrentUser();
        if (Objects.equals(aClass.getStatus(), EClassStatus.RECRUITING)) {
            boolean isApplied = false;
            if (currentUser != null) {
                long count = aClass.getCandicates().stream().filter(classTeacherCandicate -> Objects.equals(classTeacherCandicate.getTeacher().getId(), currentUser.getId())).count();
                if (count >= 1) {
                    isApplied = true;
                }
            }
            classDto.setApplied(isApplied);
        } else {
            Boolean validClassForStudent = ForumUtil.isValidClassForStudent(currentUser, aClass);
            classDto.setEnrolled(validClassForStudent);
        }
        return classDto;
    }


    @Override
    public ApiPage<ClassDto> getAllClass(Pageable pageable) {
        Page<Class> classesPage = classRepository.findAllByIsActiveIsTrue(pageable);
        return PageUtil.convert(classesPage.map(ConvertUtil::doConvertEntityToResponse));
    }

    @Override
    public ApiPage<ClassDto> accountFilterClass(ClassSearchRequest query, Pageable pageable) {
        Account account = securityUtil.getCurrentUserThrowNotFoundException();
        List<Class> classAccount = null;
        List<Long> classId = new ArrayList<>();
        if (account.getRole().getCode().equals(EAccountRole.TEACHER)) {
            classAccount = classRepository.findByAccountAndStatus(account, query.getStatus());
        } else if (account.getRole().getCode().equals(EAccountRole.STUDENT)) {
            List<StudentClass> studentClasses = account.getStudentClasses();
            studentClasses.forEach(studentClass -> {
                Class aClass = studentClass.getaClass();
                classId.add(aClass.getId());

            });
            classAccount = classRepository.findByIdInAndStatus(classId, query.getStatus());
        }
        List<ClassDto> classDtoList = new ArrayList<>();
        classAccount.forEach(aClass -> {
            classDtoList.add(ObjectUtil.copyProperties(aClass, new ClassDto(), ClassDto.class));
        });

        Page<ClassDto> page = new PageImpl<>(classDtoList, pageable, classDtoList.size());

        return PageUtil.convert(page);

    }

    @Override
    public Long createClassForRecruiting(CreateRecruitingClassRequest createRecruitingClassRequest) throws JsonProcessingException, ParseException {
        // set class bên vườn đậu
        Class clazz = new Class();
        clazz.setName(createRecruitingClassRequest.getName());
        if (classRepository.existsByCode(createRecruitingClassRequest.getCode())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Mã lớp đã tồn tại"));
        }
        Instant now = DayUtil.convertDayInstant(Instant.now().toString());
        if (!DayUtil.checkTwoDateBigger(now.toString(), createRecruitingClassRequest.getClosingDate().toString(), 3)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Ngày đóng tuyển giáo viên phải sớm hơn ngày hiện tại là 3 ngày"));
        }
        if (!DayUtil.checkTwoDateBigger(createRecruitingClassRequest.getClosingDate().toString(), createRecruitingClassRequest.getStartDate().toString(), 3)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Ngày đóng tuyển giáo viên phải sớm hơn ngày bắt đầu lớp là 3 ngay"));
        }
        if (!DayUtil.checkTwoDateBigger(createRecruitingClassRequest.getStartDate().toString(), createRecruitingClassRequest.getEndDate().toString(), 30)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Ngày bắt đâu mở lơp phải sớm hơn ngày kêt thúc lớp la 30 ngay"));
        }

        Course course = courseRepository.findById(createRecruitingClassRequest.getCourseId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Course not found by id:" + createRecruitingClassRequest.getCourseId()));
        clazz.setCourse(course);
        clazz.setCode(createRecruitingClassRequest.getCode());
        clazz.setStartDate(DayUtil.convertDayInstant(createRecruitingClassRequest.getStartDate().toString()));
        clazz.setEndDate(DayUtil.convertDayInstant(createRecruitingClassRequest.getEndDate().toString()));
        clazz.setClosingDate(DayUtil.convertDayInstant(createRecruitingClassRequest.getClosingDate().toString()));
        clazz.setMinNumberStudent(createRecruitingClassRequest.getMinNumberStudent());
        clazz.setMaxNumberStudent(createRecruitingClassRequest.getMaxNumberStudent());
        clazz.setStatus(EClassStatus.RECRUITING);
        clazz.setStartDate(createRecruitingClassRequest.getStartDate());
        clazz.setEndDate(createRecruitingClassRequest.getEndDate());
        ClassLevel classLevel = classLevelRepository.findByCode(createRecruitingClassRequest.getClassLevel()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Course not found by id:" + createRecruitingClassRequest.getClassLevel()));
        clazz.setClassLevel(classLevel);
        clazz.setClassType(createRecruitingClassRequest.getClassType());
        clazz.setActive(false);
        clazz.setUnitPrice(createRecruitingClassRequest.getEachStudentPayPrice());
        Class save = classRepository.save(clazz);
        return save.getId();
    }

    @Override
    public Boolean adminEnrolStudentIntoClass(Long studentId, Long classId) throws JsonProcessingException {
        Account student = accountRepository.findById(studentId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy học sinh: " + studentId));

        Class clazz = classRepository.findById(classId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy lớp: " + classId));


        List<StudentClass> studentClasses = student.getStudentClasses();
        studentClasses.forEach(studentClass -> {
            if (studentClass.getaClass().equals(clazz)) {
                throw ApiException.create(HttpStatus.BAD_REQUEST)
                        .withMessage(messageUtil.getLocalMessage("Học sinh đã đăng ký học lớp này !"));
            }
        });

        StudentClass studentClass = new StudentClass();
        studentClass.setAClass(clazz);
        studentClass.setAccount(student);
        studentClass.setIs_enrolled(true);
        student.getStudentClasses().add(studentClass);
        accountRepository.save(student);
        moodleService.enrolUserToCourseMoodle(clazz, student);
        return true;
    }

    @Override
    public Boolean detectExpireRecruitingClass() {
        List<Class> allRecruitingClass = classRepository.findAllByStatus(EClassStatus.RECRUITING);

        for (Class recruitingClass : allRecruitingClass) {
            Instant now = Instant.now().truncatedTo(ChronoUnit.DAYS);
            Instant closingDate = recruitingClass.getClosingDate().truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS);
            try {
                if (now.equals(closingDate)) {
                    recruitingClass.setStatus(EClassStatus.PENDING);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        classRepository.saveAll(allRecruitingClass);
        return true;
    }

    @Override
    public ClassDto cancelPendingClass(Long classId) {
        Class clazz = classRepository.findById(classId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy lớp: " + classId));
        if (!Objects.equals(clazz.getStatus(), EClassStatus.PENDING)) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Lớp có trạng thái không hợp lệ: " + classId);
        }
        clazz.setStatus(EClassStatus.CANCEL);
        return ConvertUtil.doConvertEntityToResponse(clazz);
    }

    @Override
    public ClassDto adminRejectRequestCreateClass(Long id) {
        Class clazz = classRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("Lớp không tìm thấy") + id));
        if (!Objects.equals(clazz.getStatus(), EClassStatus.WAITING)) {
            clazz.setStatus(EClassStatus.REJECTED);
        }
        return ConvertUtil.doConvertEntityToResponse(clazz);
    }

    @Override
    public Long updateClassForRequesting(Long id, CreateRequestingClassRequest createRequestingClassRequest) throws ParseException {
        Class clazz = classRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("Lớp không tìm thấy") + id));
        Account teacher = securityUtil.getCurrentUserThrowNotFoundException();
        List<Class> teacherClass = teacher.getTeacherClass();

        Class save = null;
        for (Class aClass : teacherClass) {
            if (aClass.getId().equals(clazz.getId())) {
                if (!aClass.getStatus().equals(EClassStatus.REQUESTING) && !aClass.getStatus().equals(EClassStatus.EDITREQUEST)) {
                    throw ApiException.create(HttpStatus.BAD_REQUEST)
                            .withMessage(messageUtil.getLocalMessage("lớp không cho phép cập nhật"));
                }

                clazz.setName(createRequestingClassRequest.getName());
                if (classRepository.existsByCode(createRequestingClassRequest.getCode()) && !clazz.getCode().equals(createRequestingClassRequest.getCode())) {
                    throw ApiException.create(HttpStatus.BAD_REQUEST)
                            .withMessage(messageUtil.getLocalMessage("code đã tồn tại"));
                }
                Instant now = DayUtil.convertDayInstant(Instant.now().toString());
                if (createRequestingClassRequest.getStartDate() == null) {
                    throw ApiException.create(HttpStatus.BAD_REQUEST)
                            .withMessage(messageUtil.getLocalMessage("Bạn chưa chọn ngày mở lớp"));
                }
                if (createRequestingClassRequest.getEndDate() == null) {
                    throw ApiException.create(HttpStatus.BAD_REQUEST)
                            .withMessage(messageUtil.getLocalMessage("Bạn chưa chọn ngày đóng lớp"));
                }
                if (!DayUtil.checkTwoDateBigger(now.toString(), createRequestingClassRequest.getStartDate().toString(), 3)) {
                    throw ApiException.create(HttpStatus.BAD_REQUEST)
                            .withMessage(messageUtil.getLocalMessage("Ngày đóng tuyển giáo viên phải sớm hơn ngày hiện tại là 3 ngày"));
                }

                if (!DayUtil.checkTwoDateBigger(createRequestingClassRequest.getStartDate().toString(), createRequestingClassRequest.getEndDate().toString(), 30)) {
                    throw ApiException.create(HttpStatus.BAD_REQUEST)
                            .withMessage(messageUtil.getLocalMessage("Ngày bắt đâu mở lơp phải sớm hơn ngày kêt thúc lớp la 30 ngay"));
                }

                Course course = courseRepository.findById(createRequestingClassRequest.getCourseId())
                        .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Chủ đề không tìm thâấy" + createRequestingClassRequest.getCourseId()));
                clazz.setCourse(course);


                clazz.setCode(createRequestingClassRequest.getCode());
                clazz.setStartDate(DayUtil.convertDayInstant(createRequestingClassRequest.getStartDate().toString()));
                clazz.setEndDate(DayUtil.convertDayInstant(createRequestingClassRequest.getEndDate().toString()));
                clazz.setMinNumberStudent(createRequestingClassRequest.getMinNumberStudent());
                clazz.setMaxNumberStudent(createRequestingClassRequest.getMaxNumberStudent());
                clazz.setStatus(EClassStatus.REQUESTING);
                clazz.setStartDate(createRequestingClassRequest.getStartDate());
                clazz.setEndDate(createRequestingClassRequest.getEndDate());
                ClassLevel classLevel = classLevelRepository.findById(createRequestingClassRequest.getClassLevelId())
                        .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("class level not found by id:" + createRequestingClassRequest.getClassLevelId()));
                clazz.setClassLevel(classLevel);
                clazz.setClassType(createRequestingClassRequest.getClassType());
                clazz.setActive(false);
                clazz.setUnitPrice(createRequestingClassRequest.getEachStudentPayPrice());
                save = classRepository.save(clazz);
                return save.getId();
            }
        }


        return id;
    }

    @Override
    public Long updateClassForRecruiting(Long id, CreateRecruitingClassRequest createRecruitingClassRequest) throws ParseException {
        Class clazz = classRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("Lớp không tìm thấy") + id));
        if (!clazz.getStatus().equals(EClassStatus.RECRUITING)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("lớp không cho phép cập nhật"));
        }
        clazz.setName(createRecruitingClassRequest.getName());
        if (classRepository.existsByCode(createRecruitingClassRequest.getCode()) && !clazz.getCode().equals(createRecruitingClassRequest.getCode())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("code đã tồn tại"));
        }
        Instant now = DayUtil.convertDayInstant(Instant.now().toString());
        if (!DayUtil.checkTwoDateBigger(now.toString(), createRecruitingClassRequest.getClosingDate().toString(), 3)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Ngày đóng tuyển giáo viên phải sớm hơn ngày hiện tại là 3 ngày"));
        }
        if (!DayUtil.checkTwoDateBigger(createRecruitingClassRequest.getClosingDate().toString(), createRecruitingClassRequest.getStartDate().toString(), 3)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Ngày đóng tuyển giáo viên phải sớm hơn ngày bắt đầu lớp là 3 ngay"));
        }
        if (!DayUtil.checkTwoDateBigger(createRecruitingClassRequest.getStartDate().toString(), createRecruitingClassRequest.getEndDate().toString(), 30)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Ngày bắt đâu mở lơp phải sớm hơn ngày kêt thúc lớp la 30 ngay"));
        }

        Course course = courseRepository.findById(createRecruitingClassRequest.getCourseId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Chủ đề không tìm thâấy" + createRecruitingClassRequest.getCourseId()));
        clazz.setCourse(course);
        clazz.setCode(createRecruitingClassRequest.getCode());
        clazz.setStartDate(DayUtil.convertDayInstant(createRecruitingClassRequest.getStartDate().toString()));
        clazz.setEndDate(DayUtil.convertDayInstant(createRecruitingClassRequest.getEndDate().toString()));
        clazz.setMinNumberStudent(createRecruitingClassRequest.getMinNumberStudent());
        clazz.setMaxNumberStudent(createRecruitingClassRequest.getMaxNumberStudent());
        clazz.setStatus(EClassStatus.RECRUITING);
        clazz.setStartDate(createRecruitingClassRequest.getStartDate());
        clazz.setEndDate(createRecruitingClassRequest.getEndDate());
        ClassLevel classLevel = classLevelRepository.findByCode(createRecruitingClassRequest.getClassLevel())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("class level not found by id:" + createRecruitingClassRequest.getClassLevel()));
        clazz.setClassLevel(classLevel);
        clazz.setClassType(createRecruitingClassRequest.getClassType());
        clazz.setActive(false);
        clazz.setUnitPrice(createRecruitingClassRequest.getEachStudentPayPrice());
        Class save = classRepository.save(clazz);
        return save.getId();
    }

    @Override
    public ApiPage<ClassDto> classSuggestion(long infoFindTutorId, Pageable pageable) {
        InfoFindTutor infoFindTutor = infoFindTutorRepository.findById(infoFindTutorId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Chủ đề không tìm thấy" + infoFindTutorId));

        List<Long> idTeacher = infoFindTutor.getInfoFindTutorAccounts().stream().map(infoFindTutorAccount -> infoFindTutorAccount.getTeacher().getId()).collect(Collectors.toList());
        List<Account> teachers = accountRepository.findAllById(idTeacher);

        List<Long> idSubject = infoFindTutor.getInfoFindTutorSubjects().stream().map(infoFindTutorAccount -> infoFindTutorAccount.getSubject().getId()).collect(Collectors.toList());
        List<Subject> subjects = subjectRepository.findAllById(idSubject);


        ClassSpecificationBuilder builder = ClassSpecificationBuilder.specification()
                .queryLevelClass(infoFindTutor.getClassLevel())
//                .queryTeacherClass(teachers)
                .querySubjectClass(subjects);

        Page<Class> classesPage = classRepository.findAll(builder.build(), pageable);

        return PageUtil.convert(classesPage.map(ConvertUtil::doConvertEntityToResponse));
    }

//    @Override
//    public  List<MoodleClassResponse>  synchronizedClass() throws JsonProcessingException {
//        MoodleMasterDataRequest s1MasterDataRequest = new MoodleMasterDataRequest() ;
//        List<MoodleClassResponse> response  = moodleCourseRepository.getCourse(s1MasterDataRequest);
//        List<MoodleClassResponse> classList = new ArrayList<>() ;
//        List<MoodleClassResponse> listClassMoodle = response.stream().map(moodleClassResponse -> {
//            MoodleClassResponse moodleClassResponse1 = new MoodleClassResponse() ;
//            moodleClassResponse1.setId(moodleClassResponse.getId());
//            classList.add(moodleClassResponse1 );
//            return moodleClassResponse1 ;
//        }).collect(Collectors.toList()) ;
//        return classList;
//    }
//


    @Override
    public Boolean applyToRecruitingClass(Long classId) {
        Account teacher = securityUtil.getCurrentUserThrowNotFoundException();
        Class clazz = classRepository.findById(classId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy lớp: " + classId));
        List<ClassTeacherCandicate> candicates = clazz.getCandicates();
        boolean isContain = candicates.stream().anyMatch(candicate -> candicate.getTeacher().getId().equals(teacher.getId()));
        if (isContain) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage("Giáo viên đã apply vào lớp này!");
        } else {
            ClassTeacherCandicate classTeacherCandicate = new ClassTeacherCandicate();
            classTeacherCandicate.setTeacher(teacher);
            classTeacherCandicate.setClazz(clazz);
            classTeacherCandicate.setStatus(ECandicateStatus.APPLYING);
            candicates.add(classTeacherCandicate);
        }
        classRepository.save(clazz);
        return true;
    }

    @Override
    public AccountResponse chooseCandicateForClass(ClassCandicateRequest request) {
        Long classId = request.getClassId();
        Long teacherId = request.getTeacherId();
        Class clazz = classRepository.findById(classId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay class" + classId));
        Account teacher = accountRepository.findById(teacherId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay teacher" + teacherId));
        List<ClassTeacherCandicate> candidates = clazz.getCandicates();
        for (ClassTeacherCandicate classTeacherCandicate : candidates) {
            if (classTeacherCandicate.getTeacher().getId().equals(teacherId)) {
                classTeacherCandicate.setStatus(ECandicateStatus.SELECTED);
                clazz.setStatus(EClassStatus.RESPONSING);
                clazz.setAccount(teacher);
                TeachingConfirmation teachingConfirmation = createTeachingConfirmation(classTeacherCandicate);
                classTeacherCandicate.getTeachingConfirmations().add(teachingConfirmation);
            } else {
                classTeacherCandicate.setStatus(ECandicateStatus.CLOSED);
            }
        }

        /**TODO
         * Gửi mail cho giáo viên xác nhận giáo viên nhận lớp
         *
         * */


        classRepository.save(clazz);
        return ConvertUtil.doConvertEntityToResponse(teacher);
    }

    private TeachingConfirmation createTeachingConfirmation(ClassTeacherCandicate classTeacherCandicate) {
        TeachingConfirmation teachingConfirmation = new TeachingConfirmation();
        teachingConfirmation.setExpireDate(Instant.now().plus(3, ChronoUnit.DAYS)); // 3 ngày là thời hạn để trả lời
        teachingConfirmation.setCandidate(classTeacherCandicate);
        teachingConfirmation.setCode(UUID.randomUUID().toString());
        teachingConfirmation.setStatus(EConfirmStatus.WAITING);
        return teachingConfirmation;
    }

    @Override
    public ApiPage<CandicateResponse> getClassCandicate(Long classId, Pageable pageable) {
        Class clazz = classRepository.findById(classId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay class" + classId));
        Page<ClassTeacherCandicate> classCandicates = classTeacherCandicateRepository.findAllByClazz(clazz, pageable);
        return PageUtil.convert(classCandicates.map(ConvertUtil::doConvertEntityToResponse));
    }

    @Override
    public ApiPage<ClassDto> getRecruitingClasses(Pageable pageable) {
        Page<Class> classPages = classRepository.findAllByStatus(EClassStatus.RECRUITING, pageable);
        return PageUtil.convert(classPages.map(ConvertUtil::doConvertEntityToResponse));

    }

    @Override
    public ApiPage<ClassDto> getClassByAccount(ClassSearchRequest request, Pageable pageable) {
        Account account = securityUtil.getCurrentUserThrowNotFoundException();
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        Role role = account.getRole();

        ClassSpecificationBuilder builder = ClassSpecificationBuilder.specification()
                .query(request.getQ())
                .queryByClassStatus(request.getStatus());
        if (role != null) {
            if (role.getCode().equals(EAccountRole.STUDENT)) {
                builder.queryByStudent(account);
            }
            if (role.getCode().equals(EAccountRole.TEACHER)) {
                builder.queryTeachersClass(accounts);
            }
        }
        Page<Class> classPage = classRepository.findAll(builder.build(), pageable);

        return PageUtil.convert(classPage.map(ConvertUtil::doConvertEntityToResponse));
//        return PageUtil.convert(classesPage != null ? classesPage.map(ConvertUtil::doConvertEntityToResponse) : Page.empty());
    }

    @Override
    public ClassDetailDto accountGetClassDetail(Long id) {
        Account account = securityUtil.getCurrentUserThrowNotFoundException();
        Class aClass = null;
        Role role = account.getRole();

        if (role != null) {
            if (role.getCode().equals(EAccountRole.STUDENT)) {
                List<Class> classList = account.getStudentClasses().stream().map(StudentClass::getaClass).collect(Collectors.toList());
                List<Long> idsClass = classList.stream().map(Class::getId).collect(Collectors.toList());
                for (Long ids : idsClass) {
                    if (ids.equals(id)) {
                        aClass = classRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay class" + id));
                    }
                }

            } else if (role.getCode().equals(EAccountRole.TEACHER)) {
                aClass = classRepository.findByIdAndAccount(id, account);
            }
        }

        if (aClass == null) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Class không tìm thấy!!");
        }

        ClassDetailDto classDetail = ObjectUtil.copyProperties(aClass, new ClassDetailDto(), ClassDetailDto.class);

        classDetail.setEachStudentPayPrice(aClass.getUnitPrice());


        Course course = aClass.getCourse();
        if (course != null) {
            CourseDetailResponse courseDetailResponse = new CourseDetailResponse();
            courseDetailResponse = ObjectUtil.copyProperties(course, new CourseDetailResponse(), CourseDetailResponse.class, true);
            if (course.getResource() != null) {
                courseDetailResponse.setImage(course.getResource().getUrl());
            }
            courseDetailResponse.setActive(course.getIsActive());
            courseDetailResponse.setTitle(course.getTitle());

            // set subject
            Subject subject = course.getSubject();
            if (subject != null) {
                SubjectDto subjectDto = new SubjectDto();
                subjectDto.setId(subject.getId());
                subjectDto.setName(subject.getName());
                subjectDto.setCode(subject.getCode());
                courseDetailResponse.setSubject(subjectDto);
            }
            classDetail.setCourse(courseDetailResponse);
        }
//        if (aClass.getMoodleClassId() != null) {
//            GetMoodleCourseRequest getMoodleCourseRequest = new GetMoodleCourseRequest();
//
//            getMoodleCourseRequest.setCourseid(aClass.getMoodleClassId());
//            try {
//
//
//                List<MoodleRecourseDtoResponse> resources = new ArrayList<>();
//
//                List<MoodleSectionResponse> resourceCourse = moodleCourseRepository.getResourceCourse(getMoodleCourseRequest);
//
//
//                resourceCourse.stream().skip(1).forEach(moodleRecourseClassResponse -> {
//                    MoodleRecourseDtoResponse recourseDtoResponse = new MoodleRecourseDtoResponse();
//                    recourseDtoResponse.setId(moodleRecourseClassResponse.getId());
//                    recourseDtoResponse.setName(moodleRecourseClassResponse.getName());
//                    List<MoodleModuleResponse> modules = moodleRecourseClassResponse.getModules();
//
//                    List<MoodleResourceResponse> moodleResourceResponseList = new ArrayList<>();
//
//                    modules.forEach(moodleResponse -> {
//                        MoodleResourceResponse moodleResourceResponse = new MoodleResourceResponse();
//                        moodleResourceResponse.setId(moodleResponse.getId());
//                        moodleResourceResponse.setUrl(moodleResponse.getUrl());
//                        moodleResourceResponse.setName(moodleResponse.getName());
//                        moodleResourceResponse.setType(moodleResponse.getModname());
//                        moodleResourceResponseList.add(moodleResourceResponse);
//                    });
//                    recourseDtoResponse.setModules(moodleResourceResponseList);
//                    resources.add(recourseDtoResponse);
//                });
//                classDetail.setResources(resources);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }


        AccountResponse accountResponse = ObjectUtil.copyProperties(account, new AccountResponse(), AccountResponse.class);
        accountResponse.setRole(ObjectUtil.copyProperties(account.getRole(), new RoleDto(), RoleDto.class));
        Resource resource = account.getResource();
        if (resource != null) {
            accountResponse.setAvatar(resource.getUrl());
        }
        classDetail.setTeacher(accountResponse);


        List<Account> studentList = aClass.getStudentClasses().stream().map(StudentClass::getAccount).collect(Collectors.toList());

        List<AccountResponse> accountResponses = new ArrayList<>();
        studentList.stream().map(studentMap -> {
            AccountResponse student = ObjectUtil.copyProperties(studentMap, new AccountResponse(), AccountResponse.class);
            student.setRole(ObjectUtil.copyProperties(studentMap.getRole(), new RoleDto(), RoleDto.class));
            if (studentMap.getResource() != null) {
                student.setAvatar(studentMap.getResource().getUrl());
            }

            accountResponses.add(student);
            return studentMap;
        }).collect(Collectors.toList());
        classDetail.setStudents(accountResponses);

        List<TimeTableDto> timeTableDtoList = new ArrayList<>();

        List<TimeTable> timeTables = aClass.getTimeTables();
        timeTables.forEach(timeTable -> {
            TimeTableDto timeTableDto = new TimeTableDto();
            timeTableDto.setId(timeTable.getId());
            timeTableDto.setDate(timeTable.getDate());
            timeTableDto.setSlotNumber(timeTable.getSlotNumber());
            ArchetypeTimeDto archetypeTimeDto = new ArchetypeTimeDto();
            ArchetypeTime archetypeTime = timeTable.getArchetypeTime();
            if (archetypeTime != null) {
                Slot slot = archetypeTime.getSlot();
                if (slot != null) {
                    archetypeTimeDto.setSlot(ObjectUtil.copyProperties(slot, new SlotDto(), SlotDto.class));
                }
                DayOfWeek dayOfWeek = archetypeTime.getDayOfWeek();
                if (dayOfWeek != null) {
                    archetypeTimeDto.setDayOfWeek(ObjectUtil.copyProperties(dayOfWeek, new DayOfWeekDto(), DayOfWeekDto.class));
                }
            }


            timeTableDto.setArchetypeTime(archetypeTimeDto);
            timeTableDtoList.add(timeTableDto);

        });
        classDetail.setTimeTable(timeTableDtoList);
        return classDetail;

    }

    public Class findClassByRoleAccount(Long id) {
        Account account = securityUtil.getCurrentUserThrowNotFoundException();
        Class aClass = null;
        Role role = account.getRole();
        if (role != null) {
            if (role.getCode().equals(EAccountRole.STUDENT)) {
                List<Class> classList = account.getStudentClasses().stream().map(StudentClass::getaClass).collect(Collectors.toList());
                List<Long> idsClass = classList.stream().map(Class::getId).collect(Collectors.toList());
                for (Long ids : idsClass) {
                    if (ids.equals(id)) {
                        aClass = classRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay class" + id));
                    }
                }

            } else if (role.getCode().equals(EAccountRole.TEACHER)) {
                aClass = classRepository.findByIdAndAccount(id, account);
            }
        }
        if (aClass == null) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Lớp bạn đang tìm kiếm không tồn tại");
        }
        return aClass;
    }

    @Override
    public ClassResourcesResponse accountGetResourceOfClass(Long id) {

        Class aClass = findClassByRoleAccount(id);

        ClassResourcesResponse classResourcesResponse = new ClassResourcesResponse();

        if (aClass.getMoodleClassId() != null) {
            GetMoodleCourseRequest getMoodleCourseRequest = new GetMoodleCourseRequest();

            getMoodleCourseRequest.setCourseid(aClass.getMoodleClassId());
            try {

                List<MoodleRecourseDtoResponse> resources = new ArrayList<>();

                List<MoodleSectionResponse> resourceCourse = moodleCourseRepository.getResourceCourse(getMoodleCourseRequest);


                resourceCourse.stream().skip(1).forEach(moodleRecourseClassResponse -> {
                    MoodleRecourseDtoResponse recourseDtoResponse = new MoodleRecourseDtoResponse();
                    recourseDtoResponse.setId(moodleRecourseClassResponse.getId());
                    recourseDtoResponse.setName(moodleRecourseClassResponse.getName());
                    List<MoodleModuleResponse> modules = moodleRecourseClassResponse.getModules();

                    List<MoodleResourceResponse> moodleResourceResponseList = new ArrayList<>();

                    modules.forEach(moodleResponse -> {
                        MoodleResourceResponse moodleResourceResponse = new MoodleResourceResponse();
                        moodleResourceResponse.setId(moodleResponse.getId());
                        moodleResourceResponse.setUrl(moodleResponse.getUrl());
                        moodleResourceResponse.setName(moodleResponse.getName());
                        moodleResourceResponse.setType(moodleResponse.getModname());
                        moodleResourceResponseList.add(moodleResourceResponse);
                    });
                    recourseDtoResponse.setModules(moodleResourceResponseList);
                    resources.add(recourseDtoResponse);
                });
                classResourcesResponse.setResources(resources);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        return classResourcesResponse;
    }

    @Override
    public ApiPage<AccountResponse> accountGetStudentOfClass(Long id, Pageable pageable) {
        Class aClass = classRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay class" + id));


        ClassStudentResponse classStudentResponse = new ClassStudentResponse();

        List<Account> studentList = aClass.getStudentClasses().stream().map(StudentClass::getAccount).collect(Collectors.toList());

        List<AccountResponse> accountResponses = new ArrayList<>();
        studentList.forEach(studentMap -> {
            AccountResponse accountResponse = ConvertUtil.doConvertEntityToResponse(studentMap);
//            AccountResponse student = ObjectUtil.copyProperties(studentMap, new AccountResponse(), AccountResponse.class);
            accountResponse.setRole(ObjectUtil.copyProperties(studentMap.getRole(), new RoleDto(), RoleDto.class));
            if (studentMap.getResource() != null) {
                accountResponse.setAvatar(studentMap.getResource().getUrl());
            }

            accountResponses.add(accountResponse);
        });
        Page<AccountResponse> page = new PageImpl<>(accountResponses);


        return PageUtil.convert(page);
    }

    @Override
    public List<ClassTimeTableResponse> accountGetTimeTableOfClass(Long id) {
        Class aClass = findClassByRoleAccount(id);
        Account account = securityUtil.getCurrentUserThrowNotFoundException();


        List<TimeTable> timeTables = aClass.getTimeTables();

        List<ClassTimeTableResponse> classTimeTableResponseList = new ArrayList<>();


        timeTables.forEach(timeTable -> {
            ClassTimeTableResponse classTimeTableResponse = new ClassTimeTableResponse();
            classTimeTableResponse.setId(timeTable.getId());
            classTimeTableResponse.setDate(timeTable.getDate());
            classTimeTableResponse.setSlotNumber(timeTable.getSlotNumber());


            ArchetypeTime archetypeTime = timeTable.getArchetypeTime();
            if (archetypeTime != null) {
                Archetype archetype = archetypeTime.getArchetype();
                if (archetype != null) {
                    classTimeTableResponse.setArchetypeCode(archetype.getCode());
                    classTimeTableResponse.setArchetypeCName(archetype.getName());
                }
                Slot slot = archetypeTime.getSlot();
                if (slot != null) {
                    classTimeTableResponse.setSlotName(slot.getName());
                    classTimeTableResponse.setSlotCode(slot.getCode());
                    classTimeTableResponse.setStartTime(slot.getStartTime());
                    classTimeTableResponse.setEndTime(slot.getEndTime());
                }
                DayOfWeek dayOfWeek = archetypeTime.getDayOfWeek();
                if (dayOfWeek != null) {
                    classTimeTableResponse.setDayOfWeekName(dayOfWeek.getName());
                    classTimeTableResponse.setDayOfWeekCode(dayOfWeek.getCode());
                }
            }
            classTimeTableResponseList.add(classTimeTableResponse);
        });

        return classTimeTableResponseList;
    }

    @Override
    public ClassAttendanceResponse accountGetAttendanceOfClass(Long id) {
        Account account = securityUtil.getCurrentUserThrowNotFoundException();
        Class aClass = findClassByRoleAccount(id);

        StudentClassKey studentClassKey = new StudentClassKey();
        studentClassKey.setStudentId(account.getId());
        studentClassKey.setClassId(aClass.getId());
        List<Attendance> attendanceList = null;
        if (account.getRole().getCode().equals(EAccountRole.STUDENT)) {
            attendanceList = attendanceRepository.findAllByStudentClassKeyId(studentClassKey);
        } else if (account.getRole().getCode().equals(EAccountRole.TEACHER)) {
            if (aClass.getAccount().getId().equals(account.getId())) {
                if (aClass.getTimeTables() != null) {
                    List<TimeTable> timeTables = aClass.getTimeTables();
                    attendanceList = attendanceRepository.findAllByTimeTableIn(timeTables);
                }

            }
        }


        ClassAttendanceResponse classAttendanceResponse = new ClassAttendanceResponse();

        classAttendanceResponse.setAccountId(account.getId());
        classAttendanceResponse.setClassId(aClass.getId());

        List<AttendanceDto> attendanceDtoList = new ArrayList<>();
        attendanceList.forEach(attendance -> {
            AttendanceDto attendanceDto = new AttendanceDto();
            attendanceDto.setId(attendance.getId());
            attendanceDto.setPresent(attendance.getPresent());

            TimeTable timeTable = attendance.getTimeTable();
            if (timeTable != null) {
                attendanceDto.setTimeTableId(timeTable.getId());
                attendanceDto.setDate(timeTable.getDate());
                attendanceDto.setSlotNumber(timeTable.getSlotNumber());
                ArchetypeTime archetypeTime = timeTable.getArchetypeTime();
                if (archetypeTime != null) {
                    if (archetypeTime.getSlot() != null) {
                        attendanceDto.setSlotCode(archetypeTime.getSlot().getCode());
                        attendanceDto.setSlotName(archetypeTime.getSlot().getName());
                        Slot slot = archetypeTime.getSlot();
                        attendanceDto.setStartTime(slot.getStartTime());
                        attendanceDto.setEndTime(slot.getEndTime());
                    }
                    if (archetypeTime.getDayOfWeek() != null) {
                        attendanceDto.setDowCode(archetypeTime.getDayOfWeek().getCode());
                        attendanceDto.setDowName(archetypeTime.getDayOfWeek().getName());
                    }

                    Archetype archetype = archetypeTime.getArchetype();
                    if (archetype != null) {
                        attendanceDto.setArchetypeCode(archetype.getCode());
                        attendanceDto.setArchetypeName(archetype.getName());
                    }
                }
            }
            attendanceDtoList.add(attendanceDto);
        });
        classAttendanceResponse.setAttendance(attendanceDtoList);
        return classAttendanceResponse;
    }

    @Override
    public ApiPage<ClassDto> adminGetClass(EClassStatus status, Pageable pageable) {
        Page<Class> allByStatus = null;
        if (status.equals(EClassStatus.All)) {
            allByStatus = classRepository.findAll(pageable);
        } else {
            allByStatus = classRepository.findAllByStatus(status, pageable);
        }
        return PageUtil.convert(allByStatus != null ? allByStatus.map(ConvertUtil::doConvertEntityToResponse) : null);
    }


    @Override
    public ClassTeacherResponse studentGetTeacherInfoOfClass(Long id) {
        Class aClass = findClassByRoleAccount(id);
        Account account = aClass.getAccount();
        ClassTeacherResponse classTeacherResponse = new ClassTeacherResponse();
        if (account != null) {
            AccountResponse accountResponse = ObjectUtil.copyProperties(account, new AccountResponse(), AccountResponse.class);
            AccountDetail accountDetail = account.getAccountDetail();
            if (accountDetail != null) {
                EGenderType gender = accountDetail.getGender();
                if (gender != null) {
                    GenderResponse genderResponse = new GenderResponse();
                    genderResponse.setCode(gender.name());
                    genderResponse.setName(gender.getLabel());
                    accountResponse.setGender(genderResponse);
                }
                accountResponse.setFirstName(accountDetail.getFirstName());
                accountResponse.setLastName(accountDetail.getLastName());
                accountResponse.setPhoneNumber(accountDetail.getPhone());
                accountResponse.setEmail(accountDetail.getEmail());
                accountResponse.setBirthday(accountDetail.getBirthDay());

                if (account.getResource() != null) {
                    accountResponse.setAvatar(account.getResource().getUrl());
                }

                accountResponse.setVoice(ConvertUtil.doConvertVoiceToResponse(accountDetail.getVoice()));
                accountResponse.setCurrentAddress(accountDetail.getCurrentAddress());
                accountResponse.setIdCard(accountDetail.getIdCard());
                accountResponse.setSchoolName(accountDetail.getTrainingSchoolName());
                accountResponse.setMajors(accountDetail.getMajors());
                accountResponse.setStatus(accountDetail.getStatus());
                accountResponse.setLevel(accountDetail.getLevel());
                accountResponse.setActive(accountDetail.getActive());

            }
            accountResponse.setRole(ObjectUtil.copyProperties(account.getRole(), new RoleDto(), RoleDto.class));
            Resource resource = account.getResource();
            if (resource != null) {
                accountResponse.setAvatar(resource.getUrl());
            }
            classTeacherResponse.setTeacher(accountResponse);
        }
        return classTeacherResponse;
    }


    @Override
    public ApiPage<ClassDto> getAllClassForUser(Pageable pageable, GuestSearchClassRequest guestSearchClassRequest) {

        ClassSpecificationBuilder builder = new ClassSpecificationBuilder();
        builder.queryByClassStatus(guestSearchClassRequest.getStatus());
        builder.queryByClassType(guestSearchClassRequest.getClassType());
        builder.queryBySubject(guestSearchClassRequest.getSubject());

        Specification<Class> classSpecification = builder.build();
        Page<Class> classesPage = classRepository.findAll(classSpecification, pageable);
        return PageUtil.convert(classesPage.map(ConvertUtil::doConvertEntityToResponse));

    }

    private Forum createClassForum(Class clazz) {
        Forum forum = new Forum();
        forum.setName(clazz.getName());
        forum.setCode(clazz.getCode());
        forum.setType(EForumType.CLASS);
        Course course = clazz.getCourse();
        if (course != null) {
            Subject subject = course.getSubject();
            forum.setSubject(subject);
        }
        List<Section> sections = clazz.getSections();
        List<ForumLesson> forumLessons = new ArrayList<>();
        for (Section section : sections) {
            for (Module module : section.getModules()) {
                if (module.getType().name().equals(EModuleType.LESSON.name())) {
                    ForumLesson forumLesson = new ForumLesson();
                    forumLesson.setLessonName(module.getName());
                    forumLesson.setForum(forum);
                    forumLessons.add(forumLesson);
                }
            }
        }
        forum.getForumLessons().addAll(forumLessons);
        return forum;
    }

    @Override
    public ClassDto confirmAppreciation(Long id) throws JsonProcessingException {
        Class clazz = classRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                .withMessage("Class not found by id:" + id));
        Account teacher = securityUtil.getCurrentUserThrowNotFoundException();
        Account teacherClass = Optional.ofNullable(clazz.getAccount()).orElseThrow(() -> ApiException.create(HttpStatus.CONFLICT)
                .withMessage("Class dont have teacher!"));
        if (!teacherClass.getId().equals(teacher.getId())) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage("You are not teacher of the class!!");
        }
        clazz.setStatus(EClassStatus.WAITING);
        moodleService.synchronizedClassDetailFromMoodle(clazz);
        return ConvertUtil.doConvertEntityToResponse(clazz);
    }

    @Override
    public List<ClassDto> getClassByAccountAsList(EClassStatus status) {
        Account account = securityUtil.getCurrentUserThrowNotFoundException();

        List<Class> choosenClassList = null;
        Role role = account.getRole();

        if (role != null) {
            if (role.getCode().equals(EAccountRole.STUDENT)) {
                List<Class> classes = new ArrayList<>();
                List<Class> classList = account.getStudentClasses().stream().map(StudentClass::getaClass)
                        .filter(Class::isActive)
                        .collect(Collectors.toList());
                if (status.equals(EClassStatus.All)) {
                    choosenClassList = classList;
                } else {

                    classList.forEach(aClass -> {
                        if (aClass.getStatus().equals(status)) {
                            classes.add(aClass);
                        }
                    });
                    choosenClassList = classes;
                }

            } else if (role.getCode().equals(EAccountRole.TEACHER)) {

                if (status == null || status.equals(EClassStatus.All)) {
                    choosenClassList = classRepository.findAllByAccount(account);
                } else {
                    choosenClassList = classRepository.findAllByAccountAndStatus(account, status);
                }

            }
        }
        if (choosenClassList == null) {
            return Collections.emptyList();
        }
        return choosenClassList.stream().map(ConvertUtil::doConvertEntityToResponse).collect(Collectors.toList());
    }

    public ClassDetailDto classDetail_v2(Long id) throws JsonProcessingException {
        Class aClass = classRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay class" + id));
        ClassDetailDto classDetail = ObjectUtil.copyProperties(aClass, new ClassDetailDto(), ClassDetailDto.class);

        classDetail.setEachStudentPayPrice(aClass.getUnitPrice());


        classDetail.setFinalPrice(aClass.getUnitPrice());

        Course course = aClass.getCourse();
        if (course != null) {
            CourseDetailResponse courseDetailResponse = new CourseDetailResponse();
            courseDetailResponse = ObjectUtil.copyProperties(course, new CourseDetailResponse(), CourseDetailResponse.class, true);
            if (course.getResource() != null) {
                courseDetailResponse.setImage(course.getResource().getUrl());
            }
            courseDetailResponse.setActive(course.getIsActive());
            courseDetailResponse.setTitle(course.getTitle());

            // set subject
            Subject subject = course.getSubject();
            if (subject != null) {
                SubjectDto subjectDto = new SubjectDto();
                subjectDto.setId(subject.getId());
                subjectDto.setName(subject.getName());
                subjectDto.setCode(subject.getCode());
                courseDetailResponse.setSubject(subjectDto);
            }
            classDetail.setCourse(courseDetailResponse);


        }

        if (aClass.getMoodleClassId() != null) {
            GetMoodleCourseRequest getMoodleCourseRequest = new GetMoodleCourseRequest();
            getMoodleCourseRequest.setCourseid(aClass.getMoodleClassId());
            try {
                List<MoodleRecourseDtoResponse> resources = new ArrayList<>();

                List<MoodleSectionResponse> resourceCourse = moodleCourseRepository.getResourceCourse(getMoodleCourseRequest);


                resourceCourse.stream().skip(1).forEach(moodleRecourseClassResponse -> {
                    MoodleRecourseDtoResponse recourseDtoResponse = new MoodleRecourseDtoResponse();
                    recourseDtoResponse.setId(moodleRecourseClassResponse.getId());
                    recourseDtoResponse.setName(moodleRecourseClassResponse.getName());
                    List<MoodleModuleResponse> modules = moodleRecourseClassResponse.getModules();

                    List<MoodleResourceResponse> moodleResourceResponseList = new ArrayList<>();

                    modules.forEach(moodleResponse -> {
                        MoodleResourceResponse moodleResourceResponse = new MoodleResourceResponse();
                        moodleResourceResponse.setId(moodleResponse.getId());
                        moodleResourceResponse.setUrl(moodleResponse.getUrl());
                        moodleResourceResponse.setName(moodleResponse.getName());
                        moodleResourceResponse.setType(moodleResponse.getModname());
                        moodleResourceResponseList.add(moodleResourceResponse);
                    });
                    recourseDtoResponse.setModules(moodleResourceResponseList);
                    resources.add(recourseDtoResponse);
                });
                classDetail.setResources(resources);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        Account account = aClass.getAccount();
        if (account != null) {
            AccountResponse accountResponse = ObjectUtil.copyProperties(account, new AccountResponse(), AccountResponse.class);
            accountResponse.setRole(ObjectUtil.copyProperties(account.getRole(), new RoleDto(), RoleDto.class));
            Resource resource = account.getResource();
            if (resource != null) {
                accountResponse.setAvatar(resource.getUrl());
            }
            classDetail.setTeacher(accountResponse);
        }


        List<Account> studentList = aClass.getStudentClasses().stream().map(StudentClass::getAccount).collect(Collectors.toList());

        List<AccountResponse> accountResponses = new ArrayList<>();
        studentList.stream().map(studentMap -> {
            AccountResponse student = ObjectUtil.copyProperties(studentMap, new AccountResponse(), AccountResponse.class);
            student.setRole(ObjectUtil.copyProperties(studentMap.getRole(), new RoleDto(), RoleDto.class));
            if (studentMap.getResource() != null) {
                student.setAvatar(studentMap.getResource().getUrl());
            }

            accountResponses.add(student);
            return studentMap;
        }).collect(Collectors.toList());
        classDetail.setStudents(accountResponses);

        List<TimeTableDto> timeTableDtoList = new ArrayList<>();

        List<TimeTable> timeTables = aClass.getTimeTables();
        timeTables.stream().map(timeTable -> {
            TimeTableDto timeTableDto = new TimeTableDto();
            timeTableDto.setId(timeTable.getId());
            timeTableDto.setDate(timeTable.getDate());
            timeTableDto.setSlotNumber(timeTable.getSlotNumber());
            ArchetypeTimeDto archetypeTimeDto = new ArchetypeTimeDto();
            ArchetypeTime archetypeTime = timeTable.getArchetypeTime();
            if (archetypeTime != null) {
                Slot slot = archetypeTime.getSlot();
                if (slot != null) {
                    archetypeTimeDto.setSlot(ObjectUtil.copyProperties(slot, new SlotDto(), SlotDto.class));
                }
                DayOfWeek dayOfWeek = archetypeTime.getDayOfWeek();
                if (dayOfWeek != null) {
                    archetypeTimeDto.setDayOfWeek(ObjectUtil.copyProperties(dayOfWeek, new DayOfWeekDto(), DayOfWeekDto.class));
                }
            }


            timeTableDto.setArchetypeTime(archetypeTimeDto);
            timeTableDtoList.add(timeTableDto);
            return timeTable;
        }).collect(Collectors.toList());
        classDetail.setTimeTable(timeTableDtoList);
        return classDetail;
    }

    public Boolean confirmTeaching(String confirmCode) throws JsonProcessingException {
        TeachingConfirmation confirmation = teachingConfirmationRepository.findByCode(confirmCode);
        Account teacher = confirmation.getCandidate().getTeacher();
        Account currentAccount = securityUtil.getCurrentUserThrowNotFoundException();
        if (!Objects.equals(teacher.getId(), currentAccount.getId())) {
            throw ApiException.create(HttpStatus.METHOD_NOT_ALLOWED).withMessage("Xác nhận không hợp lệ!");
        }
        Instant now = Instant.now().truncatedTo(ChronoUnit.DAYS);
        Instant expire = confirmation.getExpireDate().truncatedTo(ChronoUnit.DAYS);
        if (now.isAfter(expire)) {
            throw ApiException.create(HttpStatus.METHOD_NOT_ALLOWED).withMessage("Xác nhận đã hết hạn!");
        }
        Class clazz = confirmation.getCandidate().getClazz();
        EClassStatus status = clazz.getStatus();

        if (!Objects.equals(status, EClassStatus.RECRUITING)) {
            throw ApiException.create(HttpStatus.METHOD_NOT_ALLOWED).withMessage("Lớp đã thay đổi, vui lòng liên hệ quản lý để được hỗ trợ!");
        }

        confirmation.setIsAccept(true);
        clazz.setStatus(EClassStatus.NOTSTART);
        classRepository.save(clazz);
        teachingConfirmationRepository.save(confirmation);
        moodleService.enrolUserToCourseMoodle(clazz, teacher);
        // Link để điều hướng màn hình xác nhận qua front end
        // Để get link này gửi email dùng phương thức .toString()
        StringBuilder fullConfirmLink = new StringBuilder(confirmLink);
        fullConfirmLink.append("?code=");
        fullConfirmLink.append(confirmCode);

        /**TODO
         * Gửi email từ chối cho tất cả các ứng viên khác
         * */
        return true;
    }

    public Boolean detectExpireConfirmation() {
        List<Class> modifyClasses = new ArrayList<>();
        List<TeachingConfirmation> teachingConfirmations = teachingConfirmationRepository.findByStatus(EConfirmStatus.WAITING);
        List<TeachingConfirmation> expireConfirmations = teachingConfirmations.stream().filter(teachingConfirmation -> {
            Instant now = Instant.now().truncatedTo(ChronoUnit.DAYS);
            Instant expire = teachingConfirmation.getExpireDate().truncatedTo(ChronoUnit.DAYS);
            return now.isAfter(expire);
        }).collect(Collectors.toList());

        for (TeachingConfirmation expirteConfirmation : expireConfirmations) {
            expirteConfirmation.setStatus(EConfirmStatus.REFUSED);
            Class clazz = expirteConfirmation.getCandidate().getClazz();
            clazz.setStatus(EClassStatus.RECRUITING);
            modifyClasses.add(clazz);
        }

        if (!modifyClasses.isEmpty()) {
            classRepository.saveAll(modifyClasses);
            detectExpireRecruitingClass();
        }
        teachingConfirmationRepository.saveAll(expireConfirmations);
        return true;
    }


}