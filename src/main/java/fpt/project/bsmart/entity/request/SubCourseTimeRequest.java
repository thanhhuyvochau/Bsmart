package fpt.project.bsmart.entity.request;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class SubCourseTimeRequest {
    @NotNull
    private Long subCourseId;
    private List<TimeInWeekRequest> timeInWeekRequests = new ArrayList<>();

    public Long getSubCourseId() {
        return subCourseId;
    }

    public void setSubCourseId(Long subCourseId) {
        this.subCourseId = subCourseId;
    }

    public List<TimeInWeekRequest> getTimeInWeekRequests() {
        return timeInWeekRequests;
    }

    public void setTimeInWeekRequests(List<TimeInWeekRequest> timeInWeekRequests) {
        this.timeInWeekRequests = timeInWeekRequests;
    }
}
