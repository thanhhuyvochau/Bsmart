package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.request.CreateClassRequest;
import fpt.project.bsmart.repository.ClassRepository;
import fpt.project.bsmart.repository.CourseRepository;
import fpt.project.bsmart.service.IClassService;
import fpt.project.bsmart.util.MessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static fpt.project.bsmart.util.Constants.ErrorMessage.CATEGORY_NOT_FOUND_BY_ID;

@Service
public class ClassServiceImpl implements IClassService {


    private final MessageUtil messageUtil;
    private final CourseRepository courseRepository;

    private final ClassRepository classRepository;

    public ClassServiceImpl(MessageUtil messageUtil, CourseRepository courseRepository, ClassRepository classRepository) {
        this.messageUtil = messageUtil;
        this.courseRepository = courseRepository;
        this.classRepository = classRepository;
    }

    @Override
    public Long adminCreateClass(CreateClassRequest createClassRequest) {
        Class classes = new Class();
        classes.setCode(createClassRequest.getCode());
        classes.setStartDate(createClassRequest.getStartDate());
        classes.setEndDate(createClassRequest.getEndDate());
        classes.setMinStudentNumber(createClassRequest.getMinStudentNumber());
        classes.setMaxStudentNumber(createClassRequest.getMaxStudentNumber());
        classes.setExpectedStartDate(createClassRequest.getExpectedStartDate());
        classes.setTypeLearn(createClassRequest.getTypeLearn());
        classes.setPrice(createClassRequest.getPrice());

        Course course = courseRepository.findById(createClassRequest.getCourseId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(CATEGORY_NOT_FOUND_BY_ID) + createClassRequest.getCourseId()));

        classes.setCourse(course);
        Class save = classRepository.save(classes);
        return save.getId();
    }

}
