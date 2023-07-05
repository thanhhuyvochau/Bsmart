package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.ECourseActivityType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "activity")
public class Activity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "type")
    private ECourseActivityType type;
    @Column(name = "is_visible")
    private Boolean visible;
    @OneToOne(mappedBy = "activity", cascade = CascadeType.ALL)
    private Quiz quiz;
    @OneToOne(mappedBy = "activity", cascade = CascadeType.ALL)
    private Assignment assignment;
    @OneToOne(mappedBy = "activity", cascade = CascadeType.ALL)
    private Lesson lesson;
    @OneToOne(mappedBy = "activity", cascade = CascadeType.ALL)
    private Resource resource;
    @OneToOne(mappedBy = "activity", cascade = CascadeType.ALL)
    private ClassAnnouncement announcement;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL)
    private List<Section> sections;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Activity parent;
    @OneToMany(mappedBy = "parent")
    private List<Activity> children = new ArrayList<>();
    @OneToMany(mappedBy = "authorizeClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityAuthorize> activityAuthorizes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public Activity(String name, ECourseActivityType type, Boolean visible, Activity parent, List<ActivityAuthorize> activityAuthorizes, Course course) {
        this.name = name;
        this.type = type;
        this.visible = visible;
        this.parent = parent;
        this.activityAuthorizes = activityAuthorizes;
        this.course = course;
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


    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
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

    public ECourseActivityType getType() {
        return type;
    }

    public void setType(ECourseActivityType type) {
        this.type = type;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public Activity getParent() {
        return parent;
    }

    public void setParent(Activity parent) {
        this.parent = parent;
    }

    public List<Activity> getChildren() {
        return children;
    }

    public void setChildren(List<Activity> children) {
        this.children = children;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public ClassAnnouncement getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(ClassAnnouncement announcement) {
        this.announcement = announcement;
    }

    public List<ActivityAuthorize> getActivityAuthorizes() {
        return activityAuthorizes;
    }

    public void setActivityAuthorizes(List<ActivityAuthorize> activityAuthorizes) {
        this.activityAuthorizes = activityAuthorizes;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
