package fpt.project.bsmart.controller;


import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.ClassAnnouncementDto;
import fpt.project.bsmart.entity.dto.ClassProgressTimeDto;
import fpt.project.bsmart.entity.dto.ClassSectionDto;
import fpt.project.bsmart.entity.dto.SimpleClassAnnouncementResponse;
import fpt.project.bsmart.entity.request.ClassAnnouncementRequest;
import fpt.project.bsmart.entity.request.ClassFeedbackRequest;
import fpt.project.bsmart.entity.request.ClassSectionCreateRequest;
import fpt.project.bsmart.entity.request.ClassSectionUpdateRequest;
import fpt.project.bsmart.entity.request.category.CreateClassRequest;
import fpt.project.bsmart.entity.response.AttendanceStudentResponse;
import fpt.project.bsmart.entity.response.ClassResponse;
import fpt.project.bsmart.entity.response.SimpleClassResponse;
import fpt.project.bsmart.entity.response.TimeTableResponse;
import fpt.project.bsmart.service.AttendanceService;
import fpt.project.bsmart.service.ClassAnnouncementService;
import fpt.project.bsmart.service.IClassService;
import fpt.project.bsmart.service.ITimeTableService;
import fpt.project.bsmart.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/classes")
public class ClassController {

    private final IClassService iClassService;
    private final AttendanceService attendanceService;
    private final ITimeTableService timeTableService;
    private final ClassAnnouncementService classAnnouncementService;

    public ClassController(IClassService iClassService, AttendanceService attendanceService, ITimeTableService timeTableService, ClassAnnouncementService classAnnouncementService) {
        this.iClassService = iClassService;
        this.attendanceService = attendanceService;
        this.timeTableService = timeTableService;
        this.classAnnouncementService = classAnnouncementService;
    }

    @Operation(summary = "mentor tao lớp học")
    @PostMapping
    public ResponseEntity<ApiResponse<Boolean>> mentorCreateClass(@Valid @RequestBody CreateClassRequest request) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.createClass(request)));
    }

    //@Operation(summary = "Lấy danh sách lớp đã tới thời điểm/ đã có feedback")
    @GetMapping("/feedback")
    public ResponseEntity<ApiResponse<ApiPage<SimpleClassResponse>>> getClassFeedbacks(@Nullable ClassFeedbackRequest classFeedbackRequest, Pageable pageable) {
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

    @Operation(summary = "Lấy lớp chi tiết")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClassResponse>> getClassDetail(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.getDetailClass(id)));
    }

    @GetMapping("/{id}/time-tables")
    public ResponseEntity<ApiResponse<ApiPage<TimeTableResponse>>> getTimeTables(Long id, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(timeTableService.getTimeTableByClass(id, pageable)));
    }

    @PostMapping("/{id}/class-sections")
    public ResponseEntity<ApiResponse<ClassSectionDto>> createClassSection(@RequestBody ClassSectionCreateRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.createClassSection(request, id)));
    }

    @PutMapping("/{id}/class-sections/{classSectionId}")
    public ResponseEntity<ApiResponse<ClassSectionDto>> updateClassSection(@RequestBody ClassSectionUpdateRequest request, @PathVariable("id") Long id, @PathVariable("classSectionId") Long classSectionId) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.updateClassSection(id, classSectionId, request)));
    }

    @DeleteMapping("/{id}/class-sections/{classSectionId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteClassSection(@PathVariable("classSectionId") Long classSectionId, @PathVariable("id") Long id) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.deleteClassSection(classSectionId, id)));
    }

    @GetMapping("/{id}/class-sections/{classSectionId}")
    public ResponseEntity<ApiResponse<ClassSectionDto>> getClassSection(@PathVariable("id") Long id, @PathVariable("classSectionId") Long classSectionId) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.getClassSection(classSectionId, id)));
    }


    @GetMapping("/{id}/announcements")
    public ResponseEntity<ApiResponse<ApiPage<SimpleClassAnnouncementResponse>>> getAnnouncements(@PathVariable("id") Long id, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(classAnnouncementService.getAllClassAnnouncements(id, pageable)));
    }

    @GetMapping("/{id}/announcements/{announcement-id}")
    public ResponseEntity<ApiResponse<ClassAnnouncementDto>> getAnnouncement(@PathVariable("id") Long id, @PathVariable("announcement-id") Long classAnnouncementId) {
        return ResponseEntity.ok(ApiResponse.success(classAnnouncementService.getClassAnnouncementById(id, classAnnouncementId)));
    }

    @PostMapping("/{id}/announcements")
    public ResponseEntity<ApiResponse<ClassAnnouncementDto>> createAnnouncements(@PathVariable("id") Long id, @RequestBody ClassAnnouncementRequest request) {
        return ResponseEntity.ok(ApiResponse.success(classAnnouncementService.saveClassAnnouncement(id, request)));
    }

    @PutMapping("/{id}/announcements/{announcement-id}")
    public ResponseEntity<ApiResponse<ClassAnnouncementDto>> updateAnnouncements(@PathVariable("id") Long id, @PathVariable("announcement-id") Long classAnnouncementId, @RequestBody ClassAnnouncementRequest request) {
        return ResponseEntity.ok(ApiResponse.success(classAnnouncementService.updateClassAnnouncement(id, classAnnouncementId, request)));
    }

    @DeleteMapping("/{id}/announcements/{announcement-id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteAnnouncements(@PathVariable("id") Long id, @PathVariable("announcement-id") Long classAnnouncementId) {
        return ResponseEntity.ok(ApiResponse.success(classAnnouncementService.deleteClassAnnouncement(id, classAnnouncementId)));
    }
}
