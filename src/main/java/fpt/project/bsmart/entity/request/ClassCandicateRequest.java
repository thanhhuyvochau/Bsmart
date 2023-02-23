package fpt.project.bsmart.entity.request;


import java.io.Serializable;

public class ClassCandicateRequest implements Serializable {
    private Long classId;
    private Long teacherId;

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
}
