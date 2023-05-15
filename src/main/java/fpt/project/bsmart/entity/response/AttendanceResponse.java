package fpt.project.bsmart.entity.response;

import java.time.Instant;

public class AttendanceResponse {
    private Long id;
    private int slotId;
    private Instant date;
    private Long classId;
    private boolean  isAttendance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public boolean isAttendance() {
        return isAttendance;
    }

    public void setAttendance(boolean attendance) {
        isAttendance = attendance;
    }
}