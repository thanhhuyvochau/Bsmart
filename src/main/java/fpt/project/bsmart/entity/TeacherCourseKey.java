package fpt.project.bsmart.entity;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TeacherCourseKey implements Serializable {

    @Column(name = "teacher_id")
    private Long teachId;
    @Column(name = "course_id")
    private Long courseId;

    public Long getTeachId() {
        return teachId;
    }

    public void setTeachId(Long teachId) {
        this.teachId = teachId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeacherCourseKey that = (TeacherCourseKey) o;
        return Objects.equals(teachId, that.teachId) && Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teachId, courseId);
    }
}
