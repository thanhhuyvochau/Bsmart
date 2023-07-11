package fpt.project.bsmart.entity.request.timetable;

import fpt.project.bsmart.entity.dto.DayOfWeekDTO;
import fpt.project.bsmart.entity.dto.SlotDto;

import java.time.Instant;
import java.time.LocalDate;

public class GenerateScheduleResponse {
    private LocalDate date ;

    private Integer  numberOfSlot ;


    private DayOfWeekDTO dayOfWeek ;

    private SlotDto slot ;


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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
