package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Module;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.constant.EOrderStatus;
import fpt.project.bsmart.entity.dto.ClassProgressTimeDto;
import fpt.project.bsmart.entity.dto.ClassSectionDto;
import fpt.project.bsmart.entity.request.ClassFeedbackRequest;
import fpt.project.bsmart.entity.request.ClassFilterRequest;
import fpt.project.bsmart.entity.request.ClassSectionCreateRequest;
import fpt.project.bsmart.entity.request.ClassSectionUpdateRequest;
import fpt.project.bsmart.entity.request.category.CreateClassRequest;
import fpt.project.bsmart.entity.response.ClassResponse;
import fpt.project.bsmart.entity.response.SimpleClassResponse;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.IClassService;
import fpt.project.bsmart.util.*;
import fpt.project.bsmart.util.specification.ClassSpecificationBuilder;
import fpt.project.bsmart.validator.ClassValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.*;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Invalid.INVALID_CLASS_ID;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Invalid.INVALID_PARAMETER_VALUE;

@Service
@Transactional
public class ClassServiceImpl implements IClassService {
    private final MessageUtil messageUtil;
    private final CourseRepository courseRepository;
    private final OrderDetailRepository orderDetailRepository;

    private final ClassRepository classRepository;
    private final SubCourseRepository subCourseRepository;
    private final ClassSectionRepository classSectionRepository;

    public ClassServiceImpl(MessageUtil messageUtil, CourseRepository courseRepository, OrderDetailRepository orderDetailRepository, ClassRepository classRepository, SubCourseRepository subCourseRepository, ClassSectionRepository classSectionRepository) {
        this.messageUtil = messageUtil;
        this.courseRepository = courseRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.classRepository = classRepository;
        this.subCourseRepository = subCourseRepository;
        this.classSectionRepository = classSectionRepository;
    }

