package fpt.project.bsmart.moodle.response;

import java.util.List;

public class CourseGradeResponse {
    private List<UserGradeResponse> usergrades;
    private List<Object> warnings;

    public List<UserGradeResponse> getUsergrades() {
        return usergrades;
    }

    public void setUsergrades(List<UserGradeResponse> usergrades) {
        this.usergrades = usergrades;
    }

    public List<Object> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<Object> warnings) {
        this.warnings = warnings;
    }
}
