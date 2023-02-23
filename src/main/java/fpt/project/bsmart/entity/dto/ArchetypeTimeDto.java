package fpt.project.bsmart.entity.dto;


public class ArchetypeTimeDto {
    private Long id;
    private SlotDto slot ;
    private DayOfWeekDto dayOfWeek ;


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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
