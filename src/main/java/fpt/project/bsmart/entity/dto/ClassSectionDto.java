package fpt.project.bsmart.entity.dto;

import java.util.ArrayList;
import java.util.List;


public class ClassSectionDto {


    private Long id;

    private String name;
    private List<ClassModuleDto> classModules = new ArrayList<>();

    private List<ActivityDto> activities = new ArrayList<>();

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

    public List<ClassModuleDto> getClassModules() {
        return classModules;
    }

    public void setClassModules(List<ClassModuleDto> classModules) {
        this.classModules = classModules;
    }

    public List<ActivityDto> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityDto> activities) {
        this.activities = activities;
    }
}
