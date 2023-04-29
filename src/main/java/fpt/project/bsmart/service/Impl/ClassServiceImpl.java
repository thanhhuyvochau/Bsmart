package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EOrderStatus;
import fpt.project.bsmart.entity.dto.ClassProgressTimeDto;
import fpt.project.bsmart.entity.request.category.CreateClassRequest;
import fpt.project.bsmart.repository.ClassRepository;
import fpt.project.bsmart.repository.CourseRepository;
import fpt.project.bsmart.repository.OrderDetailRepository;
import fpt.project.bsmart.repository.SubCourseRepository;
import fpt.project.bsmart.service.IClassService;
import fpt.project.bsmart.util.ClassUtil;
import fpt.project.bsmart.util.MessageUtil;
import fpt.project.bsmart.util.TimeInWeekUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClassServiceImpl implements IClassService {
    private final MessageUtil messageUtil;
    private final CourseRepository courseRepository;
    private final OrderDetailRepository orderDetailRepository;

    private final ClassRepository classRepository;
    private final SubCourseRepository subCourseRepository;

    public ClassServiceImpl(MessageUtil messageUtil, CourseRepository courseRepository, OrderDetailRepository orderDetailRepository, ClassRepository classRepository, SubCourseRepository subCourseRepository) {
        this.messageUtil = messageUtil;
        this.courseRepository = courseRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.classRepository = classRepository;
        this.subCourseRepository = subCourseRepository;
    }

    @Override
    public Boolean createClass(CreateClassRequest request) {
        SubCourse subCourse = subCourseRepository.findById(request.getSubCourseId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm khóa học cần tạo lịch!"));
        Integer numberOfSlot = subCourse.getNumberOfSlot();
        Instant startDate = request.getNowIsStartDate() ? Instant.now() : request.getStartDate();
        Class clazz = new Class();
        clazz.setStartDate(startDate);
        clazz.setSubCourse(subCourse);
        List<TimeTable> timeTables = TimeInWeekUtil.generateTimeTable(subCourse.getTimeInWeeks(), numberOfSlot, startDate, clazz);
        clazz.getTimeTables().addAll(timeTables);


        // Thêm học sinh thanh toán thành công vào lớp
        List<OrderDetail> successOrderDetails = orderDetailRepository.findAllBySubCourse(subCourse).stream()
                .filter(orderDetail -> Objects.equals(orderDetail.getOrder().getStatus(), EOrderStatus.SUCCESS))
                .collect(Collectors.toList());
        List<User> successRegisterUsers = successOrderDetails.stream().map(orderDetail -> orderDetail.getOrder().getUser()).collect(Collectors.toList());
        for (User successRegisterUser : successRegisterUsers) {
            StudentClass studentClass = new StudentClass();
            studentClass.setStudent(successRegisterUser);
            studentClass.setClazz(clazz);
            clazz.getStudentClasses().add(studentClass);
        }

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
