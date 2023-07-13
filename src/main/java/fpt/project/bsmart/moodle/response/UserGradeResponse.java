package fpt.project.bsmart.moodle.response;

import java.util.List;

public class UserGradeResponse {
    public int courseid;
    public String courseidnumber;
    public int userid;
    public String userfullname;
    public String useridnumber;
    public int maxdepth;
    public List<GradeItemResponse> gradeitems;

    public int getCourseid() {
        return courseid;
    }

    public void setCourseid(int courseid) {
        this.courseid = courseid;
    }

    public String getCourseidnumber() {
        return courseidnumber;
    }

    public void setCourseidnumber(String courseidnumber) {
        this.courseidnumber = courseidnumber;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUserfullname() {
        return userfullname;
    }

    public void setUserfullname(String userfullname) {
        this.userfullname = userfullname;
    }

    public String getUseridnumber() {
        return useridnumber;
    }

    public void setUseridnumber(String useridnumber) {
        this.useridnumber = useridnumber;
    }

    public int getMaxdepth() {
        return maxdepth;
    }

    public void setMaxdepth(int maxdepth) {
        this.maxdepth = maxdepth;
    }

    public List<GradeItemResponse> getGradeitems() {
        return gradeitems;
    }

    public void setGradeitems(List<GradeItemResponse> gradeitems) {
        this.gradeitems = gradeitems;
    }
}
