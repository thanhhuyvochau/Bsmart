package fpt.project.bsmart.entity.dto;

public class TimeInWeekDTO {
    private DayOfWeekDTO dayOfWeek;
//    private SimpleClassDto clazz;
    private SlotDto slot;


    public TimeInWeekDTO(DayOfWeekDTO dayOfWeek, SlotDto slot) {
        this.dayOfWeek = dayOfWeek;
        this.slot = slot;
    }

    public DayOfWeekDTO getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeekDTO dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

//    public SimpleClassDto getClazz() {
//        return clazz;
//    }
//
//    public void setClazz(SimpleClassDto clazz) {
//        this.clazz = clazz;
//    }

    public SlotDto getSlot() {
        return slot;
    }

    public void setSlot(SlotDto slot) {
        this.slot = slot;
    }
}
