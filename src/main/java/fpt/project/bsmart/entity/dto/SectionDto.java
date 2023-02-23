package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;


public class SectionDto extends BaseEntity {


    private Long id;

    private String name;

    private Boolean isVisible;

    private List<ModuleDto> modules = new ArrayList<>();

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
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }

    public List<ModuleDto> getModules() {
        return modules;
    }

    public void setModules(List<ModuleDto> modules) {
        this.modules = modules;
    }
}
