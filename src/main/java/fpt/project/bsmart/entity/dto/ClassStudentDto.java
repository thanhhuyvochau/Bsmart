package fpt.project.bsmart.entity.dto;



import java.util.List;

public class ClassStudentDto {

    private Long classId ;
    private List<StudentDto> students ;

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public List<StudentDto> getStudents() {
        return students;
    }

    public void setStudents(List<StudentDto> students) {
        this.students = students;
    }
}
