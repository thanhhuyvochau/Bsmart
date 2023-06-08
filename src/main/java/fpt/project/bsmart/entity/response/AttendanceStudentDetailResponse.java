package fpt.project.bsmart.entity.response;

import java.time.Instant;

public class AttendanceStudentDetailResponse {
    private Instant date;
    private Integer slotNum;
    private boolean isAttendance;
    private String note;

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Integer getSlotNum() {
        return slotNum;
    }

    public void setSlotNum(Integer slotNum) {
        this.slotNum = slotNum;
    }

    public boolean isAttendance() {
        return isAttendance;
    }

    public void setAttendance(boolean attendance) {
        isAttendance = attendance;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
