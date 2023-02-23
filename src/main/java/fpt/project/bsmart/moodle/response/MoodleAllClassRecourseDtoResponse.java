package fpt.project.bsmart.moodle.response;

import java.util.List;

public class MoodleAllClassRecourseDtoResponse {

    private Long classId;
    private String className;
    private String codeName;


    private List<MoodleRecourseDtoResponse> resources;


    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public List<MoodleRecourseDtoResponse> getResources() {
        return resources;
    }

    public void setResources(List<MoodleRecourseDtoResponse> resources) {
        this.resources = resources;
    }
}
