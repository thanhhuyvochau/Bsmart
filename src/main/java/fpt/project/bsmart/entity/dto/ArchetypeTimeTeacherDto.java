package fpt.project.bsmart.entity.dto;


public class ArchetypeTimeTeacherDto {

    private long id;
    private SlotDto slot ;
    private DayOfWeekDto dayOfWeek ;

    private TimeTableDto timeTable ;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SlotDto getSlot() {
        return slot;
    }

    public void setSlot(SlotDto slot) {
        this.slot = slot;
    }

    public DayOfWeekDto getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeekDto dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public TimeTableDto getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(TimeTableDto timeTable) {
        this.timeTable = timeTable;
    }
}
