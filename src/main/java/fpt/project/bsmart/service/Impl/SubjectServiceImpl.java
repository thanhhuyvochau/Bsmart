package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Category;
import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.Subject;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.request.subject.SubjectRequest;
import fpt.project.bsmart.repository.CategoryRepository;
import fpt.project.bsmart.repository.CourseRepository;
import fpt.project.bsmart.repository.SubjectRepository;
import fpt.project.bsmart.service.ISubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl implements ISubjectService {
    private static String SUBJECT_NOT_FOUND_MESSAGE = "Subject nay khong ton tai";
    private static String CATEGORY_NOT_FOUND_MESSAGE = "Category nay khong ton tai";

    private final SubjectRepository subjectRepository;
    private final CategoryRepository categoryRepository;
    private final CourseRepository courseRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository, CategoryRepository categoryRepository, CourseRepository courseRepository){
        this.subjectRepository = subjectRepository;
        this.categoryRepository = categoryRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Subject> FindAll() {
        return subjectRepository.findAll();
    }

    @Override
    public Subject FindSubjectById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(SUBJECT_NOT_FOUND_MESSAGE));
    }

    @Override
    public Subject FindSubjectByName(String name) {
        return subjectRepository.findSubjectByName(name)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(SUBJECT_NOT_FOUND_MESSAGE));
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
        Subject subject = FindSubjectById(id);
        subject.setName(subjectRequest.getName());
        subject.setCode(subjectRequest.getCode());
        subject.setCategory(category);
        return subjectRepository.save(subject).getId();
    }

    public void DeleteSubject(Long id){
        Subject subject = FindSubjectById(id);
        List<Course> courseList = courseRepository.findCourseBySubject(subject);
        if(!courseList.isEmpty()){
            for (Course course : courseList){
                course.setSubject(null);
            }
        }
        subjectRepository.deleteById(id);
    }

}
