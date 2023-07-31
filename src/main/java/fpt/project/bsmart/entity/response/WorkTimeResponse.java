package fpt.project.bsmart.entity.response;

import fpt.project.bsmart.entity.constant.EUserRole;

import java.util.List;


public class WorkTimeResponse {
    private SimpleClassResponse workingClass;
    private EUserRole role;
    private List<TimeTableResponse> timeTableResponse;

    public WorkTimeResponse(SimpleClassResponse workingClass, EUserRole role, List<TimeTableResponse> timeTableResponse) {
        this.workingClass = workingClass;
        this.role = role;
        this.timeTableResponse = timeTableResponse;
    }

    public SimpleClassResponse getWorkingClass() {
        return workingClass;
    }

    public void setWorkingClass(SimpleClassResponse workingClass) {
        this.workingClass = workingClass;
    }

    public EUserRole getRole() {
        return role;
    }

    public void setRole(EUserRole role) {
        this.role = role;
    }

    public List<TimeTableResponse> getTimeTableResponse() {
        return timeTableResponse;
    }

    public void setTimeTableResponse(List<TimeTableResponse> timeTableResponse) {
        this.timeTableResponse = timeTableResponse;
    }
}
