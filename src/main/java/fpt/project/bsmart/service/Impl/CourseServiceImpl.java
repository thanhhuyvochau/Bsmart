package fpt.project.bsmart.service.Impl;


import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.entity.dto.CourseDto;
import fpt.project.bsmart.entity.request.CourseModuleRequest;
import fpt.project.bsmart.entity.request.CourseSectionRequest;
import fpt.project.bsmart.entity.request.CreateCourseRequest;
import fpt.project.bsmart.entity.request.ImageRequest;
import fpt.project.bsmart.entity.response.CourseDetailResponse;
import fpt.project.bsmart.entity.response.CourseResponse;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.ICourseService;
import fpt.project.bsmart.util.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.*;
import static fpt.project.bsmart.util.ConvertUtil.convertCourseToCourseDTO;
import static fpt.project.bsmart.util.ConvertUtil.convertCourseToCourseDetailResponse;


@Service
public class CourseServiceImpl implements ICourseService {
    private final CategoryRepository categoryRepository;
    private final MessageUtil messageUtil;

    private final CourseRepository courseRepository;

    private final SubjectRepository subjectRepository;

    private final ImageRepository imageRepository;


    public CourseServiceImpl(CategoryRepository categoryRepository, MessageUtil messageUtil, CourseRepository courseRepository, SubjectRepository subjectRepository, ImageRepository imageRepository) {
        this.categoryRepository = categoryRepository;
        this.messageUtil = messageUtil;
        this.courseRepository = courseRepository;
        this.subjectRepository = subjectRepository;
        this.imageRepository = imageRepository;
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

        Course course = new Course();
        course.setName(createCourseRequest.getName());
        course.setLevel(createCourseRequest.getLevel());
        course.setDescription(createCourseRequest.getDescription());
        Category category = categoryRepository.findById(createCourseRequest.getCategoryId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CATEGORY_NOT_FOUND_BY_ID) + createCourseRequest.getCategoryId()));

        List<Subject> subjects = category.getSubjects();
        subjects.forEach(subject -> {
            if (subject.getId().equals(createCourseRequest.getSubjectId())) {
                course.setSubject(subject);
            }
        });
        // hình
        Image image = imageRepository.findById(createCourseRequest.getImageId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CATEGORY_NOT_FOUND_BY_ID) + createCourseRequest.getCategoryId()));
        course.setImage(image);
        User currentUserAccountLogin = SecurityUtil.getCurrentUserAccountLogin();

        course.setMentor(currentUserAccountLogin);

        course.setDescription(createCourseRequest.getDescription());
        course.setStatus(ECourseStatus.REQUESTING);

        List<Role> roles = currentUserAccountLogin.getRoles();
        List<Boolean> checkRoleTeacher = roles.stream().map(role -> role.getCode().equals(EUserRole.TEACHER)).collect(Collectors.toList());
        if (checkRoleTeacher.isEmpty()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Người dùng không phải là giáo viên"));
        }


        List<CourseSectionRequest> sectionsRequestList = createCourseRequest.getSections();
        List<Section> sectionList = new ArrayList<>();
        sectionsRequestList.forEach(sectionRequest -> {
            Section section = new Section();
            section.setName(sectionRequest.getName());
            List<CourseModuleRequest> modulesList = sectionRequest.getModules();
            List<Module> moduleList = new ArrayList<>();
            modulesList.forEach(moduleRequest -> {
                Module module = new Module();
                module.setName(moduleRequest.getName());
                moduleList.add(module);
            });
            section.setModules(moduleList);
            sectionList.add(section);
        });
        course.setSections(sectionList);
        Course save = courseRepository.save(course);
        return save.getId();
    }

    @Override
    public ApiPage<CourseDto> mentorGetCourse(Pageable pageable) {
        User userLogin = SecurityUtil.getCurrentUserAccountLogin();
        Page<Course> allCourseMentor = courseRepository.findByMentor(userLogin, pageable);

        return PageUtil.convert(allCourseMentor.map(ConvertUtil::convertCourseToCourseDTO));

    }

    @Override
    public ApiPage<CourseResponse> getCourseForCoursePage(Pageable pageable) {
        Page<Course> Courses = courseRepository.findByStatus(ECourseStatus.NOTSTART, pageable);
        return PageUtil.convert(Courses.map(ConvertUtil::convertCourseCourseResponse));
    }

    @Override
    public CourseDetailResponse getDetailCourseForCoursePage(Long id) {
        Course course = courseRepository.findByIdAndStatus(id, ECourseStatus.NOTSTART)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(COURSE_NOT_FOUND_BY_ID) + id));
        return convertCourseToCourseDetailResponse(course);
    }

    @Override
    public Boolean mentorUploadImageCourse(ImageRequest imageRequest) {
        ImageUtil.uploadImage(imageRequest);
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
