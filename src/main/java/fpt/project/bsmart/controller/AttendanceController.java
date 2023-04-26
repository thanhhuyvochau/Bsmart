package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.Attendance;
import fpt.project.bsmart.entity.BankDto;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.response.AttendanceResponse;
import fpt.project.bsmart.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }


    @Operation(summary = "lấy lịch điểm danh theo một class")
    @GetMapping("/class/{classId}")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getAllAttendance(@PathVariable long classId ) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getAllAttendance(classId)));
    }
}