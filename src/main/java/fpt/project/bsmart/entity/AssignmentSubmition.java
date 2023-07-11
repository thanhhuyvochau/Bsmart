package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "activity_submittion")
public class AssignmentSubmition extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "point")
    private Float point;
    @Column(name = "note")
    private String note;
    @ManyToOne
    @JoinColumn(name = "student_class_id")
    private StudentClass studentClass;
    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;
    @OneToMany(mappedBy = "assignmentSubmition",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssignmentFile> assignmentFiles = new ArrayList<>();

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

    public StudentClass getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(StudentClass studentClass) {
        this.studentClass = studentClass;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public List<AssignmentFile> getAssignmentFiles() {
        return assignmentFiles;
    }

    public void setAssignmentFiles(List<AssignmentFile> assignmentFiles) {
        this.assignmentFiles = assignmentFiles;
    }
}
