package fpt.project.bsmart.validator;

import fpt.project.bsmart.entity.Activity;
import fpt.project.bsmart.entity.ActivityAuthorize;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.ECourseActivityType;
import fpt.project.bsmart.entity.request.AddQuizRequest;
import fpt.project.bsmart.util.MessageUtil;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class ActivityValidator {
    private final MessageUtil messageUtil;

    public ActivityValidator(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    public static boolean isValidParentActivity(Activity parent) {
        return Objects.equals(parent.getType(), ECourseActivityType.SECTION);
    }

    public static boolean isActivityBelongCourse(Activity activity, Course course) {
        return course.getActivities().stream().map(Activity::getId).anyMatch(id -> Objects.equals(id, activity.getId()));
    }

    public static boolean isAuthorizeForClass(Class clazz, Activity activity) {
        List<ActivityAuthorize> activityAuthorizes = activity.getActivityAuthorizes();
        return activityAuthorizes.stream().anyMatch(activityAuthorize -> Objects.equals(activityAuthorize.getAuthorizeClass().getId(), clazz.getId()));
    }

    public static void validateQuizInfo(AddQuizRequest addQuizRequest){
        if (addQuizRequest.getCode().trim().isEmpty()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Code is empty");
        }

        if (addQuizRequest.getStartDate().isBefore(Instant.now()) || addQuizRequest.getEndDate().isBefore(Instant.now())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Invalid start date or end date");
        }

        if (addQuizRequest.getStartDate().isAfter(addQuizRequest.getEndDate())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Start day can not after end date");
        }

        if (addQuizRequest.getTime() < 0) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Invalid quiz time");
        }
        if (addQuizRequest.getDefaultPoint() < 0) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Invalid default point " + addQuizRequest.getDefaultPoint());
        }
        if (addQuizRequest.getIsAllowReview() && addQuizRequest.getAllowReviewAfterMin() < 0) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Invalid number of allow review after min: " + addQuizRequest.getAllowReviewAfterMin());
        }

        if (addQuizRequest.getPassword().trim().isEmpty()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Quiz password is empty");
        }
    }

}
