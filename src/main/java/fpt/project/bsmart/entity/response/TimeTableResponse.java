package fpt.project.bsmart.entity.response;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.project.bsmart.config.json.View;
import fpt.project.bsmart.entity.dto.SlotDto;

import java.time.Instant;


public class TimeTableResponse {
    private Long id;
    private Instant date;
    private String classURL;
    private SlotDto slot;
    @JsonView(View.Teacher.class)
    private Boolean tookAttendance;
    @JsonView(View.Student.class)
    private Boolean present;

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

    public Boolean getTookAttendance() {
        return tookAttendance;
    }

    public void setTookAttendance(Boolean tookAttendance) {
        this.tookAttendance = tookAttendance;
    }

    public Boolean getPresent() {
        return present;
    }

    public void setPresent(Boolean present) {
        this.present = present;
    }
}
