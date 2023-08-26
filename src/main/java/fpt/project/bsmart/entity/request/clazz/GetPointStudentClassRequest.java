package fpt.project.bsmart.entity.request.clazz;

public class GetPointStudentClassRequest {

    private Long classId ;

    private Long studentId ;

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
