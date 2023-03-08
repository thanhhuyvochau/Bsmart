package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.Module;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;



public class SectionDto {

    private Long id;
    
    private String name;
    
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

    public List<ModuleDto> getModules() {
        return modules;
    }

    public void setModules(List<ModuleDto> modules) {
        this.modules = modules;
    }
}
