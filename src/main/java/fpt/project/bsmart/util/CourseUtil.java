package fpt.project.bsmart.util;


import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EQuestionType;
import fpt.project.bsmart.entity.constant.ETransactionStatus;
import fpt.project.bsmart.entity.constant.ETypeLearn;
import fpt.project.bsmart.entity.dto.*;
import fpt.project.bsmart.entity.response.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static fpt.project.bsmart.entity.constant.ECourseStatus.EDITREQUEST;
import static fpt.project.bsmart.entity.constant.ECourseStatus.REQUESTING;
import static fpt.project.bsmart.util.Constants.ErrorMessage.COURSE_DOES_NOT_BELONG_TO_THE_TEACHER;
import static fpt.project.bsmart.util.Constants.ErrorMessage.COURSE_STATUS_NOT_ALLOW;

@Component
public class CourseUtil {
    private static MessageUtil messageUtil;
    private static String successIcon;

    private static String failIcon;

    public static Boolean checkCourseValid(SubCourse subCourse ,User user ){
        if (!subCourse.getStatus().equals(REQUESTING)) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(COURSE_STATUS_NOT_ALLOW);
        }
        MentorProfile mentorProfile = user.getMentorProfile();
        if (!subCourse.getMentor().getMentorProfile().equals(mentorProfile)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(COURSE_DOES_NOT_BELONG_TO_THE_TEACHER));
        }

        if (subCourse.getStatus() != EDITREQUEST || subCourse.getStatus() != REQUESTING) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(COURSE_STATUS_NOT_ALLOW));
        }

        return true;
    }


}