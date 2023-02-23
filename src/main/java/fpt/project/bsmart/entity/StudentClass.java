package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "student_class")
public class StudentClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class aClass;


    @Column(name = "enroll_date")
    private Instant enrollDate;

    @Column(name = "is_enrolled")
    private boolean is_enrolled = false;

    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL)
    private List<Mark> marks = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setAClass(Class aClass) {
        this.aClass = aClass;
    }

    public Instant getEnrollDate() {
        return enrollDate;
    }

    public void setEnrollDate(Instant enrollDate) {
        this.enrollDate = enrollDate;
    }

    public boolean isIs_enrolled() {
        return is_enrolled;
    }

    public void setIs_enrolled(boolean is_enrolled) {
        this.is_enrolled = is_enrolled;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public void setMarks(List<Mark> marks) {
        this.marks = marks;
    }
}