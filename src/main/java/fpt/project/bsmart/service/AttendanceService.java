package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.request.AttendanceRequest;
import fpt.project.bsmart.entity.response.AttendanceResponse;
import org.springframework.data.domain.Pageable;

public interface AttendanceService {

    Boolean doAttendance(AttendanceRequest request);

    ApiPage<AttendanceResponse> getAttendanceByTimeTableForTeacher(Long timeTableId, Pageable pageable);
}
