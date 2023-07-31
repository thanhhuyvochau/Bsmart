
package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.constant.EActivityType;
import fpt.project.bsmart.entity.constant.ECourseActivityType;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.entity.dto.ActivityDto;
import fpt.project.bsmart.entity.request.CourseSearchRequest;
import fpt.project.bsmart.entity.request.CreateCourseRequest;
import fpt.project.bsmart.entity.request.ManagerApprovalCourseRequest;
import fpt.project.bsmart.entity.response.CourseResponse;
import fpt.project.bsmart.entity.response.course.CompletenessCourseResponse;
import fpt.project.bsmart.entity.response.course.ManagerGetCourse;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.ICourseService;
import fpt.project.bsmart.util.*;
import fpt.project.bsmart.util.specification.CourseSpecificationBuilder;
import fpt.project.bsmart.validator.CourseValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static fpt.project.bsmart.entity.constant.ECourseStatus.*;
import static fpt.project.bsmart.util.Constants.ErrorMessage.*;
import static fpt.project.bsmart.util.CourseUtil.checkCourseOwnership;


@Service
public class CourseServiceImpl implements ICourseService {

    private final CourseRepository courseRepository;

    private final MessageUtil messageUtil;

    private final CategoryRepository categoryRepository;

    private final ActivityRepository activityRepository;

    private final ClassRepository classRepository;

    private final ActivityHistoryRepository activityHistoryRepository;


    public CourseServiceImpl(CourseRepository courseRepository, MessageUtil messageUtil, CategoryRepository categoryRepository, ActivityRepository activityRepository, ClassRepository classRepository, ActivityHistoryRepository activityHistoryRepository) {
        this.courseRepository = courseRepository;
        this.messageUtil = messageUtil;
        this.categoryRepository = categoryRepository;
        this.activityRepository = activityRepository;
        this.classRepository = classRepository;
        this.activityHistoryRepository = activityHistoryRepository;
    }


    @Override
    public ApiPage<CourseResponse> getCourseForCoursePage(CourseSearchRequest query, Pageable pageable) {

        List<Class> byStatus = classRepository.findByStatus(NOTSTART);
        List<Long> classIds = byStatus.stream().map(Class::getId).collect(Collectors.toList());
        CourseSpecificationBuilder builder = CourseSpecificationBuilder.specifications()
                .queryLike(query.getQ())
//                .queryByClassId(classIds)
                .queryBySubjectId(query.getSubjectId())
                .queryByCategoryId(query.getCategoryId());

        Page<Course> coursesPage = courseRepository.findAll(builder.build(), pageable);

        return PageUtil.convert(coursesPage.map(ConvertUtil::convertCourseCourseResponsePage));
    }

    public ApiPage<CourseResponse> studentGetCurrentCourse(CourseSearchRequest request, Pageable pageable) {
        User user = SecurityUtil.getCurrentUser();
        CourseSpecificationBuilder builder = CourseSpecificationBuilder.specifications()
                .queryLike(request.getQ())
                .queryByCourseStatus(STARTING)
                .queryStudentCurrentCourse(user.getId())
                .queryBySubjectId(request.getSubjectId())
                .queryByCategoryId(request.getCategoryId());
        Page<Course> coursePage = courseRepository.findAll(builder.build(), pageable);
        return PageUtil.convert(coursePage.map(ConvertUtil::convertCourseCourseResponsePage));
    }

