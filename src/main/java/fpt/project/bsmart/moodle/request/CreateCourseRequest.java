package fpt.project.bsmart.moodle.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class CreateCourseRequest implements Serializable {

    private List<CreateCourseBody> courses;

    public static class CreateCourseBody {
        @JsonProperty("fullname")
        private String fullname;

        @JsonProperty("shortname")
        private String shortname;

        @JsonProperty("categoryid")
        private Long categoryid;

        @JsonProperty("startdate")
        private Long startdate;

        @JsonProperty("enddate")
        private Long enddate;

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getShortname() {
            return shortname;
        }

        public void setShortname(String shortname) {
            this.shortname = shortname;
        }

        public Long getCategoryid() {
            return categoryid;
        }

        public void setCategoryid(Long categoryid) {
            this.categoryid = categoryid;
        }

        public Long getStartdate() {
            return startdate;
        }

        public void setStartdate(Long startdate) {
            this.startdate = startdate;
        }

        public Long getEnddate() {
            return enddate;
        }

        public void setEnddate(Long enddate) {
            this.enddate = enddate;
        }
    }

    public List<CreateCourseBody> getCourses() {
        return courses;
    }

    public void setCourses(List<CreateCourseBody> courses) {
        this.courses = courses;
    }
}
