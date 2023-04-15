package fpt.project.bsmart.service.Impl;


import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.constant.ECourseStatus;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static fpt.project.bsmart.entity.constant.ECourseStatus.*;
import static fpt.project.bsmart.util.Constants.ErrorMessage.*;
import static fpt.project.bsmart.util.ConvertUtil.*;


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
    public List<CourseDto> getCoursesBySubject(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(SUBJECT_NOT_FOUND_BY_ID) + id));
        List<CourseDto> courseDtoList = new ArrayList<>();
        if (!subject.getCourses().isEmpty()) {
            for (Course course : subject.getCourses()) {
                courseDtoList.add(convertCourseToCourseDTO(course));
            }
        }
        return courseDtoList;
    }

    @Override
    public Long mentorCreateCourse(CreateCourseRequest createCourseRequest) {
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();



        Course course = new Course();

        Category category = categoryRepository.findById(createCourseRequest.getCategoryId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CATEGORY_NOT_FOUND_BY_ID) + createCourseRequest.getCategoryId()));
        if (createCourseRequest.getCategoryId() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Vui lòng chọn lĩnh vực cho khoá học"));
        }

        if (createCourseRequest.getSubjectId() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Vui lòng chọn môn học cho khoá học"));
        }

        List<Subject> subjects = category.getSubjects();
        subjects.forEach(subject -> {
            if (subject.getId().equals(createCourseRequest.getSubjectId())) {
                course.setSubject(subject);
            }
        });
        course.setName(createCourseRequest.getName());
        course.setCode(createCourseRequest.getCode());
        course.setDescription(createCourseRequest.getDescription());

        course.setStatus(NOTSTART);

        List<CreateSubCourseRequest> subCourseRequests = createCourseRequest.getSubCourseRequests();
        List<SubCourse> courseList = new ArrayList<>();
        subCourseRequests.forEach(createSubCourseRequest -> {

            if (createSubCourseRequest.getPrice() == null) {
                throw ApiException.create(HttpStatus.BAD_REQUEST)
                        .withMessage(messageUtil.getLocalMessage("Vui lòng nhập tiền cho khóa học"));
            }
            SubCourse subCourse = new SubCourse();
            subCourse.setNumberOfSlot(createSubCourseRequest.getNumberOfSlot());




            subCourse.setTypeLearn(createSubCourseRequest.getType());
            subCourse.setNumberOfSlot(createSubCourseRequest.getNumberOfSlot());
            subCourse.setMinStudent(createSubCourseRequest.getMinStudent());
            subCourse.setMaxStudent(createSubCourseRequest.getMaxStudent());
            subCourse.setStartDateExpected(createSubCourseRequest.getStartDateExpected());
            subCourse.setEndDateExpected(createSubCourseRequest.getEndDateExpected());
            subCourse.setStatus(REQUESTING);
            subCourse.setTitle(createSubCourseRequest.getSubCourseTile());
            subCourse.setPrice(createSubCourseRequest.getPrice());
            subCourse.setLevel(createSubCourseRequest.getLevel());
            subCourse.setMentor(currentUserAccountLogin);
            Image image = imageRepository.findById(createSubCourseRequest.getImageId())
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(IMAGE_NOT_FOUND_BY_ID) + createSubCourseRequest.getImageId()));
            subCourse.setImage(image);
            subCourse.setCourse(course);

            List<Long> slotIds = createSubCourseRequest.getTimeInWeekRequests().stream().map(TimeInWeekRequest::getSlotId).collect(Collectors.toList());
            List<Long> dowIds = createSubCourseRequest.getTimeInWeekRequests().stream().map(TimeInWeekRequest::getDayOfWeekId).collect(Collectors.toList());

            Map<Long, Slot> slotMap = slotRepository.findAllById(slotIds).stream().collect(Collectors.toMap(Slot::getId, Function.identity()));
            Map<Long, DayOfWeek> dayOfWeekMap = dayOfWeekRepository.findAllById(dowIds).stream().collect(Collectors.toMap(DayOfWeek::getId, Function.identity()));


            for (TimeInWeekRequest timeInWeekRequest : createSubCourseRequest.getTimeInWeekRequests()) {
                TimeInWeek timeInWeek = new TimeInWeek();
                timeInWeek.setDayOfWeek(dayOfWeekMap.get(timeInWeekRequest.getDayOfWeekId()));
                timeInWeek.setSlot(slotMap.get(timeInWeekRequest.getSlotId()));
                timeInWeek.setSubCourse(subCourse);
                subCourse.addTimeInWeek(timeInWeek);
            }

            courseList.add(subCourse);
        });



        course.setSubCourses(courseList);


        List<Role> roles = currentUserAccountLogin.getRoles();
        List<Boolean> checkRoleTeacher = roles.stream().map(role -> role.getCode().equals(EUserRole.TEACHER)).collect(Collectors.toList());
        if (checkRoleTeacher.isEmpty()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Người dùng không phải là giáo viên"));
        }


        Course save = courseRepository.save(course);
        return save.getId();
    }

    @Override
    public ApiPage<CourseSubCourseResponse> mentorGetCourse(ECourseStatus status, Pageable pageable) {
        User userLogin = SecurityUtil.getCurrentUser();
        Page<SubCourse> allCourseMentor;
        if (status.equals(ALL)) {
            allCourseMentor = subCourseRepository.findByMentor(userLogin, pageable);
        } else {

            allCourseMentor = subCourseRepository.findByStatusAndMentor(status, userLogin, pageable);
        }
        return PageUtil.convert(allCourseMentor.map(ConvertUtil::convertSubCourseToCourseSubCourseResponse));

    }

    @Override
    public ApiPage<CourseResponse> getCourseForCoursePage(CourseSearchRequest query, Pageable pageable) {


        CourseSpecificationBuilder builder = CourseSpecificationBuilder.specifications()
                .queryLike(query.getQ())
                .queryByCourseStatus(ECourseStatus.NOTSTART)
                .queryBySubCourseType(query.getTypes())
                .queryBySubjectId(query.getSubjectId())
                .queryByCategoryId(query.getCategoryId());


        Page<Course> coursesPage = courseRepository.findAll(builder.build(),pageable);
        List<Course> coursesList = coursesPage.stream().distinct().collect(Collectors.toList());
        List<CourseResponse> courseResponseList = new ArrayList<>();
//        User userLogin = SecurityUtil.getCurrentUser();
        for (Course course : coursesList) {
            courseResponseList.add(convertCourseCourseResponsePage(course));
        }
        Page<CourseResponse> page = new PageImpl<>(courseResponseList);
        return PageUtil.convert(page);

    }

    @Override
    public CourseSubCourseDetailResponse getDetailCourseForCoursePage(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));

        return convertCourseSubCourseToCourseSubCourseDetailResponse(course);
    }


    @Override
    public ApiPage<SubCourseDetailResponse> getAllSubCourseOfCourse(Long id, Pageable pageable) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));

        User userLogin = SecurityUtil.getCurrentUser();

        Page<SubCourse> subCoursesList = subCourseRepository.findByCourse(course, pageable);
        return PageUtil.convert(subCoursesList.map(subCourse -> {
            return ConvertUtil.convertSubCourseToSubCourseDetailResponse(userLogin, subCourse);
        }));
    }

    @Override

    public ApiPage<CourseSubCourseResponse> memberGetCourse(ECourseStatus status, Pageable pageable) {
        User userLogin = SecurityUtil.getCurrentUser();

//    public ApiPage<CourseSubCourseResponse> memberGetCourse(ECourseStatus status ,Pageable pageable) {
//        User userLogin = SecurityUtil.getCurrentUser();

        List<Order> orders = userLogin.getOrder();
        List<SubCourse> subCourses = new ArrayList<>();
        orders.forEach(order -> {
            List<OrderDetail> orderDetails = order.getOrderDetails();
            orderDetails.forEach(orderDetail -> {
                if (orderDetail.getSubCourse() != null) {
                    if (status.equals(ALL)) {
                        subCourses.add(orderDetail.getSubCourse());
                    } else {
                        SubCourse subCourse = orderDetail.getSubCourse();
                        if (subCourse.getStatus().equals(status)) {
                            subCourses.add(orderDetail.getSubCourse());
                        }
                    }
                }
            });
        });
        Page<SubCourse> page = new PageImpl<>(subCourses);

        return PageUtil.convert(page.map(ConvertUtil::convertSubCourseToCourseSubCourseResponse));

    }

    @Override
    public ApiPage<CourseSubCourseResponse> memberGetCourseSuggest( Pageable pageable) {
        User userLogin = SecurityUtil.getCurrentUser();
        Page<SubCourse> subCoursesList;
        if (userLogin == null) {
            subCoursesList = subCourseRepository.findByStatus(NOTSTART, pageable);
        } else {
            subCoursesList = subCourseRepository.findByStatus(NOTSTART, pageable);
        }

        return PageUtil.convert(subCoursesList.map(ConvertUtil::convertSubCourseToCourseSubCourseResponse));

    }

    @Override
    public Boolean mentorUploadImageCourse(ImageRequest imageRequest) {
        ImageUtil.uploadImage(imageRequest);
        return true;
    }

    @Override
    public Boolean memberRegisterCourse(Long id) {
        User userLogin = SecurityUtil.getCurrentUser();
//        Course course = courseRepository.findByIdAndStatus(id, ECourseStatus.NOTSTART)
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));
//        if (!course.getStatus().equals(ECourseStatus.NOTSTART)) {
//            throw ApiException.create(HttpStatus.BAD_REQUEST)
//                    .withMessage(messageUtil.getLocalMessage("Khoá học không tồn tại , vui lòng kiểm tra lại"));
//        }

        return true;
    }


//    @Override
//    public Boolean mentorUploadImageForCourse(Long id, FileDto request) {
//        Course course = courseRepository.findById(id)
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));
//        FileDto fileDto = minioService.uploadFile(request);
//        Image image = new Image();
//        image.setUrl(fileDto.getUrl());
//        image.setType(EImageType.AVATAR);
//        course.setImage(image);
//        courseRepository.save(course);
//        return true;
//    }
}
