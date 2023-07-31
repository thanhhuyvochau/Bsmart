package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "time_table_id")
    private TimeTable timeTable;
    @ManyToOne
    @JoinColumn(name = "student_class_id")
    private StudentClass studentClass;
    @Column(name = "present")
    private Boolean present;
    @Column(name = "note")
    private String note;

    public Attendance(TimeTable timeTable, StudentClass studentClass, Boolean present, String note) {
        this.timeTable = timeTable;
        this.studentClass = studentClass;
        this.present = present;
        this.note = note;
    }

    public Attendance() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
    }

    public StudentClass getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(StudentClass studentClass) {
        this.studentClass = studentClass;
    }

    public Boolean getPresent() {
        return present;
    }

    public void setPresent(Boolean present) {
        this.present = present;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendance that = (Attendance) o;
        return Objects.equals(id, that.id) && Objects.equals(timeTable, that.timeTable) && Objects.equals(studentClass, that.studentClass) && Objects.equals(present, that.present) && Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timeTable, studentClass, present, note);
    }
}
