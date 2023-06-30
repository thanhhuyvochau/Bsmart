package fpt.project.bsmart.service.Impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Module;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.constant.ECourseType;

import fpt.project.bsmart.entity.constant.EDayOfWeekCode;
import fpt.project.bsmart.entity.dto.CourseDto;
import fpt.project.bsmart.entity.dto.course.CourseContentDto;
import fpt.project.bsmart.entity.dto.module.ModuleDto;
import fpt.project.bsmart.entity.dto.section.SectionDto;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static fpt.project.bsmart.entity.constant.ECourseStatus.*;
import static fpt.project.bsmart.util.Constants.ErrorMessage.*;
import static fpt.project.bsmart.util.ConvertUtil.convertCourseSubCourseToCourseSubCourseDetailResponse;


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
    public List<Long> mentorCreateCoursePrivate(CreateCourseRequest createCourseRequest) {
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();

        // create info for course
        Course course = createCourseFromRequest(createCourseRequest);
        return createSubCourseAndTimeInWeek(currentUserAccountLogin, course, createCourseRequest);
    }


    @Override
    public List<Long> mentorCreateCoursePublic(Long id, CreateCourseRequest createCourseRequest) {
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));
        return createSubCourseAndTimeInWeek(currentUserAccountLogin, course, createCourseRequest);
    }

    private List<Long> createSubCourseAndTimeInWeek(User currentUserAccountLogin, Course course, CreateCourseRequest createCourseRequest) {
        // check mentor account is valid
        MentorUtil.checkIsMentor();

        course.setStatus(REQUESTING);

        List<Long> subCourseId = new ArrayList<>();
        List<CreateSubCourseRequest> subCourseRequestsList = createCourseRequest.getSubCourseRequests();
        List<SubCourse> subCourses = new ArrayList<>();
        subCourseRequestsList.forEach(createSubCourseRequest -> {
            List<TimeInWeekRequest> timeInWeekRequests = createSubCourseRequest.getTimeInWeekRequests();

            // create time in week for subCourse
            List<TimeInWeek> timeInWeeksFromRequest = createTimeInWeeksFromRequest(timeInWeekRequests);

            // create subCourse for course
            SubCourse subCourseFromRequest = createSubCourseFromRequest(createSubCourseRequest, currentUserAccountLogin, timeInWeeksFromRequest);
            subCourseFromRequest.setCourse(course);


            subCourses.add(subCourseFromRequest);


        });
        course.setSubCourses(subCourses);
        courseRepository.save(course);


        // ghi log
        subCourses.forEach(subCourse -> {
                    subCourseId.add(subCourse.getId());
                    ActivityHistoryUtil.logHistoryForCourseCreated(currentUserAccountLogin.getId(), subCourse);
                }
        );


        return subCourseId;
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
        course.setCode(CourseUtil.generateRandomCode(8));
        course.setDescription(createCourseRequest.getDescription());
        course.setSubject(subject);

        return course;
    }

    private SubCourse createSubCourseFromRequest(CreateSubCourseRequest subCourseRequest, User currentUserAccountLogin, List<TimeInWeek> timeInWeeks) {
        if (subCourseRequest.getPrice() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(PLEASE_ENTER_THE_PRICE_FOR_THE_COURSE));
        }
        SubCourse subCourse = new SubCourse();
        subCourse.setNumberOfSlot(subCourseRequest.getNumberOfSlot());
        subCourse.setTypeLearn(subCourseRequest.getType());
        subCourse.setMinStudent(subCourseRequest.getMinStudent());
        subCourse.setMaxStudent(subCourseRequest.getMaxStudent());
        subCourse.setStartDateExpected(subCourseRequest.getStartDateExpected());
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
        subCourse.setTimeInWeeks(timeInWeeks);

        if (subCourseRequest.getEndDateExpected() != null) {
            Instant endDateExpected = subCourseRequest.getEndDateExpected();
            int numberOfSlot = calNumberOfSlotByEndDate(subCourseRequest.getStartDateExpected(), endDateExpected, timeInWeeks);
            subCourse.setNumberOfSlot(numberOfSlot);
            subCourse.setEndDateExpected(endDateExpected);
        } else if (subCourseRequest.getNumberOfSlot() != null) {
            Integer numberOfSlot = subCourseRequest.getNumberOfSlot();
            Instant endDateExpected = calEndDateByNumberOfSlot(subCourseRequest.getStartDateExpected(), numberOfSlot, timeInWeeks);
            subCourse.setNumberOfSlot(numberOfSlot);
            subCourse.setEndDateExpected(endDateExpected);
        } else {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Lỗi không tìm thấy số lượng slot học hoặc ngày kết thúc");
        }
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
    public ApiPage<CourseSubCourseResponse> mentorGetAllCourse(ECourseStatus courseStatus, Pageable pageable) {
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
    public CourseSubCourseResponse mentorGetCourse(Long subCourseId) {
        SubCourse subCourse = subCourseRepository.findById(subCourseId).
                orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + subCourseId));
        return ConvertUtil.subCourseToCourseSubCourseResponseConverter(subCourse);

    }

    @Transactional()
    @Override
    public Boolean mentorCreateContentCourse(Long id, List<CourseContentDto> request) throws JsonProcessingException {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));

        List<Section> sections = new ArrayList<>();
        request.forEach(courseContent -> {

            SectionDto sectionContent = courseContent.getSections();
            Section section = new Section();
            section.setName(sectionContent.getName());
            ;

            List<Module> modules = new ArrayList<>();
            List<ModuleDto> modulesContent = sectionContent.getModules();
            modulesContent.forEach(moduleContent -> {
                Module module = new Module();
                module.setName(moduleContent.getName());
                module.setSection(section);
                modules.add(module);
            });
            section.getModules().addAll(modules);
            section.setCourse(course);
            sections.add(section);
        });

        course.getSections().addAll(sections);
        courseRepository.save(course);


        return true;
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


    @Transactional
    @Override
    public Boolean mentorUpdateCourse(Long subCourseId, UpdateSubCourseRequest updateCourseRequest) {
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();
        MentorProfile mentorProfile = MentorUtil.getCurrentUserMentorProfile(currentUserAccountLogin);
        SubCourse subCourse = getSubCourseById(subCourseId);
        CourseUtil.checkCourseOwnership(subCourse, mentorProfile);
        updateCourseFromRequest(subCourse, updateCourseRequest);
        if (shouldUpdateImageCourse(subCourse, updateCourseRequest.getImageId())) {
            updateImageCourse(subCourse, updateCourseRequest);
        }
        List<TimeInWeek> timeInWeeks = updateTimeInWeekFromRequest(subCourse, updateCourseRequest);
        updateSubCourseFromRequest(subCourse, updateCourseRequest, currentUserAccountLogin, timeInWeeks);
        subCourse.getTimeInWeeks().clear();
        subCourse.getTimeInWeeks().addAll((timeInWeeks));
        subCourseRepository.save(subCourse);
        return true;
    }

    private boolean shouldUpdateImageCourse(SubCourse subCourse, Long imageId) {
        return !subCourse.getImage().getId().equals(imageId);
    }


    private SubCourse getSubCourseById(Long subCourseId) {
        return subCourseRepository.findById(subCourseId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + subCourseId));
    }

    private void updateCourseFromRequest(SubCourse subCourse, UpdateSubCourseRequest updateCourseRequest) {
        if (subCourse.getCourse().getType().equals(ECourseType.PRIVATE)) {
            Course course = subCourse.getCourse();
            course.setCode(updateCourseRequest.getCourseCode());
            course.setName(updateCourseRequest.getCourseName());
            course.setDescription(updateCourseRequest.getCourseDescription());
            Category category = categoryRepository.findById(updateCourseRequest.getCategoryId())
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CATEGORY_NOT_FOUND_BY_ID) + updateCourseRequest.getCategoryId()));
            Set<Subject> subjects = category.getSubjects();
            subjects.forEach(subject -> {
                if (subject.getId().equals(updateCourseRequest.getSubjectId())) {
                    course.setSubject(subject);
                }
            });
            subCourse.setCourse(course);
        }

    }

    private void updateImageCourse(SubCourse subCourse, UpdateSubCourseRequest updateCourseRequest) {
        Image image = imageRepository.findById(subCourse.getImage().getId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(IMAGE_NOT_FOUND_BY_ID) + subCourse.getImage().getId()));

        List<SubCourse> subCourseByImage = subCourseRepository.findAllByImage(image);
        if (subCourseByImage.size() == 1) {
            Optional.ofNullable(subCourse.getImage())
                    .map(Image::getId)
                    .ifPresent(imageRepository::deleteById);
        } else {
            subCourse.setImage(null);
        }
        Image imageUpdated = imageRepository.findById(subCourse.getImage().getId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(IMAGE_NOT_FOUND_BY_ID) + updateCourseRequest.getImageId()));

        subCourse.setImage(imageUpdated);
    }

    private void updateSubCourseFromRequest(SubCourse subCourse, UpdateSubCourseRequest updateCourseRequest, User currentUserAccountLogin, List<TimeInWeek> timeInWeeks) {
        subCourse.setTitle(updateCourseRequest.getSubCourseTitle());
        subCourse.setLevel(updateCourseRequest.getLevel());
        subCourse.setPrice(updateCourseRequest.getPrice());
        subCourse.setStartDateExpected(updateCourseRequest.getStartDateExpected());
        subCourse.setMinStudent(updateCourseRequest.getMinStudent());
        subCourse.setMaxStudent(updateCourseRequest.getMaxStudent());
        subCourse.setTypeLearn(updateCourseRequest.getType());
        subCourse.setLevel(updateCourseRequest.getLevel());
        subCourse.setMentor(currentUserAccountLogin);

        if (updateCourseRequest.getEndDateExpected() != null) {
            Instant endDateExpected = updateCourseRequest.getEndDateExpected();
            int numberOfSlot = calNumberOfSlotByEndDate(updateCourseRequest.getStartDateExpected(), endDateExpected, timeInWeeks);
            subCourse.setNumberOfSlot(numberOfSlot);
            subCourse.setEndDateExpected(endDateExpected);
        } else if (updateCourseRequest.getNumberOfSlot() != null) {
            Integer numberOfSlot = updateCourseRequest.getNumberOfSlot();
            Instant endDateExpected = calEndDateByNumberOfSlot(updateCourseRequest.getStartDateExpected(), numberOfSlot, timeInWeeks);
            subCourse.setNumberOfSlot(numberOfSlot);
            subCourse.setEndDateExpected(endDateExpected);
        } else {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Lỗi không tìm thấy số lượng slot học hoặc ngày kết thúc");
        }
    }


    private List<TimeInWeek> updateTimeInWeekFromRequest(SubCourse subCourse, UpdateSubCourseRequest updateCourseRequest) {
        List<TimeInWeekRequest> timeInWeekRequests = updateCourseRequest.getTimeInWeekRequests();
        TimeInWeekRequest duplicateElement = ObjectUtil.isHasDuplicate(timeInWeekRequests);
        if (duplicateElement != null) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(SCHEDULE_AND_SLOT_HAVE_BEEN_OVERLAPPED);
        }
        List<Long> slotIds = timeInWeekRequests.stream().map(TimeInWeekRequest::getSlotId).collect(Collectors.toList());
        List<Long> dowIds = timeInWeekRequests.stream().map(TimeInWeekRequest::getDayOfWeekId).collect(Collectors.toList());
        Map<Long, Slot> slotMap = slotRepository.findAllById(slotIds).stream().collect(Collectors.toMap(Slot::getId, Function.identity()));
        Map<Long, DayOfWeek> dayOfWeekMap = dayOfWeekRepository.findAllById(dowIds).stream().collect(Collectors.toMap(DayOfWeek::getId, Function.identity()));
        List<TimeInWeek> timeInWeeks = new ArrayList<>();
        for (TimeInWeekRequest timeInWeekRequest : timeInWeekRequests) {
            DayOfWeek dayOfWeek = dayOfWeekMap.get(timeInWeekRequest.getDayOfWeekId());
            if (dayOfWeek == null) {
                throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(DAY_OF_WEEK_COULD_NOT_BE_FOUND);
            }
            Slot slot = slotMap.get(timeInWeekRequest.getSlotId());
            if (slot == null) {
                throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(SLOT_COULD_NOT_BE_FOUND);
            }
            TimeInWeek timeInWeek = new TimeInWeek();
            timeInWeek.setDayOfWeek(dayOfWeek);
            timeInWeek.setSlot(slot);
            timeInWeek.setSubCourse(subCourse);
            timeInWeeks.add(timeInWeek);
        }
        return timeInWeeks;
    }


    @Transactional
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

        if (subCourse.getImage() != null) {
            Image image = imageRepository.findById(subCourse.getImage().getId())
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                            .withMessage(messageUtil.getLocalMessage(IMAGE_NOT_FOUND_BY_ID) + subCourse.getImage().getId()));

            List<SubCourse> subCourseByImage = subCourseRepository.findAllByImage(image);
            if (subCourseByImage.size() == 1) {
                Optional.ofNullable(subCourse.getImage())
                        .map(Image::getId)
                        .ifPresent(imageRepository::deleteById);
            } else {
                subCourse.setImage(null);
            }
        }

        if (subCourse.getCourse().getType().equals(ECourseType.PRIVATE)) {
            courseRepository.delete(subCourse.getCourse());
        } else {
            subCourse.setCourse(null);
        }

        ActivityHistoryUtil.logHistoryForCourseDeleted(user.getId(), subCourse);
        subCourseRepository.delete(subCourse);

        return true;
    }

    @Override
    public ApiPage<CourseDto> getCoursePublic(Pageable pageable) {
        List<Course> coursesTypePublic = courseRepository.findAllByType(ECourseType.PUBLIC);
        List<CourseDto> courseDtosTypePublic = coursesTypePublic.stream()
                .map(ConvertUtil::convertCourseToCourseDTO)
                .collect(Collectors.toList());


        return PageUtil.convert(new PageImpl<>(courseDtosTypePublic, pageable, courseDtosTypePublic.size()));
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

        // log history
        ActivityHistoryUtil.logHistoryForCourseApprove(subCourse.getMentor().getId(), subCourse, approvalCourseRequest.getMessage());
        subCourseRepository.save(subCourse);
        return true;
    }

    @Override
    public Boolean managerCreateCourse(CreateCoursePublicRequest createCourseRequest) {
        Course courseFromRequest = createCoursePublicFromRequest(createCourseRequest);
        courseFromRequest.setType(ECourseType.PUBLIC);
        courseRepository.save(courseFromRequest);
        return true;
    }


    private Course createCoursePublicFromRequest(CreateCoursePublicRequest coursePublicRequest) {
        Long categoryId = coursePublicRequest.getCategoryId();
        if (categoryId == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(PLEASE_SELECT_THE_CATEGORY_FOR_THE_COURSE));
        }
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(CATEGORY_NOT_FOUND_BY_ID) + categoryId));


        Long subjectId = coursePublicRequest.getSubjectId();
        if (subjectId == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(PLEASE_SELECT_THE_SUBJECT_FOR_THE_COURSE));
        }
        Optional<Subject> optionalSubject = category.getSubjects().stream().filter(s -> s.getId().equals(subjectId)).findFirst();
        Subject subject = optionalSubject.orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                .withMessage(messageUtil.getLocalMessage(SUBJECT_NOT_FOUND_BY_ID) + subjectId));

        Course course = new Course();
        course.setName(coursePublicRequest.getName());
        course.setCode(coursePublicRequest.getCode());
        course.setDescription(coursePublicRequest.getDescription());
        course.setSubject(subject);

        return course;
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
        SecurityUtil.getCurrentUser();
        return true;
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
}
