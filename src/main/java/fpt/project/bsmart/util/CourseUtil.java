package fpt.project.bsmart.util;


import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.constant.EOrderStatus;
import fpt.project.bsmart.entity.dto.CategoryDto;
import fpt.project.bsmart.entity.dto.SubjectDto;
import fpt.project.bsmart.entity.response.ClassDetailResponse;
import fpt.project.bsmart.entity.response.CourseClassResponse;
import fpt.project.bsmart.repository.ClassRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fpt.project.bsmart.entity.constant.ECourseStatus.EDITREQUEST;
import static fpt.project.bsmart.entity.constant.ECourseStatus.REQUESTING;
import static fpt.project.bsmart.util.Constants.ErrorMessage.*;

@Component
public class CourseUtil {
    private static MessageUtil messageUtil;
    private static String successIcon;

    private static String failIcon;

    private static ClassRepository staticClassRepository;

    public CourseUtil(ClassRepository classRepository) {
        staticClassRepository = classRepository;
    }

    public static void checkCourseOwnership(Course course, User user) {
        if (!course.getCreator().equals(user)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(COURSE_DOES_NOT_BELONG_TO_THE_TEACHER));
        }

        if (!course.getStatus().equals(EDITREQUEST) && !course.getStatus().equals(REQUESTING)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(SUB_COURSE_STATUS_NOT_ALLOW));
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

//    public static Boolean checkCourseValid(SubCourse subCourse, User user) {
//        if (!subCourse.getStatus().equals(REQUESTING)) {
//            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(COURSE_STATUS_NOT_ALLOW);
//        }
//        MentorProfile mentorProfile = user.getMentorProfile();
//        if (!subCourse.getMentor().getMentorProfile().equals(mentorProfile)) {
//            throw ApiException.create(HttpStatus.BAD_REQUEST)
//                    .withMessage(messageUtil.getLocalMessage(COURSE_DOES_NOT_BELONG_TO_THE_TEACHER));
//        }
//
//        if (subCourse.getStatus() != EDITREQUEST && subCourse.getStatus() != REQUESTING) {
//            throw ApiException.create(HttpStatus.BAD_REQUEST)
//                    .withMessage(messageUtil.getLocalMessage(COURSE_STATUS_NOT_ALLOW));
//        }
//
//        return true;
//    }

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

    public static CourseClassResponse setCourseInformationForCourseDetailPage(Course course) {

        CourseClassResponse response = ObjectUtil.copyProperties(course, new CourseClassResponse(), CourseClassResponse.class);
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
            response.setMentorName(course.getCreator().getFullName());
        }

        return response;
    }

    public static CourseClassResponse convertCourseToCourseClassResponsePage(Course course) {


        CourseClassResponse courseResponse = new CourseClassResponse();
        courseResponse.setId(course.getId());
        courseResponse.setName(course.getName());
        courseResponse.setCode(course.getCode());
        courseResponse.setDescription(course.getDescription());
        courseResponse.setStatus(course.getStatus());
        courseResponse.setLevel(course.getLevel());
        if (course.getCreator() != null) {
            courseResponse.setMentorName(course.getCreator().getFullName());
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
}