package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.common.ECandicateStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "class_teacher_candicate")
public class ClassTeacherCandicate extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "class_id")
    @ManyToOne
    private Class clazz;
    @JoinColumn(name = "teacher_id")
    @ManyToOne
    private Account teacher;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ECandicateStatus status;
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
    private List<TeachingConfirmation> teachingConfirmations = new ArrayList<>();

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

    public Account getTeacher() {
        return teacher;
    }

    public void setTeacher(Account teacher) {
        this.teacher = teacher;
    }

    public ECandicateStatus getStatus() {
        return status;
    }

    public void setStatus(ECandicateStatus status) {
        this.status = status;
    }

    public List<TeachingConfirmation> getTeachingConfirmations() {
        return teachingConfirmations;
    }

    public void setTeachingConfirmations(List<TeachingConfirmation> teachingConfirmations) {
        this.teachingConfirmations = teachingConfirmations;
    }
}
