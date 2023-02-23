package fpt.project.bsmart.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.TimeTableDto;
import fpt.project.bsmart.entity.request.TimeTableRequest;
import fpt.project.bsmart.entity.request.TimeTableSearchRequest;
import fpt.project.bsmart.entity.response.ClassAttendanceResponse;
import fpt.project.bsmart.service.ITimeTableService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("api/timetable")
public class TimeTableController {
    private final ITimeTableService iTimeTableService;

    public TimeTableController(ITimeTableService iTimeTableService) {
        this.iTimeTableService = iTimeTableService;
    }


    @Operation(summary = "Giáo viên tao lich dạy cho lớp chờ amdin phê duyệt")
    @PostMapping({"/{classId}"})
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ApiResponse<String>> createTimeTableClass(@PathVariable Long classId, Long numberSlot, @RequestBody TimeTableRequest timeTableRequest) throws ParseException, JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iTimeTableService.createTimeTableClass(classId, numberSlot, timeTableRequest)));
    }


    @Operation(summary = "GV/Hs Lấy thời khóa biểu của class trong một ngay")
    @GetMapping({"/time-table-day/class"})
    public ResponseEntity<ApiResponse<ApiPage<TimeTableDto>>> getTimeTableInDay(@Nullable TimeTableSearchRequest timeTableSearchRequest, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iTimeTableService.getTimeTableInDay(timeTableSearchRequest, pageable)));
    }

    @Operation(summary = "giao vien/ học sinh xem tất cả thời khoá biểu/điểm danh ")
    @GetMapping("/classes")
    public ResponseEntity<ApiResponse<List<ClassAttendanceResponse>>> accountGetAllTimeTable() throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iTimeTableService.accountGetAllTimeTable()));
    }


    @Operation(summary = "manager tao lich dạy")
    @PostMapping({"/{classId}/admin"})
    @PreAuthorize("hasAnyAuthority('MANAGER','ROOT')")
    public ResponseEntity<ApiResponse<Long>> adminCreateTimeTableClass(@PathVariable Long classId, Long numberSlot, @RequestBody TimeTableRequest timeTableRequest) throws ParseException {
        return ResponseEntity.ok(ApiResponse.success(iTimeTableService.adminCreateTimeTableClass(classId, numberSlot, timeTableRequest)));
    }

    @Operation(summary = "Admin tao lich dạy")
    @PutMapping({"/{classId}/admin"})
    @PreAuthorize("hasAnyAuthority('MANAGER','ROOT')")
    public ResponseEntity<ApiResponse<Long>> adminUpdateTimeTableClass(@PathVariable Long classId, Long numberSlot, @RequestBody TimeTableRequest timeTableRequest) throws ParseException {
        return ResponseEntity.ok(ApiResponse.success(iTimeTableService.adminUpdateTimeTableClass(classId, numberSlot, timeTableRequest)));
    }



}
