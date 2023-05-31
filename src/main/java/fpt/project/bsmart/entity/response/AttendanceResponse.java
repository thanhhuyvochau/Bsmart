package fpt.project.bsmart.entity.response;

public class AttendanceResponse {
    private Long id;
    private StudentClassResponse student;
    private boolean isAttendance;
    private String note;

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