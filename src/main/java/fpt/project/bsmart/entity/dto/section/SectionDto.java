package fpt.project.bsmart.entity.dto.section;

import fpt.project.bsmart.entity.dto.module.ModuleDto;

import java.util.ArrayList;
import java.util.List;

public class SectionDto {

    private String name;


    private List<ModuleDto> modules = new ArrayList<>();

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
