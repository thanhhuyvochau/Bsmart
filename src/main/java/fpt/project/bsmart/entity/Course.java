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

    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;


    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private User mentor;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private List<SubCourse> subCourses;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
