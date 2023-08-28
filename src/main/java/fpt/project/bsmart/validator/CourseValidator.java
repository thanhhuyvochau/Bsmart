package fpt.project.bsmart.validator;

import fpt.project.bsmart.entity.Activity;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.ECourseActivityType;
import fpt.project.bsmart.entity.constant.ECourseClassStatus;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class CourseValidator {

    public static boolean isMentorOfCourse(User currentUser, Course course) {
        User mentor = course.getCreator();
        return Objects.equals(mentor.getId(), currentUser.getId());
    }

    public static void checkEmptySectionOfCourseActivity(List<Activity> activityList) {
        List<Activity> sectionActivities = activityList.stream()
                .filter(activity -> Objects.equals(activity.getType(), ECourseActivityType.SECTION) && activity.getFixed())
                .collect(Collectors.toList());
        for (Activity sectionActivity : sectionActivities) {
            if (sectionActivity.getChildren().isEmpty()) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Không thể để trống nội dung của học phần, vui lòng bổ sung");
            }
        }
    }

    public static boolean checkValidStateToReturnWaitingStatus(Course course) {
        List<Class> classes = course.getClasses();
        long numberOfOperatingClasses = classes.stream().filter(clazz -> clazz.getStatus().equals(ECourseClassStatus.NOTSTART) || clazz.getStatus().equals(ECourseClassStatus.STARTING)).count();
        return numberOfOperatingClasses <= 0;
    }
}
