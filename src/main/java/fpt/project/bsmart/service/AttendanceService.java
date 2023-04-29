package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.response.AttendanceResponse;

import java.util.List;

public interface AttendanceService {
    List<AttendanceResponse> studentGetAllAttendance(Long classId );

    List<AttendanceResponse> teacherGetAllAttendance(Long classId);
}
