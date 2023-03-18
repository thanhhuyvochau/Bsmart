package fpt.project.bsmart.entity.request;

public class TimeInWeekRequest {
    private Long dayOfWeekId;
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
}
