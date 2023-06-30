package fpt.project.bsmart.util;


import fpt.project.bsmart.entity.MentorProfile;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EOrderStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static fpt.project.bsmart.entity.constant.ECourseStatus.EDITREQUEST;
import static fpt.project.bsmart.entity.constant.ECourseStatus.REQUESTING;
import static fpt.project.bsmart.util.Constants.ErrorMessage.*;

@Component
public class CourseUtil {
    private static MessageUtil messageUtil;
    private static String successIcon;

    private static String failIcon;

    public static void checkCourseOwnership(SubCourse subCourse, MentorProfile mentorProfile) {
        if (!subCourse.getMentor().getMentorProfile().equals(mentorProfile)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(COURSE_DOES_NOT_BELONG_TO_THE_TEACHER));
        }

        if (!subCourse.getStatus().equals(EDITREQUEST) && !subCourse.getStatus().equals(REQUESTING)) {
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

    public static Boolean checkCourseValid(SubCourse subCourse, User user) {
        if (!subCourse.getStatus().equals(REQUESTING)) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(COURSE_STATUS_NOT_ALLOW);
        }
        MentorProfile mentorProfile = user.getMentorProfile();
        if (!subCourse.getMentor().getMentorProfile().equals(mentorProfile)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(COURSE_DOES_NOT_BELONG_TO_THE_TEACHER));
        }

        if (subCourse.getStatus() != EDITREQUEST && subCourse.getStatus() != REQUESTING) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(COURSE_STATUS_NOT_ALLOW));
        }

        return true;
    }

    public static Boolean isPaidCourse(SubCourse subCourse, User user) {
        List<User> allPaidSubCourseUsers = subCourse.getOrderDetails().stream()
                .filter(orderDetail -> orderDetail.getOrder().getStatus().equals(EOrderStatus.SUCCESS))
                .map(orderDetail -> orderDetail.getOrder().getUser()).collect(Collectors.toList());
        return allPaidSubCourseUsers.contains(user);
    }

    public static Boolean isFullMemberOfSubCourse(SubCourse subCourse) {
        long numberOfBought = subCourse.getOrderDetails().stream()
                .filter(orderDetail -> orderDetail.getOrder().getStatus().equals(EOrderStatus.SUCCESS)).count();
        return numberOfBought == subCourse.getMaxStudent();
    }
}