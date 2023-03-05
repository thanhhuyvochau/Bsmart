package fpt.project.bsmart.entity.request;

import java.util.List;

public class CourseSectionRequest {
    private String name;

    private List<CourseModuleRequest> modules;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CourseModuleRequest> getModules() {
        return modules;
    }

    public void setModules(List<CourseModuleRequest> modules) {
        this.modules = modules;
    }
}
