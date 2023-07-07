package fpt.project.bsmart.validator;

import fpt.project.bsmart.entity.Activity;
import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.constant.ECourseActivityType;

import java.util.Objects;

public class ActivityValidator {
    public static boolean isValidParentActivity(Activity parent) {
        return Objects.equals(parent.getType(), ECourseActivityType.SECTION);
    }

    public static boolean isActivityBelongCourse(Activity activity, Course course) {
        return course.getActivities().stream().map(Activity::getId).anyMatch(id -> Objects.equals(id, course.getId()));
    }
}