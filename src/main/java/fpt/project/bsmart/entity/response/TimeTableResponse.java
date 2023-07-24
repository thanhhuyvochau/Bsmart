package fpt.project.bsmart.entity.response;

import fpt.project.bsmart.entity.dto.SlotDto;

import java.time.Instant;


public class TimeTableResponse {
    private Long id;
    private Instant date;
    private String classURL;
    private SlotDto slot;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getClassURL() {
        return classURL;
    }

    public void setClassURL(String classURL) {
        this.classURL = classURL;
    }

    public SlotDto getSlot() {
        return slot;
    }

    public void setSlot(SlotDto slot) {
        this.slot = slot;
    }
}