    @Override
    public Long mentorCreateCourse(CreateCourseRequest createCourseRequest) {

        User currentUserAccountLogin = SecurityUtil.getCurrentUser();

        Long categoryId = createCourseRequest.getCategoryId();
        if (categoryId == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(PLEASE_SELECT_THE_CATEGORY_FOR_THE_COURSE));
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(CATEGORY_NOT_FOUND_BY_ID) + categoryId));

        Long subjectId = createCourseRequest.getSubjectId();
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
        course.setName(createCourseRequest.getName());
        course.setCode(CourseUtil.generateRandomCode(8));
        course.setDescription(createCourseRequest.getDescription());
        course.setSubject(subject);
        course.setLevel(createCourseRequest.getLevel());
        course.setStatus(REQUESTING);
        course.setCreator(currentUserAccountLogin);
        Course courseSaved = courseRepository.save(course);
        return courseSaved.getId();
    }

    @Override
    public Long mentorUpdateCourse(Long id, CreateCourseRequest createCourseRequest) {
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));

        if (!course.getStatus().equals(REQUESTING) && !course.getStatus().equals(EDITREQUEST)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(COURSE_STATUS_NOT_ALLOW));
        }

        Long categoryId = createCourseRequest.getCategoryId();
        if (categoryId == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(PLEASE_SELECT_THE_CATEGORY_FOR_THE_COURSE));
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(CATEGORY_NOT_FOUND_BY_ID) + categoryId));

        Long subjectId = createCourseRequest.getSubjectId();
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

        checkCourseOwnership(course, currentUserAccountLogin);
        course.setName(createCourseRequest.getName());
        course.setDescription(createCourseRequest.getDescription());
        course.setSubject(subject);
        course.setLevel(createCourseRequest.getLevel());
        course.setStatus(REQUESTING);
        return courseRepository.save(course).getId();
    }

    @Override
    public ApiPage<CourseResponse> getCourseOfMentor(CourseSearchRequest query, Pageable pageable) {
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();

        CourseSpecificationBuilder builder = CourseSpecificationBuilder.specifications()
                .queryLike(query.getQ())
                .queryByCourseStatus(query.getStatus())
                .queryBySubjectId(query.getSubjectId())
                .queryByCreatorId(currentUserAccountLogin.getId())
                .queryByCategoryId(query.getCategoryId());

        Page<Course> coursesPage = courseRepository.findAll(builder.build(), pageable);
        return PageUtil.convert(coursesPage.map(ConvertUtil::convertCourseCourseResponsePage));
    }

    @Override
    public Boolean mentorDeleteCourse(Long id) {
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));
        if (!course.getCreator().equals(currentUserAccountLogin)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(COURSE_DOES_NOT_BELONG_TO_THE_TEACHER));
        }
        if (!course.getStatus().equals(REQUESTING) && !course.getStatus().equals(EDITREQUEST)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(COURSE_STATUS_NOT_ALLOW));
        }
        List<Class> classes = course.getClasses();
        List<ECourseStatus> statusClasses = classes.stream().map(Class::getStatus).collect(Collectors.toList());
        if (statusClasses.contains(ECourseStatus.NOTSTART) || statusClasses.contains(STARTING) || statusClasses.contains(WAITING)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(CLASSES_ARE_CURRENTLY_STARTING_FROM_THIS_COURSE_CANNOT_DELETE));
        }
        List<Activity> activities = course.getActivities();
        activityRepository.deleteAll(activities);
        courseRepository.delete(course);
        return true;
    }

    @Override
    public ApiPage<ManagerGetCourse> coursePendingToApprove(ECourseStatus status, Pageable pageable) {

        if (status.equals(WAITING)) {
            List<Class> byStatus = classRepository.findByStatus(WAITING);
            List<Course> courseList = byStatus.stream().map(aClass -> aClass.getCourse()).distinct().collect(Collectors.toList());
            List<ManagerGetCourse> managerGetCourses = new ArrayList<>();
            for (Course course : courseList) {
                managerGetCourses.add(ConvertUtil.convertCourseToManagerGetCourse(course));
            }
            Page<ManagerGetCourse> pages = new PageImpl<ManagerGetCourse>(managerGetCourses, pageable, managerGetCourses.size());
            return PageUtil.convert(pages);
        }
        CourseSpecificationBuilder builder = CourseSpecificationBuilder.specifications().queryByCourseStatus(status);


        Page<Course> coursesPage = courseRepository.findAll(builder.build(), pageable);
        return PageUtil.convert(coursesPage.map(ConvertUtil::convertCourseToManagerGetCourse));

    }

    @Override
    public List<ActivityDto> getAllActivityByCourseId(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));
        User currentUser = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());

        if (!CourseValidator.isMentorOfCourse(currentUser, course)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage("Bạn không phải là giáo viên của khóa học này");
        }
        List<Activity> sectionActivities = course.getActivities().stream()
                .filter(activity -> Objects.equals(activity.getType(), ECourseActivityType.SECTION))
                .collect(Collectors.toList());
        ResponseUtil.responseForRole(EUserRole.TEACHER);
        return ConvertUtil.convertActivityAsTree(sectionActivities, false);
    }

    @Override
    public Boolean mentorRequestApprovalCourse(Long id, List<Long> classIds) {
        User user = MentorUtil.checkIsMentor();
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));

        if (!course.getApproved()) {
            List<Class> classesInRequest = classRepository.findAllById(classIds);

            List<Class> classesOfCourse = course.getClasses();
            // kiểm tra lớp trong request có phai lớp của khóa học không ?
            List<Long> classIdOfCourseList = classesOfCourse.stream().map(Class::getId).collect(Collectors.toList());
            for (Long aClassId : classIds) {
                if (!classIdOfCourseList.contains(aClassId)) {
                    throw ApiException.create(HttpStatus.BAD_REQUEST)
                            .withMessage("Lớp học với mã " + aClassId + " không thuộc khóa học " + course.getId());
                }
            }

            Boolean isValidCourse = CourseUtil.checkCourseValidToSendApproval(course, user, classesInRequest);

            if (isValidCourse) {
                course.setStatus(WAITING);
                courseRepository.save(course);
                classesInRequest
                        .forEach(aClass -> {
                            aClass.setStatus(WAITING);
                            ActivityHistoryUtil.logHistoryForMentorSendRequestClass(user.getId(), aClass);
                            classRepository.save(aClass);
                        });
                return true;
            }
        } else {
            List<Class> classesInRequest = classRepository.findAllById(classIds);

            List<Class> classesOfCourse = course.getClasses();
            // kiểm tra lớp trong request có phai lớp của khóa học không ?
            List<Long> classIdOfCourseList = classesOfCourse.stream().map(Class::getId).collect(Collectors.toList());
            for (Long aClassId : classIds) {
                if (!classIdOfCourseList.contains(aClassId)) {
                    throw ApiException.create(HttpStatus.BAD_REQUEST)
                            .withMessage("Lớp học với mã " + aClassId + " không thuộc khóa học " + course.getId());
                }
            }
            Boolean isValidCourse = CourseUtil.checkCourseIsApprovalValidToSendApproval(course, user, classesInRequest);

            if (isValidCourse) {
                classesInRequest
                        .forEach(aClass -> {
                            aClass.setStatus(WAITING);
                            ActivityHistoryUtil.logHistoryForMentorSendRequestClass(user.getId(), aClass);
                            classRepository.save(aClass);
                        });
                return true;
            }
        }

        ActivityHistory byUserIdAndCourse = activityHistoryRepository.findByUserIdAndType(user.getId(), EActivityType.COURSE);
        if (byUserIdAndCourse == null) {
            ActivityHistoryUtil.logHistoryForMentorSendRequestCourse(user.getId(), course);
        } else {
            byUserIdAndCourse.setCount(byUserIdAndCourse.getCount() + 1);
            activityHistoryRepository.save(byUserIdAndCourse);
        }

        return false;
    }

    @Override
    public CompletenessCourseResponse getCompletenessCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));

        return checkCompletenessCourse(course);
    }

    private CompletenessCourseResponse checkCompletenessCourse(Course course) {
        int completionPercentage = 0;
        int totalFields = 2;

        if (course.getClasses().size() > 0) {
            completionPercentage++;
        }
        if (course.getActivities().size() > 0) {
            completionPercentage++;
        }

        // Tính % hoàn thành dựa trên số lượng trường thông tin có giá trị
        completionPercentage = (int) Math.round(((double) completionPercentage / totalFields) * 100);

        CompletenessCourseResponse response = new CompletenessCourseResponse();
        response.setPercentComplete(completionPercentage);
        response.setAllowSendingApproval(completionPercentage == 100);
        return response;
    }

    @Override
    public Boolean managerApprovalCourseRequest(Long id, ManagerApprovalCourseRequest approvalCourseRequest) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));

        if (!course.getApproved()) {
            validateApprovalCourseRequest(approvalCourseRequest.getStatus());

            if (course.getStatus() != WAITING) {
                throw ApiException.create(HttpStatus.BAD_REQUEST)
                        .withMessage(messageUtil.getLocalMessage(COURSE_STATUS_NOT_ALLOW));
            }
            List<Class> classToApproval = classRepository.findAllById(approvalCourseRequest.getClassIds());
            List<Class> classList = new ArrayList<>();
            for (Class aClass : classToApproval) {
                aClass.setStatus(approvalCourseRequest.getStatus());
                classList.add(aClass);
            }
            classRepository.saveAll(classList);
            course.setStatus(approvalCourseRequest.getStatus());

            if (approvalCourseRequest.getStatus().equals(NOTSTART)) {
                for (Activity activity : course.getActivities()) {
                    activity.setFixed(true);
                }
                course.setApproved(true);
            }


            courseRepository.save(course);
        } else {
            List<Class> classToApproval = classRepository.findAllById(approvalCourseRequest.getClassIds());
            List<Class> classList = new ArrayList<>();
            for (Class aClass : classToApproval) {
                aClass.setStatus(approvalCourseRequest.getStatus());
                classList.add(aClass);
            }
            classRepository.saveAll(classList);
        }


        return true;
    }

    private void validateApprovalCourseRequest(ECourseStatus statusRequest) {
        List<ECourseStatus> ALLOWED_STATUSES = Arrays.asList(NOTSTART, EDITREQUEST, REJECTED);
        if (!ALLOWED_STATUSES.contains(statusRequest)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(COURSE_STATUS_NOT_ALLOW));

        }
    }
}
//
//    @Override
//    public List<CourseDto> getCoursesBySubject(Long subjectId) {
//        Subject subject = subjectRepository.findById(subjectId)
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(SUBJECT_NOT_FOUND_BY_ID) + subjectId));
//        return subject.getCourses().stream()
//                .map(ConvertUtil::convertCourseToCourseDTO)
//                .collect(Collectors.toList());
//    }
//
//
//    @Override
//    public List<Long> mentorCreateCoursePrivate(CreateCourseRequest createCourseRequest) {
//        User currentUserAccountLogin = SecurityUtil.getCurrentUser();
//
//        // create info for course
//        Course course = createCourseFromRequest(createCourseRequest);
//        return createSubCourseAndTimeInWeek(currentUserAccountLogin, course, createCourseRequest);
//    }
//
//
//    @Override
//    public List<Long> mentorCreateCoursePublic(Long id, CreateCourseRequest createCourseRequest) {
//        User currentUserAccountLogin = SecurityUtil.getCurrentUser();
//        Course course = courseRepository.findById(id)
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));
//        return createSubCourseAndTimeInWeek(currentUserAccountLogin, course, createCourseRequest);
//    }
//
//    private List<Long> createSubCourseAndTimeInWeek(User currentUserAccountLogin, Course course, CreateCourseRequest createCourseRequest) {
//        // check mentor account is valid
//        MentorUtil.checkIsMentor();
//
//        course.setStatus(REQUESTING);
//
//        List<Long> subCourseId = new ArrayList<>();
//        List<CreateSubCourseRequest> subCourseRequestsList = createCourseRequest.getSubCourseRequests();
//        List<SubCourse> subCourses = new ArrayList<>();
//        subCourseRequestsList.forEach(createSubCourseRequest -> {
//            List<TimeInWeekRequest> timeInWeekRequests = createSubCourseRequest.getTimeInWeekRequests();
//
//            // create time in week for subCourse
//            List<TimeInWeek> timeInWeeksFromRequest = createTimeInWeeksFromRequest(timeInWeekRequests);
//
//            // create subCourse for course
//            SubCourse subCourseFromRequest = createSubCourseFromRequest(createSubCourseRequest, currentUserAccountLogin, timeInWeeksFromRequest);
//            subCourseFromRequest.setCourse(course);
//
//
//            subCourses.add(subCourseFromRequest);
//
//
//        });
//        course.setSubCourses(subCourses);
//        courseRepository.save(course);
//
//
//        // ghi log
//        subCourses.forEach(subCourse -> {
//                    subCourseId.add(subCourse.getId());
//                    ActivityHistoryUtil.logHistoryForCourseCreated(currentUserAccountLogin.getId(), subCourse);
//                }
//        );
//
//
//        return subCourseId;
//    }
//
//
//    private Course createCourseFromRequest(CreateCourseRequest createCourseRequest) {
//        Long categoryId = createCourseRequest.getCategoryId();
//        if (categoryId == null) {
//            throw ApiException.create(HttpStatus.BAD_REQUEST)
//                    .withMessage(messageUtil.getLocalMessage(PLEASE_SELECT_THE_CATEGORY_FOR_THE_COURSE));
//        }
//        Category category = categoryRepository.findById(categoryId)
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
//                        .withMessage(messageUtil.getLocalMessage(CATEGORY_NOT_FOUND_BY_ID) + categoryId));
//
//
//        Long subjectId = createCourseRequest.getSubjectId();
//        if (subjectId == null) {
//            throw ApiException.create(HttpStatus.BAD_REQUEST)
//                    .withMessage(messageUtil.getLocalMessage(PLEASE_SELECT_THE_SUBJECT_FOR_THE_COURSE));
//        }
//        Optional<Subject> optionalSubject = category.getSubjects().stream().filter(s -> s.getId().equals(subjectId)).findFirst();
//        Subject subject = optionalSubject.orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
//                .withMessage(messageUtil.getLocalMessage(SUBJECT_NOT_FOUND_BY_ID) + subjectId));
//
//        Course course = new Course();
//        course.setName(createCourseRequest.getName());
//        course.setCode(CourseUtil.generateRandomCode(8));
//        course.setDescription(createCourseRequest.getDescription());
//        course.setSubject(subject);
//
//        return course;
//    }
//
//    private SubCourse createSubCourseFromRequest(CreateSubCourseRequest subCourseRequest, User currentUserAccountLogin, List<TimeInWeek> timeInWeeks) {
//        if (subCourseRequest.getPrice() == null) {
//            throw ApiException.create(HttpStatus.BAD_REQUEST)
//                    .withMessage(messageUtil.getLocalMessage(PLEASE_ENTER_THE_PRICE_FOR_THE_COURSE));
//        }
//        SubCourse subCourse = new SubCourse();
//        subCourse.setNumberOfSlot(subCourseRequest.getNumberOfSlot());
//        subCourse.setTypeLearn(subCourseRequest.getType());
//        subCourse.setMinStudent(subCourseRequest.getMinStudent());
//        subCourse.setMaxStudent(subCourseRequest.getMaxStudent());
//        subCourse.setStartDateExpected(subCourseRequest.getStartDateExpected());
//        subCourse.setStatus(REQUESTING);
//        subCourse.setTitle(subCourseRequest.getSubCourseTile());
//        subCourse.setPrice(subCourseRequest.getPrice());
//        subCourse.setLevel(subCourseRequest.getLevel());
//        subCourse.setMentor(currentUserAccountLogin);
//
//        Long imageId = subCourseRequest.getImageId();
//        Image image = imageRepository.findById(imageId)
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
//                        .withMessage(messageUtil.getLocalMessage(IMAGE_NOT_FOUND_BY_ID) + imageId));
//        subCourse.setImage(image);
//        subCourse.setTimeInWeeks(timeInWeeks);
//
//        if (subCourseRequest.getEndDateExpected() != null) {
//            Instant endDateExpected = subCourseRequest.getEndDateExpected();
//            int numberOfSlot = calNumberOfSlotByEndDate(subCourseRequest.getStartDateExpected(), endDateExpected, timeInWeeks);
//            subCourse.setNumberOfSlot(numberOfSlot);
//            subCourse.setEndDateExpected(endDateExpected);
//        } else if (subCourseRequest.getNumberOfSlot() != null) {
//            Integer numberOfSlot = subCourseRequest.getNumberOfSlot();
//            Instant endDateExpected = calEndDateByNumberOfSlot(subCourseRequest.getStartDateExpected(), numberOfSlot, timeInWeeks);
//            subCourse.setNumberOfSlot(numberOfSlot);
//            subCourse.setEndDateExpected(endDateExpected);
//        } else {
//            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Lỗi không tìm thấy số lượng slot học hoặc ngày kết thúc");
//        }
//        return subCourse;
//    }
//
//    private List<TimeInWeek> createTimeInWeeksFromRequest(List<TimeInWeekRequest> timeInWeekRequests) {
//        TimeInWeekRequest duplicateElement = ObjectUtil.isHasDuplicate(timeInWeekRequests);
//        if (duplicateElement != null) {
//            throw ApiException.create(HttpStatus.NOT_FOUND)
//                    .withMessage(SCHEDULE_AND_SLOT_HAVE_BEEN_OVERLAPPED);
//        }
//
//        List<Long> slotIds = timeInWeekRequests.stream().map(TimeInWeekRequest::getSlotId).collect(Collectors.toList());
//        List<Long> dowIds = timeInWeekRequests.stream().map(TimeInWeekRequest::getDayOfWeekId).collect(Collectors.toList());
//
//        Map<Long, Slot> slotMap = slotRepository.findAllById(slotIds).stream()
//                .collect(Collectors.toMap(Slot::getId, Function.identity()));
//        Map<Long, DayOfWeek> dayOfWeekMap = dayOfWeekRepository.findAllById(dowIds).stream()
//                .collect(Collectors.toMap(DayOfWeek::getId, Function.identity()));
//
//        List<TimeInWeek> timeInWeeks = new ArrayList<>();
//        for (TimeInWeekRequest timeInWeekRequest : timeInWeekRequests) {
//            TimeInWeek timeInWeek = new TimeInWeek();
//            DayOfWeek dayOfWeek = Optional.ofNullable(dayOfWeekMap.get(timeInWeekRequest.getDayOfWeekId()))
//                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
//                            .withMessage(DAY_OF_WEEK_COULD_NOT_BE_FOUND));
//            timeInWeek.setDayOfWeek(dayOfWeek);
//
//            Slot slot = Optional.ofNullable(slotMap.get(timeInWeekRequest.getSlotId()))
//                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
//                            .withMessage(SLOT_COULD_NOT_BE_FOUND));
//            timeInWeek.setSlot(slot);
//
//            timeInWeeks.add(timeInWeek);
//        }
//        return timeInWeeks;
//    }
//
//
//    @Override
//    public ApiPage<CourseSubCourseResponse> mentorGetAllCourse(ECourseStatus courseStatus, Pageable pageable) {
//        User currentUser = SecurityUtil.getCurrentUser();
//        Page<SubCourse> subCoursesPage;
//
//        if (courseStatus.equals(ALL)) {
//            subCoursesPage = subCourseRepository.findByMentor(currentUser, pageable);
//        } else {
//            subCoursesPage = subCourseRepository.findByStatusAndMentor(courseStatus, currentUser, pageable);
//        }
//        return PageUtil.convert(subCoursesPage.map(ConvertUtil::subCourseToCourseSubCourseResponseConverter));
//
//    }
//
//    @Override
//    public CourseSubCourseResponse mentorGetCourse(Long subCourseId) {
//        SubCourse subCourse = subCourseRepository.findById(subCourseId).
//                orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
//                        .withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + subCourseId));
//        return ConvertUtil.subCourseToCourseSubCourseResponseConverter(subCourse);
//
//    }
//
//    @Transactional()
//    @Override
//    public Boolean mentorCreateContentCourse(Long id, List<CourseContentDto> request) throws JsonProcessingException {
//
//        Course course = courseRepository.findById(id)
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));
//
//        List<Section> sections = new ArrayList<>();
//        request.forEach(courseContent -> {
//
//            SectionDto sectionContent = courseContent.getSections();
//            Section section = new Section();
//            section.setName(sectionContent.getName());
//            ;
//
//            List<Module> modules = new ArrayList<>();
//            List<ModuleDto> modulesContent = sectionContent.getModules();
//            modulesContent.forEach(moduleContent -> {
//                Module module = new Module();
//                module.setName(moduleContent.getName());
//                module.setSection(section);
//                modules.add(module);
//            });
//            section.getModules().addAll(modules);
//            section.setCourse(course);
//            sections.add(section);
//        });
//
//        course.getSections().addAll(sections);
//        courseRepository.save(course);
//
//
//        return true;
//    }
//
//

