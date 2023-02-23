package fpt.project.bsmart.entity;

import javax.persistence.*;

@Entity
@Table(name = "mark")
public class Mark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "student_class_id")
    private StudentClass student;
    @Column(name = "mark")
    private Float mark;
    @Column(name = "max_mark")
    private Float maxMark;
    @Column(name = "min_mark")
    private Float minMark;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentClass getStudent() {
        return student;
    }

    public void setStudent(StudentClass student) {
        this.student = student;
    }

    public Float getMark() {
        return mark;
    }

    public void setMark(Float mark) {
        this.mark = mark;
    }

    public Float getMaxMark() {
        return maxMark;
    }

    public void setMaxMark(Float maxMark) {
        this.maxMark = maxMark;
    }

    public Float getMinMark() {
        return minMark;
    }

    public void setMinMark(Float minMark) {
        this.minMark = minMark;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
}
