package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "class_section")
public class ClassSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class clazz;
    @OneToMany(orphanRemoval = true, mappedBy = "classSection", cascade = CascadeType.ALL)
    private List<ClassModule> classModules = new ArrayList<>();

    @OneToMany(orphanRemoval = true, mappedBy = "classSection", cascade = CascadeType.ALL)
    private List<Activity> activities = new ArrayList<>();

    public ClassSection(String name, Class clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public ClassSection() {
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

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public List<ClassModule> getClassModules() {
        return classModules;
    }

    public void setClassModules(List<ClassModule> classModules) {
        this.classModules = classModules;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
}
