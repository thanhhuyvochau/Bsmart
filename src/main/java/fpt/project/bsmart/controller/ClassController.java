package fpt.project.bsmart.controller;


import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.ClassProgressTimeDto;
import fpt.project.bsmart.entity.request.ClassFeedbackRequest;
import fpt.project.bsmart.entity.request.category.CreateClassRequest;
import fpt.project.bsmart.entity.response.AttendanceStudentResponse;
import fpt.project.bsmart.entity.response.ClassResponse;
import fpt.project.bsmart.service.AttendanceService;
import fpt.project.bsmart.service.IClassService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/classes")
public class ClassController {

    private final IClassService iClassService;
    private final AttendanceService attendanceService;

    public ClassController(IClassService iClassService, AttendanceService attendanceService) {
        this.iClassService = iClassService;
        this.attendanceService = attendanceService;
    }

    @Operation(summary = "mentor tao lớp học")
    @PostMapping
    public ResponseEntity<ApiResponse<Boolean>> mentorCreateClass(@Valid @RequestBody CreateClassRequest request) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.createClass(request)));
    }

    //@Operation(summary = "Lấy danh sách lớp đã tới thời điểm/ đã có feedback")
    @GetMapping("/feedback")
    public ResponseEntity<ApiResponse<ApiPage<ClassResponse>>> getClassFeedbacks(@Nullable ClassFeedbackRequest classFeedbackRequest, Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(iClassService.getClassFeedbacks(classFeedbackRequest, pageable)));
    }

    @Operation(summary = "Lấy tiến trình của một lớp")
    @GetMapping("/progression")
    public ResponseEntity<ApiResponse<ClassProgressTimeDto>> getClassProgression(@Valid @RequestParam Long classId) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.getClassProgression(classId)));
    }
    @Operation(summary = "Học sinh lấy điểm danh của lớp học")
    @GetMapping("/{classId}/student/attendances")
    public ResponseEntity<ApiResponse<AttendanceStudentResponse>> getDetailStudentAttendance(@PathVariable long classId) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getAttendanceByClassForStudent(classId)));
    }

}
