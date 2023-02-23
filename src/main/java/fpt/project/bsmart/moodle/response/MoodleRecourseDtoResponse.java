package fpt.project.bsmart.moodle.response;

import java.util.List;

public class MoodleRecourseDtoResponse {


    private Long id;
    private String name;

    private List<MoodleResourceResponse> modules;

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

    public List<MoodleResourceResponse> getModules() {
        return modules;
    }

    public void setModules(List<MoodleResourceResponse> modules) {
        this.modules = modules;
    }
}
