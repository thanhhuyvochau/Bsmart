package fpt.project.bsmart.service.Impl;


import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.entity.dto.CourseDto;
import fpt.project.bsmart.entity.request.CourseModuleRequest;
import fpt.project.bsmart.entity.request.CourseSectionRequest;
import fpt.project.bsmart.entity.request.CreateCourseRequest;
import fpt.project.bsmart.repository.CategoryRepository;
import fpt.project.bsmart.repository.CourseRepository;
import fpt.project.bsmart.repository.SubjectRepository;
import fpt.project.bsmart.repository.UserRepository;
import fpt.project.bsmart.service.ICourseService;
import fpt.project.bsmart.util.MessageUtil;
import fpt.project.bsmart.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.CATEGORY_NOT_FOUND_BY_ID;
import static fpt.project.bsmart.util.Constants.ErrorMessage.SUBJECT_NOT_FOUND_BY_ID;
import static fpt.project.bsmart.util.ConvertUtil.convertCourseToCourseDTO;


@Service
public class CourseServiceImpl implements ICourseService {
    private final CategoryRepository categoryRepository;
    private final MessageUtil messageUtil;

    private final CourseRepository courseRepository;

    private final SubjectRepository subjectRepository;

    private final UserRepository userRepository;

    public CourseServiceImpl(CategoryRepository categoryRepository, MessageUtil messageUtil, CourseRepository courseRepository, SubjectRepository subjectRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.messageUtil = messageUtil;
        this.courseRepository = courseRepository;
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<CourseDto> getCoursesBySubject(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(SUBJECT_NOT_FOUND_BY_ID) + id));
        List<CourseDto> courseDtoList = new ArrayList<>();
        if(!subject.getCourses().isEmpty()){
            for (Course course : subject.getCourses()){
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
        User currentUserAccountLogin = SecurityUtil.getCurrentUserAccountLogin();

        course.setMentor(currentUserAccountLogin);

        course.setDescription(createCourseRequest.getDescription());


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
}
