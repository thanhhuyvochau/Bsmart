
package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.director.NotificationDirector;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.constant.*;
import fpt.project.bsmart.entity.dto.ActivityDto;
import fpt.project.bsmart.entity.dto.ResponseMessage;
import fpt.project.bsmart.entity.request.CourseSearchRequest;
import fpt.project.bsmart.entity.request.CreateCourseRequest;
import fpt.project.bsmart.entity.request.ManagerApprovalCourseRequest;
import fpt.project.bsmart.entity.response.CourseResponse;
import fpt.project.bsmart.entity.response.course.CompletenessCourseResponse;
import fpt.project.bsmart.entity.response.course.ManagerGetCourse;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.ICourseService;
import fpt.project.bsmart.util.*;
import fpt.project.bsmart.util.email.EmailUtil;
import fpt.project.bsmart.util.specification.CourseSpecificationBuilder;
import fpt.project.bsmart.validator.CourseValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static fpt.project.bsmart.entity.constant.ECourseClassStatus.*;
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
    private final WebSocketUtil webSocketUtil;
    private final NotificationRepository notificationRepository;

    private final FeedbackTemplateRepository feedbackTemplateRepository;
    private final EmailUtil emailUtil;

    public CourseServiceImpl(CourseRepository courseRepository, MessageUtil messageUtil, CategoryRepository categoryRepository, ActivityRepository activityRepository, ClassRepository classRepository, ActivityHistoryRepository activityHistoryRepository, WebSocketUtil webSocketUtil, NotificationRepository notificationRepository, FeedbackTemplateRepository feedbackTemplateRepository, EmailUtil emailUtil) {
        this.courseRepository = courseRepository;
        this.messageUtil = messageUtil;
        this.categoryRepository = categoryRepository;
        this.activityRepository = activityRepository;
        this.classRepository = classRepository;
        this.activityHistoryRepository = activityHistoryRepository;
        this.webSocketUtil = webSocketUtil;
        this.notificationRepository = notificationRepository;
        this.feedbackTemplateRepository = feedbackTemplateRepository;
        this.emailUtil = emailUtil;
    }


    @Override
    public ApiPage<CourseResponse> getCourseForCoursePage(CourseSearchRequest query, final Pageable pageable) {

        List<Class> byStatus = classRepository.findByStatus(NOTSTART);
        List<Long> classIds = byStatus.stream().map(Class::getId).collect(Collectors.toList());
        CourseSpecificationBuilder builder = CourseSpecificationBuilder.specifications()
                .queryLike(query.getQ())
                .queryByCourseStatus(NOTSTART)
                .queryByClassId(classIds)
                .queryBySubjectId(query.getSubjectId())
                .queryByCategoryId(query.getCategoryId());

        Page<Course> coursesPage = courseRepository.findAll(builder.build(), pageable);
        List<Course> collect = coursesPage.stream().distinct().collect(Collectors.toList());
        // Add a call to the distinct() method to remove duplicates
        Page<Course> coursePages = new PageImpl<>(collect, pageable, collect.size());
        return PageUtil.convert(coursePages.map(ConvertUtil::convertCourseCourseResponsePage));
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
        return PageUtil.convert(coursePage.map(ConvertUtil::convertCourseCourseResponse));
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
        return PageUtil.convert(coursesPage.map(ConvertUtil::convertCourseCourseResponse));
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
        List<ECourseClassStatus> statusClasses = classes.stream().map(Class::getStatus).collect(Collectors.toList());
        if (statusClasses.contains(ECourseClassStatus.NOTSTART) || statusClasses.contains(STARTING) || statusClasses.contains(WAITING)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(CLASSES_ARE_CURRENTLY_STARTING_FROM_THIS_COURSE_CANNOT_DELETE));
        }
        List<Activity> activities = course.getActivities();
        activityRepository.deleteAll(activities);
        courseRepository.delete(course);
        return true;
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
        CourseValidator.checkEmptySectionOfCourseActivity(course.getActivities());
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
//                course.setApproved(true);
                course.setStatus(WAITING);
                courseRepository.save(course);
                classesInRequest
                        .forEach(aClass -> {
                            aClass.setStatus(WAITING);
                            ActivityHistoryUtil.logHistoryForMentorSendRequestClass(user.getId(), aClass);
                            classRepository.save(aClass);
                        });
                ActivityHistory activityHistory = activityHistoryRepository.findByTypeAndActivityId(EActivityType.COURSE, course.getId());
                if (activityHistory != null) {
                    Integer count = activityHistory.getCount();
                    count = count + 1;
                    activityHistory.setCount(count);
                    activityHistoryRepository.save(activityHistory);
                } else {
                    ActivityHistoryUtil.logHistoryForMentorSendRequestCourse(user.getId(), course);
                }

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
                ActivityHistory activityHistory = activityHistoryRepository.findByTypeAndActivityId(EActivityType.COURSE, course.getId());
                if (activityHistory != null) {
                    activityHistory.setCount(activityHistory.getCount() + 1);
                    activityHistoryRepository.save(activityHistory);
                } else {
                    ActivityHistoryUtil.logHistoryForMentorSendRequestCourse(user.getId(), course);
                }
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


    //     ################################## START MANAGER ##########################################
    @Override
    public ApiPage<ManagerGetCourse> coursePendingToApprove(ECourseClassStatus status, Pageable pageable) {

//        if (status.equals(WAITING)) {
//            List<Class> byStatus = classRepository.findByStatus(WAITING);
//            List<Course> courseList = byStatus.stream().map(aClass -> aClass.getCourse()).distinct().collect(Collectors.toList());
//            List<ManagerGetCourse> managerGetCourses = new ArrayList<>();
//            for (Course course : courseList) {
//                managerGetCourses.add(ConvertUtil.convertCourseToManagerGetCourse(course, status));
//            }
//            Page<ManagerGetCourse> pages = new PageImpl<ManagerGetCourse>(managerGetCourses, pageable, managerGetCourses.size());
//            return PageUtil.convert(pages);
//        }

        /**Get course status same with parameter or one of class of course is that parameter*/
        CourseSpecificationBuilder builder = CourseSpecificationBuilder.specifications().queryByCourseStatusForManager(status);
        Page<Course> coursesPage = courseRepository.findAll(builder.build(), pageable);
        return PageUtil.convert(coursesPage.map(course -> ConvertUtil.convertCourseToManagerGetCourse(course, status)));
    }

    @Override
    public Boolean managerApprovalCourseRequest(Long id, ManagerApprovalCourseRequest approvalCourseRequest) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));

        FeedbackTemplate feedbackIsDefault = feedbackTemplateRepository.findByTypeAndIsDefault(EFeedbackType.COURSE, true);

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
                aClass.setFeedbackTemplate(feedbackIsDefault);
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
            User creator = course.getCreator();
            /**Notification for course*/
            Notification notification = NotificationDirector.buildApprovalCourse(course, approvalCourseRequest.getStatus());
            notificationRepository.save(notification);
            ResponseMessage responseMessage = ConvertUtil.convertNotificationToResponseMessage(notification, creator);
            webSocketUtil.sendPrivateNotification(creator.getEmail(), responseMessage);
            courseRepository.save(course);
            /**Notification for classes*/
            List<Notification> classNotifications = new ArrayList<>();
            for (Class clazz : classList) {
                Notification clazzNotification = NotificationDirector.buildApprovalClass(clazz, clazz.getStatus());
                classNotifications.add(clazzNotification);
                emailUtil.sendApprovalClassEmail(course, approvalCourseRequest, clazz);
            }
            notificationRepository.saveAll(classNotifications);
            for (Notification classNotification : classNotifications) {
                ResponseMessage classResponseMessage = ConvertUtil.convertNotificationToResponseMessage(classNotification, creator);
                webSocketUtil.sendPrivateNotification(creator.getEmail(), classResponseMessage);
            }
            emailUtil.sendApprovalCourseEmail(course, approvalCourseRequest);
        } else {
            List<Class> classToApproval = classRepository.findAllById(approvalCourseRequest.getClassIds());
            List<Class> classList = new ArrayList<>();
            for (Class aClass : classToApproval) {
                aClass.setStatus(approvalCourseRequest.getStatus());
                aClass.setFeedbackTemplate(feedbackIsDefault);
                classList.add(aClass);
            }
            classRepository.saveAll(classList);
            /**Notification and send email for classes*/
            List<Notification> classNotifications = new ArrayList<>();
            for (Class clazz : classList) {
                Notification clazzNotification = NotificationDirector.buildApprovalClass(clazz, clazz.getStatus());
                classNotifications.add(clazzNotification);
                emailUtil.sendApprovalClassEmail(course, approvalCourseRequest, clazz);
            }
            notificationRepository.saveAll(classNotifications);
            for (Notification classNotification : classNotifications) {
                Optional<Notifier> optionalNotifier = Optional.ofNullable(classNotification.getNotifiers().get(0));
                if (optionalNotifier.isPresent()) {
                    Notifier notifier = optionalNotifier.get();
                    ResponseMessage classResponseMessage = ConvertUtil.convertNotificationToResponseMessage(classNotification, notifier.getUser());
                    webSocketUtil.sendPrivateNotification(notifier.getUser().getEmail(), classResponseMessage);
                }
            }
            emailUtil.sendApprovalCourseEmail(course, approvalCourseRequest);
        }


        return true;
    }


    private void validateApprovalCourseRequest(ECourseClassStatus statusRequest) {
        List<ECourseClassStatus> ALLOWED_STATUSES = Arrays.asList(NOTSTART, EDITREQUEST, REJECTED);
        if (!ALLOWED_STATUSES.contains(statusRequest)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(COURSE_STATUS_NOT_ALLOW));

        }
    }


    @Override
    public Boolean managerBlockCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));

        // lấy danh sách lớp đang dạy
        List<Class> classStarting = course.getClasses()
                .stream().filter(aClass -> aClass.getStatus().equals(STARTING)).collect(Collectors.toList());

        course.setStatus(BLOCK);

        classStarting.forEach(aClass -> {
            aClass.setStatus(BLOCK); // Update the status of each class
            classRepository.save(aClass); // Save the modified class
        });

        return true;
    }

    //     ################################## END MANAGER ##########################################
}
