package fpt.project.bsmart.entity;


import fpt.project.bsmart.entity.constant.ECourseLevel;
import fpt.project.bsmart.entity.constant.ECourseStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ECourseStatus status;


    @Column(name = "reference_discount")
    private Double referenceDiscount = 0.0;




    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private User mentor;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<SubCourse> subCourses;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public ECourseStatus getStatus() {
        return status;
    }

    public void setStatus(ECourseStatus status) {
        this.status = status;
    }

    public Double getReferenceDiscount() {
        return referenceDiscount;
    }

    public void setReferenceDiscount(Double referenceDiscount) {
        this.referenceDiscount = referenceDiscount;
    }



    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public User getMentor() {
        return mentor;
    }

    public void setMentor(User mentor) {
        this.mentor = mentor;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<SubCourse> getSubCourses() {
        return subCourses;
    }

    public void setSubCourses(List<SubCourse> subCourses) {
        this.subCourses = subCourses;
    }
}
