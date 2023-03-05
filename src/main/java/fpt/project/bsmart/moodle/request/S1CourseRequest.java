package fpt.project.bsmart.moodle.request;

import java.util.List;

public class S1CourseRequest {

    private List<CreateCourseRequest.CreateCourseBody> courses;

    public List<CreateCourseRequest.CreateCourseBody> getCourses() {
        return courses;
    }

    public void setCourses(List<CreateCourseRequest.CreateCourseBody> courses) {
        this.courses = courses;
    }
}
