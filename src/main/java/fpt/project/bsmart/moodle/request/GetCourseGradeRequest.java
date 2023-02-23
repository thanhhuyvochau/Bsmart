package fpt.project.bsmart.moodle.request;

public class GetCourseGradeRequest {
    private Long courseid;
    private Integer userid;

    public Long getCourseid() {
        return courseid;
    }

    public void setCourseid(Long courseid) {
        this.courseid = courseid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}
