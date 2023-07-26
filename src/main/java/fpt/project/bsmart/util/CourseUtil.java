package fpt.project.bsmart.util;


import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.constant.EOrderStatus;
import fpt.project.bsmart.entity.dto.mentor.MentorDto;
import fpt.project.bsmart.entity.response.Class.ManagerGetCourseClassResponse;
import fpt.project.bsmart.entity.response.ClassDetailResponse;

import fpt.project.bsmart.entity.response.MentorGetCourseClassResponse;

import fpt.project.bsmart.repository.ClassRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static fpt.project.bsmart.entity.constant.ECourseStatus.EDITREQUEST;
import static fpt.project.bsmart.entity.constant.ECourseStatus.REQUESTING;
import static fpt.project.bsmart.util.Constants.ErrorMessage.COURSE_DOES_NOT_BELONG_TO_THE_TEACHER;
import static fpt.project.bsmart.util.Constants.ErrorMessage.SUB_COURSE_STATUS_NOT_ALLOW;
import static fpt.project.bsmart.entity.constant.ECourseStatus.*;
import static fpt.project.bsmart.util.Constants.ErrorMessage.*;

@Component
public class CourseUtil {
    private static MessageUtil staticMessageUtil;
    private static String successIcon;

    private static String failIcon;

    private static ClassRepository staticClassRepository;

    public CourseUtil(ClassRepository classRepository, MessageUtil messageUtil) {
        staticMessageUtil= messageUtil ;
        staticClassRepository = classRepository;
    }

    public static void checkCourseOwnership(Course course, User user) {
        if (!course.getCreator().equals(user)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(staticMessageUtil.getLocalMessage(COURSE_DOES_NOT_BELONG_TO_THE_TEACHER));
        }

        if (!course.getStatus().equals(EDITREQUEST) && !course.getStatus().equals(REQUESTING)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(staticMessageUtil.getLocalMessage(SUB_COURSE_STATUS_NOT_ALLOW));
        }
    }


    public static String generateRandomCode(int length) {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            int randomIndex = (int) (Math.random() * alphabet.length());
            char randomChar = alphabet.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public static Boolean checkCourseValidToSendApproval(Course course, User user, List<Class> classesInRequest) {
        if (!course.getStatus().equals(REQUESTING) && !course.getStatus().equals(EDITREQUEST)) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(staticMessageUtil.getLocalMessage(COURSE_STATUS_NOT_ALLOW));
        }

        // kiểm tra có pha mentor của khóa học đó không
        MentorProfile mentorProfile = user.getMentorProfile();
        if (!course.getCreator().getMentorProfile().equals(mentorProfile)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(staticMessageUtil.getLocalMessage(COURSE_DOES_NOT_BELONG_TO_THE_TEACHER));
        }


        // Kiem tra khóa học có nội dung chưa
        if (course.getActivities().size() == 0) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(staticMessageUtil.getLocalMessage(THE_COURSE_HAS_NO_CONTENT) +  course.getId());
        }

