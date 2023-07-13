package fpt.project.bsmart.moodle.request;

import java.util.ArrayList;
import java.util.List;

public class CreateEnrolCourseRequest {

    private List<Enrolment> enrolments = new ArrayList<>();

    public List<Enrolment> getEnrolments() {
        return enrolments;
    }

    public void setEnrolments(List<Enrolment> enrolments) {
        this.enrolments = enrolments;
    }

    public static class Enrolment {
        private int userid;
        private int courseid;
        private int roleid;

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public int getCourseid() {
            return courseid;
        }

        public void setCourseid(int courseid) {
            this.courseid = courseid;
        }

        public int getRoleid() {
            return roleid;
        }

        public void setRoleid(int roleid) {
            this.roleid = roleid;
        }
    }
}
