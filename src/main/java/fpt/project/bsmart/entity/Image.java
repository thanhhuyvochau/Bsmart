package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.EImageType;

import javax.persistence.*;

@Entity
@Table(name = "image")
public class Image extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "type")
    private EImageType type;
    @Column(name = "name")
    private String note;

    @Column(name = "status")
    private boolean status;

    @OneToOne(mappedBy = "image")
    private Course course;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EImageType getType() {
        return type;
    }

    public void setType(EImageType type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
