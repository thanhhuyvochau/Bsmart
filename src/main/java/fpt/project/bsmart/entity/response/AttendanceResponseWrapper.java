package fpt.project.bsmart.entity.response;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.SlotDto;

import java.time.Instant;

public class AttendanceResponseWrapper {
    private TimeTableResponse timeTableResponse;
    private ApiPage<AttendanceResponse> attendanceResponses;

    public AttendanceResponseWrapper(TimeTableResponse timeTableResponse, ApiPage<AttendanceResponse> attendanceResponses) {
        this.timeTableResponse = timeTableResponse;
        this.attendanceResponses = attendanceResponses;
    }

    public TimeTableResponse getTimeTableResponse() {
        return timeTableResponse;
    }

    public void setTimeTableResponse(TimeTableResponse timeTableResponse) {
        this.timeTableResponse = timeTableResponse;
    }

    public ApiPage<AttendanceResponse> getAttendanceResponses() {
        return attendanceResponses;
    }

    public void setAttendanceResponses(ApiPage<AttendanceResponse> attendanceResponses) {
        this.attendanceResponses = attendanceResponses;
    }
}