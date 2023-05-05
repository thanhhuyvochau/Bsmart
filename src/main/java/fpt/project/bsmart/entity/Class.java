package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "class")
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date")
    private Instant startDate;
    @Column(name = "end_date")
    private Instant endDate;
    @Column(name = "student_number")
    private Integer numberOfStudent = 0;
    @ManyToOne
    @JoinColumn(name = "sub_course_id")
    private SubCourse subCourse;

    @OneToMany(mappedBy = "clazz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeTable> timeTables = new ArrayList<>();
    @OneToMany(mappedBy = "clazz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentClass> studentClasses = new ArrayList<>();
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Integer getNumberOfStudent() {
        return numberOfStudent;
    }

    public void setNumberOfStudent(Integer numberOfStudent) {
        this.numberOfStudent = numberOfStudent;
    }

    public SubCourse getSubCourse() {
        return subCourse;
    }

    public void setSubCourse(SubCourse subCourse) {
        this.subCourse = subCourse;
    }

    public List<TimeTable> getTimeTables() {
        return timeTables;
    }

    public void setTimeTables(List<TimeTable> timeTables) {
        this.timeTables = timeTables;
    }

    public List<StudentClass> getStudentClasses() {
        return studentClasses;
    }

    public void setStudentClasses(List<StudentClass> studentClasses) {
        this.studentClasses = studentClasses;
    }
}