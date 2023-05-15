package fpt.project.bsmart.entity;

import javax.persistence.*;

@Entity
@Table(name = "activity")
public class Activity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "type_id")
    private ActivityType type;
    @Column(name = "is_visible")
    private Boolean isVisible;
    @OneToOne(mappedBy = "activity")
    private Quiz quiz;
    @OneToOne(mappedBy = "activity", cascade = CascadeType.ALL)
    private Assignment assignment;
    @ManyToOne
    @JoinColumn(name = "class_section_id")
    private ClassSection classSection;

    public Activity(String name, ActivityType type, Boolean isVisible, ClassSection classSection) {
        this.name = name;
        this.type = type;
        this.isVisible = isVisible;
        this.classSection = classSection;
    }

    public Activity() {
    }

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

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Boolean visible) {
        isVisible = visible;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public ClassSection getClassSection() {
        return classSection;
    }

    public void setClassSection(ClassSection classSection) {
        this.classSection = classSection;
    }
}
