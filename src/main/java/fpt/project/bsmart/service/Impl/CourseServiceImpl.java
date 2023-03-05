package fpt.project.bsmart.service.Impl;


import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.request.CourseModuleRequest;
import fpt.project.bsmart.entity.request.CourseSectionRequest;
import fpt.project.bsmart.entity.request.CreateCourseRequest;
import fpt.project.bsmart.repository.CategoryRepository;
import fpt.project.bsmart.repository.CourseRepository;
import fpt.project.bsmart.service.ICourseService;
import fpt.project.bsmart.util.MessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static fpt.project.bsmart.util.Constants.ErrorMessage.CATEGORY_NOT_FOUND_BY_ID;


@Service
public class CourseServiceImpl implements ICourseService {
    private final CategoryRepository categoryRepository;
    private final MessageUtil messageUtil;

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CategoryRepository categoryRepository, MessageUtil messageUtil, CourseRepository courseRepository) {
        this.categoryRepository = categoryRepository;
        this.messageUtil = messageUtil;
        this.courseRepository = courseRepository;
    }

    @Override
    public Long mentorCreateCourse(CreateCourseRequest createCourseRequest) {
        Course course = new Course();
        course.setName(createCourseRequest.getName());
//        course.setMentor();  note : do get current login
        course.setDescription(createCourseRequest.getDescription());
        course.setLevel(createCourseRequest.getLevel());
        Category category = categoryRepository.findById(createCourseRequest.getCategoryId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CATEGORY_NOT_FOUND_BY_ID) + createCourseRequest.getCategoryId()));
        List<Subject> subjects = category.getSubjects();
        subjects.forEach(subject -> {
            if (subject.getId().equals(createCourseRequest.getSubjectId())) {
                course.setSubject(subject);
            }
        });
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
