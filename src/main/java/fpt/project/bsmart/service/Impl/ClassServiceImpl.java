package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.SubCourse;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.request.category.CreateClassRequest;
import fpt.project.bsmart.repository.ClassRepository;
import fpt.project.bsmart.repository.CourseRepository;
import fpt.project.bsmart.repository.SubCourseRepository;
import fpt.project.bsmart.service.IClassService;
import fpt.project.bsmart.util.MessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ClassServiceImpl implements IClassService {


    private final MessageUtil messageUtil;
    private final CourseRepository courseRepository;

    private final ClassRepository classRepository;
    private final SubCourseRepository subCourseRepository;

    public ClassServiceImpl(MessageUtil messageUtil, CourseRepository courseRepository, ClassRepository classRepository, SubCourseRepository subCourseRepository) {
        this.messageUtil = messageUtil;
        this.courseRepository = courseRepository;
        this.classRepository = classRepository;
        this.subCourseRepository = subCourseRepository;
    }

    @Override
    public Boolean createClass(CreateClassRequest request) {
        SubCourse subCourse = subCourseRepository.findById(request.getSubCourseId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm khóa học cần tạo lịch!"));
        Course course = subCourse.getCourse();
        Integer numberOfSlot = course.getNumberOfSlot();
        Instant startDate = request.getNowIsStartDate() ? Instant.now() : request.getStartDate();
        Class clazz = new Class();
        clazz.setStartDate(startDate);
        clazz.setSubCourse(subCourse);
        return true;
    }
}
