package fpt.project.bsmart.validator;

import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.User;

import java.util.Objects;

public class CourseValidator {
    public static boolean isMentorOfCourse(User currentUser, Course course) {
        User mentor = course.getCreator();
        return Objects.equals(mentor.getId(), currentUser.getId());
    }
}
