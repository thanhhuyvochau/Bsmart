package fpt.project.bsmart.entity.request;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class TimeInWeekRequest {
    @NotNull
    private Long dayOfWeekId;
    @NotNull
    private Long slotId;

    public Long getDayOfWeekId() {
        return dayOfWeekId;
    }

    public void setDayOfWeekId(Long dayOfWeekId) {
        this.dayOfWeekId = dayOfWeekId;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeInWeekRequest that = (TimeInWeekRequest) o;
        return Objects.equals(dayOfWeekId, that.dayOfWeekId) && Objects.equals(slotId, that.slotId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayOfWeekId, slotId);
    }
}
