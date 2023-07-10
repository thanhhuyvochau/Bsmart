package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "student_class")
public class StudentClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class clazz;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;
    @OneToMany(mappedBy = "studentClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendanceList = new ArrayList<>();
    @OneToMany(mappedBy = "studentClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssignmentSubmition> submitAssignment = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public List<Attendance> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(List<Attendance> attendanceList) {
        this.attendanceList = attendanceList;
    }

    public List<AssignmentSubmition> getSubmitAssignment() {
        return submitAssignment;
    }

    public void setSubmitAssignment(List<AssignmentSubmition> submitAssignment) {
        this.submitAssignment = submitAssignment;
    }
}

