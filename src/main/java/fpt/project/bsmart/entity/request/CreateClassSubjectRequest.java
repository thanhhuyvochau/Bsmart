package fpt.project.bsmart.entity.request;

import java.io.Serializable;


public class CreateClassSubjectRequest implements Serializable {


    private Long subjectId;

    private Long courseId;

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
