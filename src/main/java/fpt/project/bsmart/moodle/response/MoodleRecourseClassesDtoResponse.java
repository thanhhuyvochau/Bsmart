package fpt.project.bsmart.moodle.response;

import java.util.List;

public class MoodleRecourseClassesDtoResponse {

    private Long classId ;
    private String className;

    private String classCode;

    private List<MoodleRecourseDtoResponse> modules ;

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

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public List<MoodleRecourseDtoResponse> getModules() {
        return modules;
    }

    public void setModules(List<MoodleRecourseDtoResponse> modules) {
        this.modules = modules;
    }
}
