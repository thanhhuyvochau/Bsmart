package fpt.project.bsmart.moodle.request;


import java.util.ArrayList;
import java.util.List;

public class DeleteMoodleCourseRequest {

    private List<Long> courseids = new ArrayList<>();

    public List<Long> getCourseids() {
        return courseids;
    }

    public void setCourseids(List<Long> courseids) {
        this.courseids = courseids;
    }
}
