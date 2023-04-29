package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.dto.ClassProgressTimeDto;
import fpt.project.bsmart.entity.request.category.CreateClassRequest;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.IClassService;
import fpt.project.bsmart.util.ClassUtil;
import fpt.project.bsmart.util.MessageUtil;
import fpt.project.bsmart.util.TimeInWeekUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClassServiceImpl implements IClassService {
    private final MessageUtil messageUtil;
    private final CourseRepository courseRepository;

    private final ClassRepository classRepository;
    private final SubCourseRepository subCourseRepository;

    private final OrderDetailRepository orderDetailRepository;

    public ClassServiceImpl(MessageUtil messageUtil, CourseRepository courseRepository, ClassRepository classRepository, SubCourseRepository subCourseRepository, OrderDetailRepository orderDetailRepository) {
        this.messageUtil = messageUtil;
        this.courseRepository = courseRepository;
        this.classRepository = classRepository;
        this.subCourseRepository = subCourseRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public Boolean createClass(CreateClassRequest request) {
        SubCourse subCourse = subCourseRepository.findById(request.getSubCourseId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm khóa học cần tạo lịch!"));
        Integer numberOfSlot = subCourse.getNumberOfSlot();
        Instant startDate = request.getNowIsStartDate() ? Instant.now() : request.getStartDate();

        // set information cho class
        Class clazz = new Class();
        clazz.setStartDate(startDate);
        clazz.setSubCourse(subCourse);
//        List<User> usersBySubCourse = orderDetailRepository.findUsersBySubCourse(subCourse);
        StudentClass studentClass = new StudentClass() ;
        studentClass.setClazz(clazz);
//        usersBySubCourse.forEach(studentClass::setStudent) ;
        List<TimeTable> timeTables = TimeInWeekUtil.generateTimeTable(subCourse.getTimeInWeeks(), numberOfSlot, startDate, clazz);
        clazz.getTimeTables().addAll(timeTables);
        classRepository.save(clazz);
        return true;
    }

    @Override
    public ClassProgressTimeDto getClassProgression(Long clazzId) {
        Class clazz = classRepository.findById(clazzId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy lớp với id:" + clazzId));
        if (clazz.getStartDate().isBefore(Instant.now())) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage("Lớp học chưa tới thời gian bắt đầu!");
        }
        return Optional.ofNullable(ClassUtil.getPercentageOfClassTime(clazz)).orElseThrow(() -> ApiException.create(HttpStatus.CONFLICT).withMessage("Đã có lỗi xảy ra vui lòng thử lại"));
    }
}
