package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "section")
public class Section extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "clazz_id")
    private Class clazz;
    @Column(name = "name")
    private String name;
    @Column(name = "is_visible")
    private Boolean isVisible;
    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private List<Module> modules = new ArrayList<>();
    @OneToOne(mappedBy = "section")
    private ForumLesson forumLesson;
    @Column(name = "moodle_section_id")
    private Long moodleId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public ForumLesson getForumLesson() {
        return forumLesson;
    }

    public void setForumLesson(ForumLesson forumLesson) {
        this.forumLesson = forumLesson;
    }

    public Long getMoodleId() {
        return moodleId;
    }

    public void setMoodleId(Long moodleId) {
        this.moodleId = moodleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(id, section.id) && Objects.equals(clazz, section.clazz) && Objects.equals(name, section.name) && Objects.equals(isVisible, section.isVisible) && Objects.equals(modules, section.modules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clazz, name, isVisible, modules);
    }

}
