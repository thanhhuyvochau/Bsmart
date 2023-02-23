package fpt.project.bsmart.entity;


import javax.persistence.*;


@Entity
@Table(name = "attendance")
public class Attendance extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    private StudentClassKey studentClassKeyId = new StudentClassKey();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "time_table_id")
    private TimeTable timeTable;

    @Column(name = "is_present")
    private Boolean isPresent;

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

    public Boolean getPresent() {
        return isPresent;
    }

    public void setPresent(Boolean present) {
        isPresent = present;
    }


    public StudentClassKey getStudentClassKeyId() {
        return studentClassKeyId;
    }

    public void setStudentClassKeyId(StudentClassKey studentClassKeyId) {
        this.studentClassKeyId = studentClassKeyId;
    }
}
