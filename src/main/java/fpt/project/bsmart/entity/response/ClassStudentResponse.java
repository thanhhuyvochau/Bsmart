package fpt.project.bsmart.entity.response;


import java.util.List;

public class ClassStudentResponse {

    private List<AccountResponse> students;

    public List<AccountResponse> getStudents() {
        return students;
    }

    public void setStudents(List<AccountResponse> students) {
        this.students = students;
    }
}
