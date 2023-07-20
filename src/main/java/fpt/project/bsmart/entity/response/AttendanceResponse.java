package fpt.project.bsmart.entity.response;

public class AttendanceResponse {
    private Long id;
    private StudentClassResponse student;
    private Boolean attendance;
    private String note;
    private boolean hasTookAttendance = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentClassResponse getStudent() {
        return student;
    }

    public void setStudent(StudentClassResponse student) {
        this.student = student;
    }

    public Boolean getAttendance() {
        return attendance;
    }

    public void setAttendance(Boolean attendance) {
        this.attendance = attendance;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean getHasTookAttendance() {
        return hasTookAttendance;
    }

    public void setHasTookAttendance(boolean hasTookAttendance) {
        this.hasTookAttendance = hasTookAttendance;
    }
}