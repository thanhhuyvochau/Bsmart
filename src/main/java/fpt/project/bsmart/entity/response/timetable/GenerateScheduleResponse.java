package fpt.project.bsmart.entity.response.timetable;

import fpt.project.bsmart.entity.dto.DayOfWeekDTO;
import fpt.project.bsmart.entity.dto.SlotDto;

import java.time.Instant;


public class GenerateScheduleResponse {
    private Instant date;
    private Integer numberOfSlot;
    private DayOfWeekDTO dayOfWeek;
    private SlotDto slot;


    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Integer getNumberOfSlot() {
        return numberOfSlot;
    }

    public void setNumberOfSlot(Integer numberOfSlot) {
        this.numberOfSlot = numberOfSlot;
    }

    public DayOfWeekDTO getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeekDTO dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public SlotDto getSlot() {
        return slot;
    }

    public void setSlot(SlotDto slot) {
        this.slot = slot;
    }
}
