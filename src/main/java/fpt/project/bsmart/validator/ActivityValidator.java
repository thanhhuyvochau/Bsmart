package fpt.project.bsmart.validator;

import fpt.project.bsmart.entity.Activity;
import fpt.project.bsmart.entity.ActivityAuthorize;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.ECourseActivityType;
import fpt.project.bsmart.entity.request.AddQuizRequest;
import fpt.project.bsmart.util.MessageUtil;
import fpt.project.bsmart.util.QuizUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import static fpt.project.bsmart.util.Constants.ErrorMessage.Empty.EMPTY_CODE;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Empty.EMPTY_PASSWORD;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Invalid.*;

@Component
public class ActivityValidator {
    private static MessageUtil messageUtil;

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
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(EMPTY_CODE));
        }

        if (addQuizRequest.getDefaultPoint() < 0
                || addQuizRequest.getDefaultPoint() > 10) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_QUIZ_DEFAULT_POINT) + addQuizRequest.getDefaultPoint());
        }
        if (addQuizRequest.getIsAllowReview()){
            if(addQuizRequest.getAllowReviewAfterMin() < 0
                    || addQuizRequest.getAllowReviewAfterMin() > QuizUtil.MAX_ALLOW_REVIEW_AFTER_MIN) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_ALLOW_REVIEW_AFTER_MIN) + addQuizRequest.getAllowReviewAfterMin());
            }
        }

        if (addQuizRequest.getPassword().trim().isEmpty()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(EMPTY_PASSWORD));
        }
    }

}
