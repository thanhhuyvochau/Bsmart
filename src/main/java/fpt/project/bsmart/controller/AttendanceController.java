package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.Attendance;
import fpt.project.bsmart.entity.BankDto;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.response.AttendanceResponse;
import fpt.project.bsmart.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }




    @Operation(summary = "HS lấy lịch điểm danh theo một class / nhiều lớp ")
    @GetMapping("/student-View" )
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> studentGetAllAttendance(@RequestParam(name = "classId", defaultValue = "false") Long classId ) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.studentGetAllAttendance(classId)));
    }

    @Operation(summary = "GV lấy lịch điểm danh theo một class / nhiều lớp ")
    @GetMapping("/teacher-View" )
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> teacherGetAllAttendance(@RequestParam(name = "classId", defaultValue = "false") Long classId ) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.teacherGetAllAttendance(classId)));
    }
}