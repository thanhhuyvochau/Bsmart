package fpt.project.bsmart.service.Impl;


import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.constant.EAccountStatus;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.constant.ECourseType;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.entity.dto.CourseDto;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.response.CourseResponse;
import fpt.project.bsmart.entity.response.CourseSubCourseDetailResponse;
import fpt.project.bsmart.entity.response.CourseSubCourseResponse;
import fpt.project.bsmart.entity.response.SubCourseDetailResponse;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.ICourseService;
import fpt.project.bsmart.util.*;
import fpt.project.bsmart.util.specification.CourseSpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


import static fpt.project.bsmart.entity.constant.ECourseStatus.*;
import static fpt.project.bsmart.util.Constants.ErrorMessage.*;
import static fpt.project.bsmart.util.ConvertUtil.convertCourseSubCourseToCourseSubCourseDetailResponse;
import static fpt.project.bsmart.util.ConvertUtil.convertCourseToCourseDTO;


@Service
public class CourseServiceImpl implements ICourseService {
    private final CategoryRepository categoryRepository;
    private final MessageUtil messageUtil;

    private final CourseRepository courseRepository;
    private final SubCourseRepository subCourseRepository;


    private final SubjectRepository subjectRepository;

    private final ImageRepository imageRepository;

    private final DayOfWeekRepository dayOfWeekRepository;
    private final SlotRepository slotRepository;


    public CourseServiceImpl(CategoryRepository categoryRepository, MessageUtil messageUtil, CourseRepository courseRepository, SubCourseRepository subCourseRepository, SubjectRepository subjectRepository, ImageRepository imageRepository, DayOfWeekRepository dayOfWeekRepository, SlotRepository slotRepository) {
        this.categoryRepository = categoryRepository;
        this.messageUtil = messageUtil;
        this.courseRepository = courseRepository;
        this.subCourseRepository = subCourseRepository;
        this.subjectRepository = subjectRepository;
        this.imageRepository = imageRepository;
        this.dayOfWeekRepository = dayOfWeekRepository;
        this.slotRepository = slotRepository;
    }

