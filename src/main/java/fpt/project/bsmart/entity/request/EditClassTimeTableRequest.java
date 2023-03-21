package fpt.project.bsmart.entity.request;

import javax.validation.constraints.NotNull;
import java.time.Instant;

public class EditClassTimeTableRequest {
    @NotNull
    private Long classId;
    @NotNull
    private Long timeTableId;

    private Instant date;

    private Long slotId;


    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getTimeTableId() {
        return timeTableId;
    }

    public void setTimeTableId(Long timeTableId) {
        this.timeTableId = timeTableId;
    }
}
