package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.SubCourse;
import fpt.project.bsmart.entity.TimeTable;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.ClassProgressTimeDto;
import fpt.project.bsmart.entity.request.ClassFeedbackRequest;
import fpt.project.bsmart.entity.request.category.CreateClassRequest;
import fpt.project.bsmart.entity.response.ClassResponse;
import fpt.project.bsmart.repository.ClassRepository;
import fpt.project.bsmart.repository.CourseRepository;
import fpt.project.bsmart.repository.SubCourseRepository;
import fpt.project.bsmart.service.IClassService;
import fpt.project.bsmart.util.*;
import fpt.project.bsmart.util.specification.ClassFeedbackSpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
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
        Integer numberOfSlot = subCourse.getNumberOfSlot();
        Instant startDate = request.getNowIsStartDate() ? Instant.now() : request.getStartDate();
        Class clazz = new Class();
        clazz.setStartDate(startDate);
        clazz.setSubCourse(subCourse);
        List<TimeTable> timeTables = TimeInWeekUtil.generateTimeTable(subCourse.getTimeInWeeks(), numberOfSlot, startDate, clazz);
        clazz.getTimeTables().addAll(timeTables);
        classRepository.save(clazz);
        return true;
    }

    @Override
    public ApiPage<ClassResponse> getClassFeedbacks(ClassFeedbackRequest classFeedbackRequest, Pageable pageable) {
        ClassFeedbackSpecificationBuilder classFeedbackSpecificationBuilder = ClassFeedbackSpecificationBuilder.classFeedbackSpecificationBuilder()
                .searchBySubCourseName(classFeedbackRequest.getSubCourseName())
                .searchByMentorName(classFeedbackRequest.getMentorName())
                .filterByStartDay(classFeedbackRequest.getStartDate())
                .filterByEndDate(classFeedbackRequest.getEndDate());
        Page<Class> classes = classRepository.findAll(classFeedbackSpecificationBuilder.build(), pageable);
        List<ClassResponse> classResponses = classes.stream()
                .map(ConvertUtil::convertClassToClassResponse)
                .collect(Collectors.toList());
        PageImpl<ClassResponse> classResponsePage = new PageImpl<>(classResponses);
        return PageUtil.convert(classResponsePage);
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
