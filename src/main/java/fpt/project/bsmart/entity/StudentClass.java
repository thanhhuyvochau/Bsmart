package fpt.project.bsmart.entity;

import javax.persistence.*;

@Entity
@Table(name = "student_class")
public class StudentClass {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "clazz")
    private Class clazz;
    @ManyToOne
    @JoinColumn(name = "student")
    private User student;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class classId) {
        this.clazz = classId;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User studentId) {
        this.student = studentId;
    }
}