//
//
//    @Override
//    public CourseSubCourseDetailResponse getDetailCourseForCoursePage(Long id) {
//        Course course = courseRepository.findById(id)
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));
//        return convertCourseSubCourseToCourseSubCourseDetailResponse(course);
//    }
//
//
//    @Override
//    public ApiPage<SubCourseDetailResponse> getAllSubCourseOfCourse(Long courseId, Pageable pageable) {
//        Course course = courseRepository.findById(courseId)
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
//                        .withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + courseId));
//
//        User currentUser = SecurityUtil.getCurrentUser();
//        Page<SubCourse> subCoursePage = subCourseRepository.findByCourseAndStatus(course, ECourseStatus.NOTSTART, pageable);
//
//        List<SubCourseDetailResponse> subCourseResponses = subCoursePage.getContent().stream()
//                .map(subCourse -> ConvertUtil.convertSubCourseToSubCourseDetailResponse(currentUser, subCourse))
//                .collect(Collectors.toList());
//
//        return PageUtil.convert(new PageImpl<>(subCourseResponses, pageable, subCoursePage.getTotalElements()));
//    }
//
//    @Override
//    public ApiPage<CourseSubCourseResponse> memberGetCourse(ECourseStatus status, Pageable pageable) {
//        User currentUser = SecurityUtil.getCurrentUser();
//        List<Order> orders = currentUser.getOrder();
//        if (status == ECourseStatus.ALL) {
//            return memberGetAllCourses(orders, pageable);
//        }
//        return memberGetCoursesByStatus(status, orders, pageable);
//    }
//
//    public ApiPage<CourseSubCourseResponse> memberGetAllCourses(List<Order> orders, Pageable pageable) {
//
//        List<SubCourse> subCourses = new ArrayList<>();
//        orders.forEach(order -> {
//            order.getOrderDetails().stream()
//                    .map(OrderDetail::getSubCourse)
//                    .filter(Objects::nonNull)
//                    .forEach(subCourses::add);
//        });
//
//        Page<SubCourse> subCoursePage = new PageImpl<>(subCourses, pageable, subCourses.size());
//        List<CourseSubCourseResponse> courseSubCourseResponses = subCoursePage.getContent().stream()
//                .map(ConvertUtil::subCourseToCourseSubCourseResponseConverter)
//                .collect(Collectors.toList());
//
//        return PageUtil.convert(new PageImpl<>(courseSubCourseResponses, pageable, subCoursePage.getTotalElements()));
//    }
//
//    public ApiPage<CourseSubCourseResponse> memberGetCoursesByStatus(ECourseStatus status, List<Order> orders, Pageable pageable) {
//
//        List<SubCourse> subCourses = new ArrayList<>();
//
//        orders.forEach(order -> {
//            order.getOrderDetails().stream()
//                    .map(OrderDetail::getSubCourse)
//                    .filter(Objects::nonNull)
//                    .filter(subCourse -> subCourse.getStatus() == status)
//                    .forEach(subCourses::add);
//        });
//
//        Page<SubCourse> subCoursePage = new PageImpl<>(subCourses, pageable, subCourses.size());
//        List<CourseSubCourseResponse> courseSubCourseResponses = subCoursePage.getContent().stream()
//                .map(ConvertUtil::subCourseToCourseSubCourseResponseConverter)
//                .collect(Collectors.toList());
//        return PageUtil.convert(new PageImpl<>(courseSubCourseResponses, pageable, subCoursePage.getTotalElements()));
//    }
//
//
//    /**
//     * Phương thức này có bug vì người dùng đăng nhập hay không đăng nhập. Kết quả trả về sẽ không đúng.
//     *
//     * @return kết quả phải khác khi đăng nhập
//     * @bug
//     */
//
//    @Override
//    public ApiPage<CourseSubCourseResponse> memberGetCourseSuggest(Pageable pageable) {
//        User userLogin = SecurityUtil.getCurrentUser();
//        Page<SubCourse> subCoursesList;
//        if (userLogin == null) {
//            subCoursesList = subCourseRepository.findByStatus(NOTSTART, pageable);
//        } else {
//            subCoursesList = subCourseRepository.findByStatus(NOTSTART, pageable);
//        }
//        return PageUtil.convert(subCoursesList.map(ConvertUtil::subCourseToCourseSubCourseResponseConverter));
//    }
//
//
//    @Transactional
//    @Override
//    public Boolean mentorUpdateCourse(Long subCourseId, UpdateSubCourseRequest updateCourseRequest) {
//        User currentUserAccountLogin = SecurityUtil.getCurrentUser();
//        MentorProfile mentorProfile = MentorUtil.getCurrentUserMentorProfile(currentUserAccountLogin);
//        SubCourse subCourse = getSubCourseById(subCourseId);
//        CourseUtil.checkCourseOwnership(subCourse, mentorProfile);
//        updateCourseFromRequest(subCourse, updateCourseRequest);
//        if (shouldUpdateImageCourse(subCourse, updateCourseRequest.getImageId())) {
//            updateImageCourse(subCourse, updateCourseRequest);
//        }
//        List<TimeInWeek> timeInWeeks = updateTimeInWeekFromRequest(subCourse, updateCourseRequest);
//        updateSubCourseFromRequest(subCourse, updateCourseRequest, currentUserAccountLogin, timeInWeeks);
//        subCourse.getTimeInWeeks().clear();
//        subCourse.getTimeInWeeks().addAll((timeInWeeks));
//        subCourseRepository.save(subCourse);
//        return true;
//    }
//
//    private boolean shouldUpdateImageCourse(SubCourse subCourse, Long imageId) {
//        return !subCourse.getImage().getId().equals(imageId);
//    }
//
//
//    private SubCourse getSubCourseById(Long subCourseId) {
//        return subCourseRepository.findById(subCourseId)
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
//                        .withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + subCourseId));
//    }
//
//    private void updateCourseFromRequest(SubCourse subCourse, UpdateSubCourseRequest updateCourseRequest) {
//        if (subCourse.getCourse().getType().equals(ECourseType.PRIVATE)) {
//            Course course = subCourse.getCourse();
//            course.setCode(updateCourseRequest.getCourseCode());
//            course.setName(updateCourseRequest.getCourseName());
//            course.setDescription(updateCourseRequest.getCourseDescription());
//            Category category = categoryRepository.findById(updateCourseRequest.getCategoryId())
//                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CATEGORY_NOT_FOUND_BY_ID) + updateCourseRequest.getCategoryId()));
//            Set<Subject> subjects = category.getSubjects();
//            subjects.forEach(subject -> {
//                if (subject.getId().equals(updateCourseRequest.getSubjectId())) {
//                    course.setSubject(subject);
//                }
//            });
//            subCourse.setCourse(course);
//        }
//
//    }
//
//    private void updateImageCourse(SubCourse subCourse, UpdateSubCourseRequest updateCourseRequest) {
//        Image image = imageRepository.findById(subCourse.getImage().getId())
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
//                        .withMessage(messageUtil.getLocalMessage(IMAGE_NOT_FOUND_BY_ID) + subCourse.getImage().getId()));
//
//        List<SubCourse> subCourseByImage = subCourseRepository.findAllByImage(image);
//        if (subCourseByImage.size() == 1) {
//            Optional.ofNullable(subCourse.getImage())
//                    .map(Image::getId)
//                    .ifPresent(imageRepository::deleteById);
//        } else {
//            subCourse.setImage(null);
//        }
//        Image imageUpdated = imageRepository.findById(subCourse.getImage().getId())
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
//                        .withMessage(messageUtil.getLocalMessage(IMAGE_NOT_FOUND_BY_ID) + updateCourseRequest.getImageId()));
//
//        subCourse.setImage(imageUpdated);
//    }
//
//    private void updateSubCourseFromRequest(SubCourse subCourse, UpdateSubCourseRequest updateCourseRequest, User currentUserAccountLogin, List<TimeInWeek> timeInWeeks) {
//        subCourse.setTitle(updateCourseRequest.getSubCourseTitle());
//        subCourse.setLevel(updateCourseRequest.getLevel());
//        subCourse.setPrice(updateCourseRequest.getPrice());
//        subCourse.setStartDateExpected(updateCourseRequest.getStartDateExpected());
//        subCourse.setMinStudent(updateCourseRequest.getMinStudent());
//        subCourse.setMaxStudent(updateCourseRequest.getMaxStudent());
//        subCourse.setTypeLearn(updateCourseRequest.getType());
//        subCourse.setLevel(updateCourseRequest.getLevel());
//        subCourse.setMentor(currentUserAccountLogin);
//
//        if (updateCourseRequest.getEndDateExpected() != null) {
//            Instant endDateExpected = updateCourseRequest.getEndDateExpected();
//            int numberOfSlot = calNumberOfSlotByEndDate(updateCourseRequest.getStartDateExpected(), endDateExpected, timeInWeeks);
//            subCourse.setNumberOfSlot(numberOfSlot);
//            subCourse.setEndDateExpected(endDateExpected);
//        } else if (updateCourseRequest.getNumberOfSlot() != null) {
//            Integer numberOfSlot = updateCourseRequest.getNumberOfSlot();
//            Instant endDateExpected = calEndDateByNumberOfSlot(updateCourseRequest.getStartDateExpected(), numberOfSlot, timeInWeeks);
//            subCourse.setNumberOfSlot(numberOfSlot);
//            subCourse.setEndDateExpected(endDateExpected);
//        } else {
//            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Lỗi không tìm thấy số lượng slot học hoặc ngày kết thúc");
//        }
//    }
//
//
//    private List<TimeInWeek> updateTimeInWeekFromRequest(SubCourse subCourse, UpdateSubCourseRequest updateCourseRequest) {
//        List<TimeInWeekRequest> timeInWeekRequests = updateCourseRequest.getTimeInWeekRequests();
//        TimeInWeekRequest duplicateElement = ObjectUtil.isHasDuplicate(timeInWeekRequests);
//        if (duplicateElement != null) {
//            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(SCHEDULE_AND_SLOT_HAVE_BEEN_OVERLAPPED);
//        }
//        List<Long> slotIds = timeInWeekRequests.stream().map(TimeInWeekRequest::getSlotId).collect(Collectors.toList());
//        List<Long> dowIds = timeInWeekRequests.stream().map(TimeInWeekRequest::getDayOfWeekId).collect(Collectors.toList());
//        Map<Long, Slot> slotMap = slotRepository.findAllById(slotIds).stream().collect(Collectors.toMap(Slot::getId, Function.identity()));
//        Map<Long, DayOfWeek> dayOfWeekMap = dayOfWeekRepository.findAllById(dowIds).stream().collect(Collectors.toMap(DayOfWeek::getId, Function.identity()));
//        List<TimeInWeek> timeInWeeks = new ArrayList<>();
//        for (TimeInWeekRequest timeInWeekRequest : timeInWeekRequests) {
//            DayOfWeek dayOfWeek = dayOfWeekMap.get(timeInWeekRequest.getDayOfWeekId());
//            if (dayOfWeek == null) {
//                throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(DAY_OF_WEEK_COULD_NOT_BE_FOUND);
//            }
//            Slot slot = slotMap.get(timeInWeekRequest.getSlotId());
//            if (slot == null) {
//                throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(SLOT_COULD_NOT_BE_FOUND);
//            }
//            TimeInWeek timeInWeek = new TimeInWeek();
//            timeInWeek.setDayOfWeek(dayOfWeek);
//            timeInWeek.setSlot(slot);
//            timeInWeek.setSubCourse(subCourse);
//            timeInWeeks.add(timeInWeek);
//        }
//        return timeInWeeks;
//    }
//
//
//    @Transactional
//    @Override
//    public Boolean mentorDeleteCourse(Long subCourseId) {
//        User user = MentorUtil.checkIsMentor();
//
//        SubCourse subCourse = subCourseRepository.findById(subCourseId).
//                orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
//                        .withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + subCourseId));
//
//        if (!subCourse.getMentor().getMentorProfile().equals(user.getMentorProfile())) {
//            throw ApiException.create(HttpStatus.BAD_REQUEST)
//                    .withMessage(messageUtil.getLocalMessage(COURSE_DOES_NOT_BELONG_TO_THE_TEACHER));
//        }
//
//        if (subCourse.getImage() != null) {
//            Image image = imageRepository.findById(subCourse.getImage().getId())
//                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
//                            .withMessage(messageUtil.getLocalMessage(IMAGE_NOT_FOUND_BY_ID) + subCourse.getImage().getId()));
//
//            List<SubCourse> subCourseByImage = subCourseRepository.findAllByImage(image);
//            if (subCourseByImage.size() == 1) {
//                Optional.ofNullable(subCourse.getImage())
//                        .map(Image::getId)
//                        .ifPresent(imageRepository::deleteById);
//            } else {
//                subCourse.setImage(null);
//            }
//        }
//
//        if (subCourse.getCourse().getType().equals(ECourseType.PRIVATE)) {
//            courseRepository.delete(subCourse.getCourse());
//        } else {
//            subCourse.setCourse(null);
//        }
//
//        ActivityHistoryUtil.logHistoryForCourseDeleted(user.getId(), subCourse);
//        subCourseRepository.delete(subCourse);
//
//        return true;
//    }
//
//    @Override
//    public ApiPage<CourseDto> getCoursePublic(Pageable pageable) {
//        List<Course> coursesTypePublic = courseRepository.findAllByType(ECourseType.PUBLIC);
//        List<CourseDto> courseDtosTypePublic = coursesTypePublic.stream()
//                .map(ConvertUtil::convertCourseToCourseDTO)
//                .collect(Collectors.toList());
//
//
//        return PageUtil.convert(new PageImpl<>(courseDtosTypePublic, pageable, courseDtosTypePublic.size()));
//    }
//
//
//    @Override
//    public Boolean mentorRequestApprovalCourse(Long subCourseId) {
//        User user = MentorUtil.checkIsMentor();
//        SubCourse subCourse = subCourseRepository.findById(subCourseId).
//                orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + subCourseId));
//
//        Boolean isValidCourse = CourseUtil.checkCourseValid(subCourse, user);
//
//        if (isValidCourse) {
//            subCourse.setStatus(WAITING);
//            subCourseRepository.save(subCourse);
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public ApiPage<CourseSubCourseResponse> coursePendingToApprove(ECourseStatus status, Pageable pageable) {
//        if (status.equals(REQUESTING)) {
//            throw ApiException.create(HttpStatus.BAD_REQUEST)
//                    .withMessage(messageUtil.getLocalMessage(USER_NOT_HAVE_PERMISSION_TO_VIEW_THIS_COURSE));
//        }
//        Page<SubCourse> subCoursesPedingPage = null;
//        if (status.equals(ALL)) {
//            subCoursesPedingPage = subCourseRepository.findByStatusNot(REQUESTING, pageable);
//        } else {
//            subCoursesPedingPage = subCourseRepository.findByStatus(status, pageable);
//        }
//        return PageUtil.convert(subCoursesPedingPage.map(ConvertUtil::subCourseToCourseSubCourseResponseConverter));
//    }
//

//
//    @Override
//    public Boolean managerCreateCourse(CreateCoursePublicRequest createCourseRequest) {
//        Course courseFromRequest = createCoursePublicFromRequest(createCourseRequest);
//        courseFromRequest.setType(ECourseType.PUBLIC);
//        courseRepository.save(courseFromRequest);
//        return true;
//    }
//
//
//    private Course createCoursePublicFromRequest(CreateCoursePublicRequest coursePublicRequest) {
//        Long categoryId = coursePublicRequest.getCategoryId();
//        if (categoryId == null) {
//            throw ApiException.create(HttpStatus.BAD_REQUEST)
//                    .withMessage(messageUtil.getLocalMessage(PLEASE_SELECT_THE_CATEGORY_FOR_THE_COURSE));
//        }
//        Category category = categoryRepository.findById(categoryId)
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
//                        .withMessage(messageUtil.getLocalMessage(CATEGORY_NOT_FOUND_BY_ID) + categoryId));
//
//
//        Long subjectId = coursePublicRequest.getSubjectId();
//        if (subjectId == null) {
//            throw ApiException.create(HttpStatus.BAD_REQUEST)
//                    .withMessage(messageUtil.getLocalMessage(PLEASE_SELECT_THE_SUBJECT_FOR_THE_COURSE));
//        }
//        Optional<Subject> optionalSubject = category.getSubjects().stream().filter(s -> s.getId().equals(subjectId)).findFirst();
//        Subject subject = optionalSubject.orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
//                .withMessage(messageUtil.getLocalMessage(SUBJECT_NOT_FOUND_BY_ID) + subjectId));
//
//        Course course = new Course();
//        course.setName(coursePublicRequest.getName());
//        course.setCode(coursePublicRequest.getCode());
//        course.setDescription(coursePublicRequest.getDescription());
//        course.setSubject(subject);
//
//        return course;
//    }
//
//
//    private void validateApprovalCourseRequest(ECourseStatus statusRequest) {
//        List<ECourseStatus> ALLOWED_STATUSES = Arrays.asList(NOTSTART, EDITREQUEST, REJECTED);
//        if (!ALLOWED_STATUSES.contains(statusRequest)) {
//            throw ApiException.create(HttpStatus.BAD_REQUEST)
//                    .withMessage(messageUtil.getLocalMessage(COURSE_STATUS_NOT_ALLOW));
//
//        }
//    }
//
//
//    @Override
//    public Boolean mentorUploadImageCourse(ImageRequest imageRequest) {
//        ImageUtil.uploadImage(imageRequest);
//        return true;
//    }
//
//    @Override
//    public Boolean memberRegisterCourse(Long id) {
//        SecurityUtil.getCurrentUser();
//        return true;
//    }
//
//    private Instant calEndDateByNumberOfSlot(Instant startDate, int numberOfSlot, List<TimeInWeek> timeInWeeks) {
//        List<EDayOfWeekCode> availableDOW = timeInWeeks.stream().map(timeInWeek -> timeInWeek.getDayOfWeek().getCode()).distinct().collect(Collectors.toList());
//        Instant endDate = startDate;
//        int i = numberOfSlot;
//        while (i > 0) {
//            EDayOfWeekCode dayOfWeekCode = TimeUtil.getDayOfWeek(endDate);
//            if (dayOfWeekCode == null) {
//                throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không thể nhận diện thứ trong tuần, lỗi hệ thống vui lòng liên hệ Admin!");
//            }
//            if (availableDOW.contains(dayOfWeekCode)) {
//                List<TimeInWeek> dateOfWeeks = timeInWeeks.stream().filter(timeInWeek -> Objects.equals(timeInWeek.getDayOfWeek().getCode(), dayOfWeekCode)).collect(Collectors.toList());
//                for (TimeInWeek dow : dateOfWeeks) {
//                    if (i <= 0) break;
//                    i--;
//                }
//            }
//            endDate = endDate.plus(1, ChronoUnit.DAYS);
//        }
//        return endDate;
//    }
//
//    private int calNumberOfSlotByEndDate(Instant startDate, Instant endDate, List<TimeInWeek> timeInWeeks) {
//        List<EDayOfWeekCode> availableDOW = timeInWeeks.stream().map(timeInWeek -> timeInWeek.getDayOfWeek().getCode()).distinct().collect(Collectors.toList());
//        Instant date = startDate;
//        int numberOfSlot = 0;
//        while (date.isBefore(endDate) || date.equals(endDate)) {
//            EDayOfWeekCode dayOfWeekCode = TimeUtil.getDayOfWeek(date);
//            if (availableDOW.contains(dayOfWeekCode)) {
//                List<TimeInWeek> dateOfWeeks = timeInWeeks.stream().filter(timeInWeek -> Objects.equals(timeInWeek.getDayOfWeek().getCode(), dayOfWeekCode)).collect(Collectors.toList());
//                numberOfSlot = numberOfSlot + dateOfWeeks.size();
//            }
//            date = date.plus(1, ChronoUnit.DAYS);
//        }
//        return numberOfSlot;
//    }
//}
