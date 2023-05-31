package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.request.AttendanceRequest;
import fpt.project.bsmart.entity.response.AttendanceResponse;
import fpt.project.bsmart.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }


    @Operation(summary = "Lấy điểm danh theo thời khóa biểu")
    @GetMapping("/{timeTableId}")
    public ResponseEntity<ApiResponse<ApiPage<AttendanceResponse>>> getAllAttendanceByTimeTable(@PathVariable long timeTableId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getAttendanceByTimeTableForTeacher(timeTableId, pageable)));
    }

    @Operation(summary = "Điểm danh hoặc sửa cho thời khóa biểu")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<ApiResponse<Boolean>> doAttendance(@RequestBody AttendanceRequest request) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.doAttendance(request)));
    }
}