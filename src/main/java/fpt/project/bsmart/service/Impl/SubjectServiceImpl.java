package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Category;
import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.Subject;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.dto.SubjectDTO;
import fpt.project.bsmart.entity.request.subject.SubjectRequest;
import fpt.project.bsmart.repository.CategoryRepository;
import fpt.project.bsmart.repository.CourseRepository;
import fpt.project.bsmart.repository.SubjectRepository;
import fpt.project.bsmart.service.ISubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static fpt.project.bsmart.util.common.HttpMessageUtil.CATEGORY_NOT_FOUND_MESSAGE;
import static fpt.project.bsmart.util.common.HttpMessageUtil.SUBJECT_NOT_FOUND_MESSAGE;

@Service
public class SubjectServiceImpl implements ISubjectService {
    private final SubjectRepository subjectRepository;
    private final CategoryRepository categoryRepository;
    private final CourseRepository courseRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository, CategoryRepository categoryRepository, CourseRepository courseRepository){
        this.subjectRepository = subjectRepository;
        this.categoryRepository = categoryRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public List<SubjectDTO> FindAll() {
        List<Subject> subjects = subjectRepository.findAll();
        List<SubjectDTO> dtos = new ArrayList<>();
        for (Subject subject : subjects){
            dtos.add(mapSubjectToDTO(subject));
        }
        return dtos;
    }

    @Override
    public SubjectDTO FindSubjectById(Long id) {
        return mapSubjectToDTO(findSubjectById(id));
    }

    @Override
    public Long AddSubject(SubjectRequest subjectRequest) {
        Category category = categoryRepository.findById(subjectRequest.getCategoryId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(CATEGORY_NOT_FOUND_MESSAGE));
        Subject subject = new Subject();
        subject.setName(subjectRequest.getName());
        subject.setCode(subjectRequest.getCode());
        subject.setCategory(category);
        return subjectRepository.save(subject).getId();
    }

    @Override
    public Long UpdateSubject(Long id, SubjectRequest subjectRequest) {
        Category category = categoryRepository.findById(subjectRequest.getCategoryId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(CATEGORY_NOT_FOUND_MESSAGE));
        Subject subject = findSubjectById(id);
        subject.setName(subjectRequest.getName());
        subject.setCode(subjectRequest.getCode());
        subject.setCategory(category);
        return subjectRepository.save(subject).getId();
    }

    public void DeleteSubject(Long id){
        Subject subject = findSubjectById(id);
        List<Course> courseList = subject.getCourses();
        if(!courseList.isEmpty()){
            for (Course course : courseList){
                course.setSubject(null);
                courseRepository.save(course);
            }
        }
        subjectRepository.deleteById(id);
    }

    private Subject findSubjectById(Long id){
        return subjectRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(SUBJECT_NOT_FOUND_MESSAGE));
    }

    private SubjectDTO mapSubjectToDTO(Subject subject){
        SubjectDTO dto = new SubjectDTO();
        dto.setId(subject.getId());
        dto.setCode(subject.getCode());
        dto.setName(subject.getName());
        dto.setCategoryId(subject.getCategory().getId());
        return dto;
    }
}