    @Override
    public List<CourseDto> getCoursesBySubject(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(SUBJECT_NOT_FOUND_BY_ID) + subjectId));
        return subject.getCourses().stream()
                .map(ConvertUtil::convertCourseToCourseDTO)
                .collect(Collectors.toList());
    }


    @Override
    public Long mentorCreateCourse(CreateCourseRequest createCourseRequest) {
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();


        // check mentor account is valid
        checkMentorProfile(currentUserAccountLogin);

        Course course = createCourseFromRequest(createCourseRequest);

        course.setStatus(REQUESTING);


        List<CreateSubCourseRequest> subCourseRequestsList = createCourseRequest.getSubCourseRequests();
        List<SubCourse> subCourses = new ArrayList<>();
        subCourseRequestsList.forEach(createSubCourseRequest -> {
            // create subCourse for course
            SubCourse subCourseFromRequest = createSubCourseFromRequest(createSubCourseRequest, currentUserAccountLogin);

            List<TimeInWeekRequest> timeInWeekRequests = createSubCourseRequest.getTimeInWeekRequests();

            // create time in week for subCourse
            List<TimeInWeek> timeInWeeksFromRequest = createTimeInWeeksFromRequest(timeInWeekRequests);


            subCourseFromRequest.setTimeInWeeks(timeInWeeksFromRequest);
            subCourseFromRequest.setCourse(course);
            subCourses.add(subCourseFromRequest);

        });

        course.setSubCourses(subCourses);

        // ghi log
        subCourses.forEach(subCourse -> {
                    ActivityHistoryUtil.logHistoryForCourseApprove(subCourse.getId(), "mentor create course");
                }
        );

        return courseRepository.save(course).getId();
    }

    private void checkMentorProfile(User currentUserAccountLogin) {
        MentorProfile mentorProfile = currentUserAccountLogin.getMentorProfile();
        List<Role> roles = currentUserAccountLogin.getRoles();
        boolean isRoleTeacher = roles.stream().anyMatch(role -> role.getCode().equals(EUserRole.TEACHER));

        if (!isRoleTeacher || mentorProfile == null || !mentorProfile.getStatus().equals(EAccountStatus.STARTING)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(ACCOUNT_IS_NOT_MENTOR));
        }
    }

    private Course createCourseFromRequest(CreateCourseRequest createCourseRequest) {
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

        Course course = new Course();
        course.setName(createCourseRequest.getName());
        course.setCode(createCourseRequest.getCode());
        course.setDescription(createCourseRequest.getDescription());
        course.setSubject(subject);

        return course;
    }

    private SubCourse createSubCourseFromRequest(CreateSubCourseRequest subCourseRequest, User currentUserAccountLogin) {
        if (subCourseRequest.getPrice() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(PLEASE_ENTER_THE_PRICE_FOR_THE_COURSE));
        }
        SubCourse subCourse = new SubCourse();
        subCourse.setNumberOfSlot(subCourseRequest.getNumberOfSlot());
        subCourse.setTypeLearn(subCourseRequest.getType());
        subCourse.setNumberOfSlot(subCourseRequest.getNumberOfSlot());
        subCourse.setMinStudent(subCourseRequest.getMinStudent());
        subCourse.setMaxStudent(subCourseRequest.getMaxStudent());
        subCourse.setStartDateExpected(subCourseRequest.getStartDateExpected());
        subCourse.setEndDateExpected(subCourseRequest.getEndDateExpected());
        subCourse.setStatus(REQUESTING);
        subCourse.setTitle(subCourseRequest.getSubCourseTile());
        subCourse.setPrice(subCourseRequest.getPrice());
        subCourse.setLevel(subCourseRequest.getLevel());
        subCourse.setMentor(currentUserAccountLogin);

        Long imageId = subCourseRequest.getImageId();
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(IMAGE_NOT_FOUND_BY_ID) + imageId));
        subCourse.setImage(image);

        return subCourse;
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


    @Override
    public ApiPage<CourseSubCourseResponse> mentorGetCourse(ECourseStatus courseStatus, Pageable pageable) {
        User currentUser = SecurityUtil.getCurrentUser();
        Page<SubCourse> subCoursesPage;

        if (courseStatus.equals(ALL)) {
            subCoursesPage = subCourseRepository.findByMentor(currentUser, pageable);
        } else {
            subCoursesPage = subCourseRepository.findByStatusAndMentor(courseStatus, currentUser, pageable);
        }
        return PageUtil.convert(subCoursesPage.map(ConvertUtil::subCourseToCourseSubCourseResponseConverter));

    }

    @Override
    public ApiPage<CourseResponse> getCourseForCoursePage(CourseSearchRequest query, Pageable pageable) {

        CourseSpecificationBuilder builder = CourseSpecificationBuilder.specifications()
                .queryLike(query.getQ())
                .queryByCourseStatus(ECourseStatus.NOTSTART)
                .queryBySubCourseType(query.getTypes())
                .queryBySubjectId(query.getSubjectId())
                .queryByCategoryId(query.getCategoryId());

        Page<Course> coursesPage = courseRepository.findAll(builder.build(), pageable);
        return PageUtil.convert(coursesPage.map(ConvertUtil::convertCourseCourseResponsePage));
    }


    @Override
    public CourseSubCourseDetailResponse getDetailCourseForCoursePage(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));
        return convertCourseSubCourseToCourseSubCourseDetailResponse(course);
    }


    @Override
    public ApiPage<SubCourseDetailResponse> getAllSubCourseOfCourse(Long courseId, Pageable pageable) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + courseId));

        User currentUser = SecurityUtil.getCurrentUser();
        Page<SubCourse> subCoursePage = subCourseRepository.findByCourseAndStatus(course, ECourseStatus.NOTSTART, pageable);

        List<SubCourseDetailResponse> subCourseResponses = subCoursePage.getContent().stream()
                .map(subCourse -> ConvertUtil.convertSubCourseToSubCourseDetailResponse(currentUser, subCourse))
                .collect(Collectors.toList());

        return PageUtil.convert(new PageImpl<>(subCourseResponses, pageable, subCoursePage.getTotalElements()));
    }

    @Override
    public ApiPage<CourseSubCourseResponse> memberGetCourse(ECourseStatus status, Pageable pageable) {
        User currentUser = SecurityUtil.getCurrentUser();
        List<Order> orders = currentUser.getOrder();
        List<SubCourse> subCourses = new ArrayList<>();
        if (status == ECourseStatus.ALL) {
            return memberGetAllCourses(orders, pageable);
        }
        return memberGetCoursesByStatus(status, orders, pageable);
    }

    public ApiPage<CourseSubCourseResponse> memberGetAllCourses(List<Order> orders, Pageable pageable) {

        List<SubCourse> subCourses = new ArrayList<>();
        orders.forEach(order -> {
            order.getOrderDetails().stream()
                    .map(OrderDetail::getSubCourse)
                    .filter(Objects::nonNull)
                    .forEach(subCourses::add);
        });

        Page<SubCourse> subCoursePage = new PageImpl<>(subCourses, pageable, subCourses.size());
        List<CourseSubCourseResponse> courseSubCourseResponses = subCoursePage.getContent().stream()
                .map(ConvertUtil::subCourseToCourseSubCourseResponseConverter)
                .collect(Collectors.toList());

        return PageUtil.convert(new PageImpl<>(courseSubCourseResponses, pageable, subCoursePage.getTotalElements()));
    }

    public ApiPage<CourseSubCourseResponse> memberGetCoursesByStatus(ECourseStatus status, List<Order> orders, Pageable pageable) {

        List<SubCourse> subCourses = new ArrayList<>();

        orders.forEach(order -> {
            order.getOrderDetails().stream()
                    .map(OrderDetail::getSubCourse)
                    .filter(Objects::nonNull)
                    .filter(subCourse -> subCourse.getStatus() == status)
                    .forEach(subCourses::add);
        });

        Page<SubCourse> subCoursePage = new PageImpl<>(subCourses, pageable, subCourses.size());
        List<CourseSubCourseResponse> courseSubCourseResponses = subCoursePage.getContent().stream()
                .map(ConvertUtil::subCourseToCourseSubCourseResponseConverter)
                .collect(Collectors.toList());
        return PageUtil.convert(new PageImpl<>(courseSubCourseResponses, pageable, subCoursePage.getTotalElements()));
    }


    /**
     * Phương thức này có bug vì người dùng đăng nhập hay không đăng nhập. Kết quả trả về sẽ không đúng.
     *
     * @return kết quả phải khác khi đăng nhập
     * @bug
     */

    @Override
    public ApiPage<CourseSubCourseResponse> memberGetCourseSuggest(Pageable pageable) {
        User userLogin = SecurityUtil.getCurrentUser();
        Page<SubCourse> subCoursesList;
        if (userLogin == null) {
            subCoursesList = subCourseRepository.findByStatus(NOTSTART, pageable);
        } else {
            subCoursesList = subCourseRepository.findByStatus(NOTSTART, pageable);
        }

        return PageUtil.convert(subCoursesList.map(ConvertUtil::subCourseToCourseSubCourseResponseConverter));

    }

    /*
     * TODO :lean code ở đây
     *  @author Your Name
     * */


    @Transactional
    @Override
    public Boolean mentorUpdateCourse(Long subCourseId, UpdateSubCourseRequest updateCourseRequest) {

        User currentUserAccountLogin = SecurityUtil.getCurrentUser();

        MentorProfile mentorProfile = currentUserAccountLogin.getMentorProfile();
        if (mentorProfile == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(ACCOUNT_IS_NOT_MENTOR));
        }

        if (!mentorProfile.getStatus().equals(EAccountStatus.STARTING)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(ACCOUNT_IS_NOT_MENTOR));
        }


        SubCourse subCourse = subCourseRepository.findById(subCourseId).
                orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + subCourseId));

        if (!subCourse.getMentor().getMentorProfile().equals(mentorProfile)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(COURSE_DOES_NOT_BELONG_TO_THE_TEACHER));
        }

        if (!subCourse.getStatus().equals(EDITREQUEST) && !subCourse.getStatus().equals(REQUESTING)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(SUB_COURSE_STATUS_NOT_ALLOW));
        }

        if (updateCourseRequest.getImageId() != null) {
            Optional.ofNullable(subCourse.getImage())
                    .map(Image::getId)
                    .ifPresent(imageRepository::deleteById);
        }
