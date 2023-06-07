package fpt.project.bsmart.entity.request;

import java.util.ArrayList;
import java.util.List;

public class AttendanceRequest {
    private Long timeTableId;
    private List<AttendanceDetailRequest> details = new ArrayList<>();

    public Long getTimeTableId() {
        return timeTableId;
    }

    public void setTimeTableId(Long timeTableId) {
        this.timeTableId = timeTableId;
    }

    public List<AttendanceDetailRequest> getDetails() {
        return details;
    }

    public void setDetails(List<AttendanceDetailRequest> details) {
        this.details = details;
    }
}