    @Override
    public Boolean createClass(CreateClassRequest request) {
        SubCourse subCourse = subCourseRepository.findById(request.getSubCourseId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(SUB_COURSE_NOT_FOUND_BY_ID) + request.getSubCourseId()));
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
        // Copy module từ course qua làm base chương trình cho lớp học
        List<Section> sections = subCourse.getCourse().getSections();
        for (Section section : sections) {
            ClassSection classSection = new ClassSection(section.getName(), clazz);
            for (Module module : section.getModules()) {
                ClassModule classModule = new ClassModule(module.getName(), classSection);
                classSection.getClassModules().add(classModule);
            }
            clazz.getClassSections().add(classSection);
        }
        classRepository.save(clazz);
        return true;
    }

    @Override
    public ApiPage<SimpleClassResponse> getClassFeedbacks(ClassFeedbackRequest classFeedbackRequest, Pageable pageable) {
        ClassSpecificationBuilder classSpecificationBuilder = ClassSpecificationBuilder.classSpecificationBuilder()
                .searchBySubCourseName(classFeedbackRequest.getSubCourseName())
                .searchByMentorName(classFeedbackRequest.getMentorName())
                .filterByStartDay(classFeedbackRequest.getStartDate())
                .filterByEndDate(classFeedbackRequest.getEndDate());
        Page<Class> classes = classRepository.findAll(classSpecificationBuilder.build(), pageable);
        List<SimpleClassResponse> simpleClassRespons = classes.stream()
                .map(ConvertUtil::convertClassToSimpleClassResponse)
                .collect(Collectors.toList());
        PageImpl<SimpleClassResponse> classResponsePage = new PageImpl<>(simpleClassRespons);
        return PageUtil.convert(classResponsePage);

    }

    @Override
    public ClassProgressTimeDto getClassProgression(Long clazzId) {
        Class clazz = classRepository.findById(clazzId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CLASS_NOT_FOUND_BY_ID) + clazzId));
        if (clazz.getStartDate().isBefore(Instant.now())) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage(messageUtil.getLocalMessage(BEFORE_CLASS_START_TIME));
        }
        return Optional.ofNullable(ClassUtil.getPercentageOfClassTime(clazz)).orElseThrow(() -> ApiException.create(HttpStatus.CONFLICT).withMessage(messageUtil.getLocalMessage(INTERNAL_SERVER_ERROR)));
    }

    @Override
    public ClassResponse getDetailClass(Long id) {
        Class clazz = classRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CLASS_NOT_FOUND_BY_ID) + id));
        User currentUser = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        if (ClassValidator.isMentorOfClass(currentUser, clazz) || ClassValidator.isStudentOfClass(clazz, currentUser)) {
            return ConvertUtil.convertClassToClassResponse(clazz);
        }
        throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(STUDENT_NOT_BELONG_TO_CLASS));
    }

    @Override
    public ApiPage<SimpleClassResponse> getUserClasses(ClassFilterRequest request, Pageable pageable) {
        User user = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        ClassSpecificationBuilder builder = ClassSpecificationBuilder.classSpecificationBuilder();
        builder.searchByClassName(request.getQ())
                .filterByStartDay(request.getStartDate())
                .filterByEndDate(request.getEndDate())
                .filterByStatus(request.getStatus());

        if (request.getAsRole() == 2) {
            builder.byMentor(user);
        } else if (request.getAsRole() == 1) {
            builder.byStudent(user);
        }
        Specification<Class> specification = builder.build();
        Page<Class> classes = classRepository.findAll(specification, pageable);
        return PageUtil.convert(classes.map(ConvertUtil::convertClassToSimpleClassResponse));
    }

    @Override
    public ClassSectionDto createClassSection(ClassSectionCreateRequest request, Long classId) {
        ClassSection classSection = new ClassSection();
        classSection.setName(request.getName());
        Optional<Class> optionalClass = classRepository.findById(classId);
        if (optionalClass.isPresent()) {
            Class clazz = optionalClass.get();
            if (!ClassValidator.isMentorOfClass(SecurityUtil.getCurrentUser(), clazz)) {
                throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(MENTOR_NOT_BELONG_TO_CLASS));
            }
            classSection.setClazz(clazz);
        } else {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(INVALID_CLASS_ID) + classId);
        }
        ClassSection savedClassSection = classSectionRepository.save(classSection);
        return ConvertUtil.convertClassSectionToDto(savedClassSection);
    }

    @Override
    public ClassSectionDto getClassSection(Long classSectionId, Long classId) {
        Optional<ClassSection> optionalClassSection = classSectionRepository.findById(classSectionId);
        if (optionalClassSection.isPresent()) {
            ClassSection classSection = optionalClassSection.get();
            Class clazz = classSection.getClazz();
            if (!Objects.equals(clazz.getId(), classId)) {
                throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(INVALID_PARAMETER_VALUE));
            } else if (!ClassValidator.isMentorOfClass(SecurityUtil.getCurrentUser(), clazz)) {
                throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(MENTOR_NOT_BELONG_TO_CLASS));
            }
            return ConvertUtil.convertClassSectionToDto(classSection);
        } else {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CLASS_SECTION_NOT_FOUND_BY_ID) + classSectionId);
        }
    }

    @Override
    public ClassSectionDto updateClassSection(Long classId, Long classSectionId, ClassSectionUpdateRequest request) {
        Optional<ClassSection> optionalClassSection = classSectionRepository.findById(classSectionId);
        if (optionalClassSection.isPresent()) {
            ClassSection classSection = optionalClassSection.get();
            classSection.setName(request.getName());
            Class clazz = classSection.getClazz();
            if (!Objects.equals(clazz.getId(), classId)) {
                throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(INVALID_PARAMETER_VALUE));
            } else if (!ClassValidator.isMentorOfClass(SecurityUtil.getCurrentUser(), clazz)) {
                throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(MENTOR_NOT_BELONG_TO_CLASS));
            }
            ClassSection updatedClassSection = classSectionRepository.save(classSection);
            return ConvertUtil.convertClassSectionToDto(updatedClassSection);
        } else {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CLASS_SECTION_NOT_FOUND_BY_ID) + classSectionId);
        }
    }

    @Override
    public Boolean deleteClassSection(Long classSectionId, Long classId) {
        Optional<ClassSection> optionalClassSection = classSectionRepository.findById(classSectionId);
        if (optionalClassSection.isPresent()) {
            ClassSection classSection = optionalClassSection.get();
            Class clazz = classSection.getClazz();
            if (!Objects.equals(clazz.getId(), classId)) {
                throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(INVALID_PARAMETER_VALUE));
            } else if (!ClassValidator.isMentorOfClass(SecurityUtil.getCurrentUser(), clazz)) {
                throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(MENTOR_NOT_BELONG_TO_CLASS));
            }
            classSectionRepository.delete(classSection);
        } else {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CLASS_SECTION_NOT_FOUND_BY_ID) + classSectionId);
        }
        return true;
    }
}