//        subCourseRepository.save(subCourse);
//        subCourse.setImage(null);


        Course course = subCourse.getCourse();
        course.setCode(updateCourseRequest.getCourseCode());
        course.setName(updateCourseRequest.getCourseName());
        course.setDescription(updateCourseRequest.getCourseDescription());

        if (updateCourseRequest.getCategoryId() != null) {
            Category category = categoryRepository.findById(updateCourseRequest.getCategoryId())
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CATEGORY_NOT_FOUND_BY_ID) + updateCourseRequest.getCategoryId()));
            List<Subject> subjects = category.getSubjects();
            subjects.forEach(subject -> {
                if (subject.getId().equals(updateCourseRequest.getSubjectId())) {
                    course.setSubject(subject);
                }
            });
        }

        if (updateCourseRequest.getImageId() != null) {

            Image image = imageRepository.findById(updateCourseRequest.getImageId())
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                            .withMessage(messageUtil.getLocalMessage(IMAGE_NOT_FOUND_BY_ID) + updateCourseRequest.getImageId()));

            subCourse.setImage(image);
        }

        subCourse.setTitle(updateCourseRequest.getSubCourseTitle());
        subCourse.setLevel(updateCourseRequest.getLevel());
        subCourse.setPrice(updateCourseRequest.getPrice());
        subCourse.setStartDateExpected(updateCourseRequest.getStartDateExpected());
        subCourse.setEndDateExpected(updateCourseRequest.getEndDateExpected());
        subCourse.setMinStudent(updateCourseRequest.getMinStudent());
        subCourse.setMaxStudent(updateCourseRequest.getMaxStudent());
        subCourse.setTypeLearn(updateCourseRequest.getType());
        subCourse.setLevel(updateCourseRequest.getLevel());


        subCourse.setMentor(currentUserAccountLogin);
        List<TimeInWeekRequest> timeInWeekRequests = updateCourseRequest.getTimeInWeekRequests();
        TimeInWeekRequest duplicateElement = ObjectUtil.isHasDuplicate(timeInWeekRequests);
        if (duplicateElement == null) {
            List<Long> slotIds = timeInWeekRequests.stream().map(TimeInWeekRequest::getSlotId).collect(Collectors.toList());
            List<Long> dowIds = timeInWeekRequests.stream().map(TimeInWeekRequest::getDayOfWeekId).collect(Collectors.toList());
            Map<Long, Slot> slotMap = slotRepository.findAllById(slotIds).stream().collect(Collectors.toMap(Slot::getId, Function.identity()));
            Map<Long, DayOfWeek> dayOfWeekMap = dayOfWeekRepository.findAllById(dowIds).stream().collect(Collectors.toMap(DayOfWeek::getId, Function.identity()));
            for (TimeInWeekRequest timeInWeekRequest : timeInWeekRequests) {
                TimeInWeek timeInWeek = new TimeInWeek();
                DayOfWeek dayOfWeek = Optional.ofNullable(dayOfWeekMap.get(timeInWeekRequest.getDayOfWeekId()))
                        .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(DAY_OF_WEEK_COULD_NOT_BE_FOUND));
                timeInWeek.setDayOfWeek(dayOfWeek);

                Slot slot = Optional.ofNullable(slotMap.get(timeInWeekRequest.getSlotId()))
                        .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(SLOT_COULD_NOT_BE_FOUND));
                timeInWeek.setSlot(slot);

                timeInWeek.setSubCourse(subCourse);
                subCourse.addTimeInWeek(timeInWeek);
            }

        } else {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(SCHEDULE_AND_SLOT_HAVE_BEEN_OVERLAPPED);
        }
        subCourseRepository.save(subCourse);
        return true;
    }


    @Override
    public Boolean mentorDeleteCourse(Long subCourseId) {
        User user = MentorUtil.checkIsMentor();
        SubCourse subCourse = subCourseRepository.findById(subCourseId).
                orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + subCourseId));

        if (!subCourse.getMentor().getMentorProfile().equals(user.getMentorProfile())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(COURSE_DOES_NOT_BELONG_TO_THE_TEACHER));
        }
        Optional.ofNullable(subCourse.getImage())
                .map(Image::getId)
                .ifPresent(imageRepository::deleteById);

        subCourse.setCourse(null);
        subCourseRepository.delete(subCourse);
        return true;
    }

    @Override
    public List<CourseDto> getCoursePublic() {
        List<Course> coursesTypePublic = courseRepository.findAllByType(ECourseType.PUBLIC);
        return coursesTypePublic.stream()
                .map(ConvertUtil::convertCourseToCourseDTO)
                .collect(Collectors.toList());
    }


    @Override
    public Boolean mentorRequestApprovalCourse(Long subCourseId) {
        User user = MentorUtil.checkIsMentor();
        SubCourse subCourse = subCourseRepository.findById(subCourseId).
                orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + subCourseId));

        Boolean isValidCourse = CourseUtil.checkCourseValid(subCourse, user);

        if (isValidCourse) {
            subCourse.setStatus(WAITING);
            subCourseRepository.save(subCourse);
            return true;
        }
        return false;
    }

    @Override
    public ApiPage<CourseSubCourseResponse> coursePendingToApprove(ECourseStatus status, Pageable pageable) {
        if (status.equals(REQUESTING)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(USER_NOT_HAVE_PERMISSION_TO_VIEW_THIS_COURSE));
        }
        Page<SubCourse> subCoursesPedingPage = null;
        if (status.equals(ALL)) {
            subCoursesPedingPage = subCourseRepository.findByStatusNot(REQUESTING, pageable);
        } else {
            subCoursesPedingPage = subCourseRepository.findByStatus(status, pageable);
        }
        return PageUtil.convert(subCoursesPedingPage.map(ConvertUtil::subCourseToCourseSubCourseResponseConverter));
    }

    @Transactional
    @Override
    public Boolean managerApprovalCourseRequest(Long subCourseId, ManagerApprovalCourseRequest approvalCourseRequest) {

        SubCourse subCourse = subCourseRepository.findById(subCourseId).
                orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + subCourseId));


        validateApprovalCourseRequest(approvalCourseRequest.getStatus());

        if (subCourse.getStatus() != WAITING) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(COURSE_STATUS_NOT_ALLOW));
        }

        subCourse.setStatus(approvalCourseRequest.getStatus());
        subCourseRepository.save(subCourse);

        ActivityHistoryUtil.logHistoryForCourseApprove(subCourseId, approvalCourseRequest.getMessage());

        return true;
    }

    private void validateApprovalCourseRequest(ECourseStatus statusRequest) {
        List<ECourseStatus> ALLOWED_STATUSES = Arrays.asList(NOTSTART, EDITREQUEST, REJECTED);
        if (!ALLOWED_STATUSES.contains(statusRequest)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(COURSE_STATUS_NOT_ALLOW));

        }
    }


    @Override
    public Boolean mentorUploadImageCourse(ImageRequest imageRequest) {
        ImageUtil.uploadImage(imageRequest);
        return true;
    }

    @Override
    public Boolean memberRegisterCourse(Long id) {
        User userLogin = SecurityUtil.getCurrentUser();
        return true;
    }


}
