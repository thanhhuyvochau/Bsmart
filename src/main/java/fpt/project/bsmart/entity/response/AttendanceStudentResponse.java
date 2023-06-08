package fpt.project.bsmart.entity.response;

import java.util.ArrayList;
import java.util.List;

public class AttendanceStudentResponse {
    private Double absentPercentage;
    private List<AttendanceStudentDetailResponse> attendanceStudentDetails = new ArrayList<>();

    public Double getAbsentPercentage() {
        return absentPercentage;
    }

    public void setAbsentPercentage(Double absentPercentage) {
        this.absentPercentage = absentPercentage;
    }

    public List<AttendanceStudentDetailResponse> getAttendanceStudentDetails() {
        return attendanceStudentDetails;
    }

    public void setAttendanceStudentDetails(List<AttendanceStudentDetailResponse> attendanceStudentDetails) {
        this.attendanceStudentDetails = attendanceStudentDetails;
    }
}