        // kiểm tra course có  lớp học chưa
        List<Class> classes = course.getClasses();
        if (classes.size() == 0) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(staticMessageUtil.getLocalMessage(THE_COURSE_HAS_NO_CLASS_UNABLE_TO_SUBMIT_APPROVAL_REQUEST));
        }


        // Kiểm tra lớp học có thời khóa biểu chưaz`
        for (Class aClass : classesInRequest) {
            if (!aClass.getStatus().equals(REQUESTING) && !aClass.getStatus().equals(EDITREQUEST)) {
                throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(staticMessageUtil.getLocalMessage(CLASSES_ARE_CURRENTLY_STARTING_CANNOT_NOT_REQUEST_TO_APPROVAL));
            }
            if (aClass.getTimeTables() == null) {
                throw ApiException.create(HttpStatus.BAD_REQUEST)
                        .withMessage(staticMessageUtil.getLocalMessage(THE_CLASS_HAS_NO_TIME_TABLE) + aClass.getId());
            }

        }

        return true;
    }

    public static Boolean checkCourseValidToApproval(Course course, List<Class> classesInRequest) {
        if (!course.getStatus().equals(WAITING)) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(staticMessageUtil.getLocalMessage(COURSE_STATUS_NOT_ALLOW));
        }


        // Kiem tra khóa học có nội dung chưa
        if (course.getActivities().size() == 0) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(staticMessageUtil.getLocalMessage(THE_COURSE_HAS_NO_CONTENT) +  course.getId());
        }

        // kiểm tra course có  lớp học chưa
        List<Class> classes = course.getClasses();
        if (classes.size() == 0) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(staticMessageUtil.getLocalMessage(THE_COURSE_HAS_NO_CLASS_UNABLE_TO_SUBMIT_APPROVAL_REQUEST));
        }


        // Kiểm tra lớp học có thời khóa biểu chưaz
        for (Class aClass : classesInRequest) {
            if (!aClass.getStatus().equals(WAITING)) {
                throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(staticMessageUtil.getLocalMessage(CLASS_STATUS_NOT_ALLOW));
            }
            if (aClass.getTimeTables() == null) {
                throw ApiException.create(HttpStatus.BAD_REQUEST)
                        .withMessage(staticMessageUtil.getLocalMessage(THE_CLASS_HAS_NO_TIME_TABLE) + aClass.getId());
            }

        }

        return true;
    }

    public static Boolean isPaidCourse(Class clazz, User user) {
        List<User> allPaidSubCourseUsers = clazz.getOrderDetails().stream()
                .filter(orderDetail -> orderDetail.getOrder().getStatus().equals(EOrderStatus.SUCCESS))
                .map(orderDetail -> orderDetail.getOrder().getUser()).collect(Collectors.toList());
        return allPaidSubCourseUsers.contains(user);
    }

    public static Boolean isFullMemberOfSubCourse(Class clazz) {
        long numberOfBought = clazz.getOrderDetails().stream()
                .filter(orderDetail -> orderDetail.getOrder().getStatus().equals(EOrderStatus.SUCCESS)).count();
        return numberOfBought == clazz.getMaxStudent();
    }

    public static MentorGetCourseClassResponse setCourseInformationForCourseDetailPage(Course course) {

        MentorGetCourseClassResponse response = ObjectUtil.copyProperties(course, new MentorGetCourseClassResponse(), MentorGetCourseClassResponse.class);
        response.setSubjectResponse(ConvertUtil.convertSubjectToSubjectDto(course.getSubject()));
        Subject subject = course.getSubject();
        if (subject != null) {
            response.setSubjectResponse(ConvertUtil.convertSubjectToSubjectDto(subject));

            Set<Category> categories = subject.getCategories();
            if (!categories.isEmpty()) {
                for (Category category : categories) {

                    response.setCategoryResponse(ConvertUtil.convertCategoryToCategoryDto(category));
                }
            }
        }
        if (course.getCreator() != null) {
            MentorDto mentorDto = MentorUtil.convertUserToMentorDto(course.getCreator());
            response.setMentor(mentorDto);
        }


        return response;
    }

    public static MentorGetCourseClassResponse convertCourseToCourseClassResponsePage(Course course) {

        MentorGetCourseClassResponse courseResponse = new MentorGetCourseClassResponse();
        courseResponse.setId(course.getId());
        courseResponse.setName(course.getName());
        courseResponse.setCode(course.getCode());
        courseResponse.setDescription(course.getDescription());
        courseResponse.setStatus(course.getStatus());
        courseResponse.setLevel(course.getLevel());
        if (course.getCreator() != null) {
            MentorDto mentorDto = MentorUtil.convertUserToMentorDto(course.getCreator());
            courseResponse.setMentor(mentorDto);
        }


        Subject subject = course.getSubject();
        if (subject != null) {
            courseResponse.setSubjectResponse(ConvertUtil.convertSubjectToSubjectDto(subject));

            Set<Category> categories = subject.getCategories();
            if (!categories.isEmpty()) {
                for (Category category : categories) {

                    courseResponse.setCategoryResponse(ConvertUtil.convertCategoryToCategoryDto(category));
                }
            }
        }

        List<ClassDetailResponse> classDetailResponses = new ArrayList<>();
        List<Class> classes = staticClassRepository.findByCourseAndStatus(course, ECourseStatus.NOTSTART);
        classes.forEach(aClass -> {

            classDetailResponses.add(ClassUtil.convertClassToClassDetailResponse(course.getCreator(), aClass));
        });
        courseResponse.setClasses(classDetailResponses);

        return courseResponse;
    }

    public static ManagerGetCourseClassResponse convertCourseToCourseClassResponseManager(Course course) {


        ManagerGetCourseClassResponse courseResponse = new ManagerGetCourseClassResponse();
        courseResponse.setId(course.getId());
        courseResponse.setName(course.getName());
        courseResponse.setCode(course.getCode());
        courseResponse.setDescription(course.getDescription());
        courseResponse.setStatus(course.getStatus());
        courseResponse.setLevel(course.getLevel());
        if (course.getCreator() != null) {
            MentorDto mentorDto = MentorUtil.convertUserToMentorDto(course.getCreator());
            courseResponse.setMentor(mentorDto);
        }


        Subject subject = course.getSubject();
        if (subject != null) {
            courseResponse.setSubjectResponse(ConvertUtil.convertSubjectToSubjectDto(subject));

            Set<Category> categories = subject.getCategories();
            if (!categories.isEmpty()) {
                for (Category category : categories) {

                    courseResponse.setCategoryResponse(ConvertUtil.convertCategoryToCategoryDto(category));
                }
            }
        }

        List<ClassDetailResponse> classDetailResponses = new ArrayList<>();
        List<Class> classes = staticClassRepository.findByCourseAndStatus(course, ECourseStatus.WAITING);
        classes.forEach(aClass -> {
            classDetailResponses.add(ClassUtil.convertClassToClassDetailResponse(course.getCreator(), aClass));
        });
        courseResponse.setClasses(classDetailResponses);

        return courseResponse;
    }


}