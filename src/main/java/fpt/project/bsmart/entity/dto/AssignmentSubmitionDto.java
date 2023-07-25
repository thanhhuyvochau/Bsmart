package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.BaseEntity;
import fpt.project.bsmart.entity.response.StudentClassResponse;

import java.util.ArrayList;
import java.util.List;


public class AssignmentSubmitionDto extends BaseEntity {
    private Long id;
    private Float point;
    private String note;
    private StudentClassResponse studentClass;
    private List<AssignmentFileDto> assignmentFiles = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPoint() {
        return point;
    }

    public void setPoint(Float point) {
        this.point = point;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<AssignmentFileDto> getAssignmentFiles() {
        return assignmentFiles;
    }

    public void setAssignmentFiles(List<AssignmentFileDto> assignmentFiles) {
        this.assignmentFiles = assignmentFiles;
    }

    public StudentClassResponse getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(StudentClassResponse studentClass) {
        this.studentClass = studentClass;
    }
}
