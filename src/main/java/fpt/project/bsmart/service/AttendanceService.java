package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.request.AttendanceRequest;
import fpt.project.bsmart.entity.response.AttendanceResponse;
import fpt.project.bsmart.entity.response.AttendanceResponseWrapper;
import fpt.project.bsmart.entity.response.AttendanceStudentResponse;
import org.springframework.data.domain.Pageable;

public interface AttendanceService {

    Boolean doAttendance(AttendanceRequest request);

    AttendanceResponseWrapper getAttendanceByTimeTableForTeacher(Long timeTableId, Pageable pageable);

    AttendanceStudentResponse getAttendanceByClassForStudent(Long classId);
}
