package fpt.project.bsmart.entity.request.category;

import javax.validation.constraints.NotNull;
import java.time.Instant;

public class CreateClassRequest {
    @NotNull
    private Long subCourseId;

    private Instant startDate;
    private Boolean nowIsStartDate = false;

    public Long getSubCourseId() {
        return subCourseId;
    }

    public void setSubCourseId(Long subCourseId) {
        this.subCourseId = subCourseId;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Boolean getNowIsStartDate() {
        return nowIsStartDate;
    }

    public void setNowIsStartDate(Boolean nowIsStartDate) {
        this.nowIsStartDate = nowIsStartDate;
    }
}
