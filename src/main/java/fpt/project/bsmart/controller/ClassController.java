package fpt.project.bsmart.controller;


import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.request.MentorCreateClassRequest;
import fpt.project.bsmart.entity.request.clazz.MentorCreateClass;
import fpt.project.bsmart.entity.response.Class.MentorGetClassDetailResponse;
import fpt.project.bsmart.entity.response.ClassResponse;
import fpt.project.bsmart.entity.response.CourseClassResponse;
import fpt.project.bsmart.service.IClassService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/classes")
public class ClassController {

    private final IClassService iClassService;

    public ClassController(IClassService iClassService) {
        this.iClassService = iClassService;
    }


    @Operation(summary = "mentor tao khoá học của riêng mình ")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PostMapping
    public ResponseEntity<ApiResponse<List<String>>> mentorCreateCoursePrivate(@Valid @RequestBody MentorCreateClassRequest mentorCreateClassRequest) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.mentorCreateCoursePrivate(mentorCreateClassRequest)));
    }

    @Operation(summary = "mentor tao class cho course (step 3 ) ")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PostMapping("course/{courseId}")
    public ResponseEntity<ApiResponse<Long>> mentorCreateClassForCourse(@PathVariable Long courseId,
                                                                        @Valid @RequestBody MentorCreateClass mentorCreateClassRequest) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.mentorCreateClassForCourse(courseId, mentorCreateClassRequest)));
    }

    @Operation(summary = "Mentor lấy tất cả các class của course load lên trang khoa học")
    @GetMapping("/course/{id}")
    public ResponseEntity<ApiResponse<CourseClassResponse>> getAllClassOfCourse(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.getAllClassOfCourse(id)));
    }

    @Operation(summary = "mentor update class cho course")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> mentorUpdateClassForCourse(@PathVariable Long id,
                                                                           @Valid @RequestBody MentorCreateClass mentorCreateClassRequest) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.mentorUpdateClassForCourse(id, mentorCreateClassRequest)));
    }

    @Operation(summary = "mentor xoa class cho course")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> mentorDeleteClassForCourse(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.mentorDeleteClassForCourse(id)));
    }

    @Operation(summary = "mentor lấy các class của course ")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/course/{id}/mentor")
    public ResponseEntity<ApiResponse<ApiPage<MentorGetClassDetailResponse>>> mentorGetClassOfCourse(@PathVariable Long id, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.mentorGetClassOfCourse(id, pageable)));
    }

    @Operation(summary = "mentor lấy tất cả các class của course để phê duyệt")
    @GetMapping("/pending/course/{id}")
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<ApiResponse<CourseClassResponse>> getAllClassOfCourseForManager(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.getAllClassOfCourseForManager(id)));
    }

    @Operation(summary = "Lấy lớp chi tiết, bao gồm nội dung của lớp đang giảng dạy hoặc đã kết thúc")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','STUDENT','TEACHER')")
    public ResponseEntity<ApiResponse<ClassResponse>> getClassDetail(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.getDetailClass(id)));
    }


//    @Operation(summary = "mentor tao lớp học")
//    @PostMapping
//    public ResponseEntity<ApiResponse<Boolean>> mentorCreateClass(@Valid @RequestBody CreateClassRequest request) {
//        return ResponseEntity.ok(ApiResponse.success(iClassService.createClass(request)));
//    }
//
//    //@Operation(summary = "Lấy danh sách lớp đã tới thời điểm/ đã có feedback")
//    @GetMapping("/feedback")
//    public ResponseEntity<ApiResponse<ApiPage<SimpleClassResponse>>> getClassFeedbacks(@Nullable ClassFeedbackRequest classFeedbackRequest, Pageable pageable) {
//        return ResponseEntity.ok(ApiResponse.success(iClassService.getClassFeedbacks(classFeedbackRequest, pageable)));
//    }
//
//    @Operation(summary = "Lấy tiến trình của một lớp")
//    @GetMapping("/progression")
//    public ResponseEntity<ApiResponse<ClassProgressTimeDto>> getClassProgression(@Valid @RequestParam Long classId) {
//        return ResponseEntity.ok(ApiResponse.success(iClassService.getClassProgression(classId)));
//    }

//    @Operation(summary = "Học sinh lấy điểm danh của lớp học")
//    @GetMapping("/{classId}/student/attendances")
//    public ResponseEntity<ApiResponse<AttendanceStudentResponse>> getDetailStudentAttendance(@PathVariable long classId) {
//        return ResponseEntity.ok(ApiResponse.success(attendanceService.getAttendanceByClassForStudent(classId)));
//    }

//    @GetMapping("/{id}/time-tables")
//    public ResponseEntity<ApiResponse<ApiPage<TimeTableResponse>>> getTimeTables(Long id, Pageable pageable) {
//        return ResponseEntity.ok(ApiResponse.success(timeTableService.getTimeTableByClass(id, pageable)));
//    }

//    @PostMapping("/{id}/class-sections")
//    public ResponseEntity<ApiResponse<ClassSectionDto>> createClassSection(@RequestBody ClassSectionCreateRequest request, @PathVariable Long id) {
//        return ResponseEntity.ok(ApiResponse.success(iClassService.createClassSection(request, id)));
//    }
//
//    @PutMapping("/{id}/class-sections/{classSectionId}")
//    public ResponseEntity<ApiResponse<ClassSectionDto>> updateClassSection(@RequestBody ClassSectionUpdateRequest request, @PathVariable("id") Long id, @PathVariable("classSectionId") Long classSectionId) {
//        return ResponseEntity.ok(ApiResponse.success(iClassService.updateClassSection(id, classSectionId, request)));
//    }

//    @DeleteMapping("/{id}/class-sections/{classSectionId}")
//    public ResponseEntity<ApiResponse<Boolean>> deleteClassSection(@PathVariable("classSectionId") Long classSectionId, @PathVariable("id") Long id) {
//        return ResponseEntity.ok(ApiResponse.success(iClassService.deleteClassSection(classSectionId, id)));
//    }
//
//    @GetMapping("/{id}/class-sections/{classSectionId}")
//    public ResponseEntity<ApiResponse<ClassSectionDto>> getClassSection(@PathVariable("id") Long id, @PathVariable("classSectionId") Long classSectionId) {
//        return ResponseEntity.ok(ApiResponse.success(iClassService.getClassSection(classSectionId, id)));
//    }


//    @GetMapping("/{id}/announcements")
//    public ResponseEntity<ApiResponse<ApiPage<SimpleClassAnnouncementResponse>>> getAnnouncements(@PathVariable("id") Long id, Pageable pageable) {
//        return ResponseEntity.ok(ApiResponse.success(classAnnouncementService.getAllClassAnnouncements(id, pageable)));
//    }
//
//    @GetMapping("/{id}/announcements/{announcement-id}")
//    public ResponseEntity<ApiResponse<ClassAnnouncementDto>> getAnnouncement(@PathVariable("id") Long id, @PathVariable("announcement-id") Long classAnnouncementId) {
//        return ResponseEntity.ok(ApiResponse.success(classAnnouncementService.getClassAnnouncementById(id, classAnnouncementId)));
//    }
//
//    @PostMapping("/{id}/announcements")
//    public ResponseEntity<ApiResponse<ClassAnnouncementDto>> createAnnouncements(@PathVariable("id") Long id, @RequestBody ClassAnnouncementRequest request) {
//        return ResponseEntity.ok(ApiResponse.success(classAnnouncementService.saveClassAnnouncement(id, request)));
//    }
//
//    @PutMapping("/{id}/announcements/{announcement-id}")
//    public ResponseEntity<ApiResponse<ClassAnnouncementDto>> updateAnnouncements(@PathVariable("id") Long id, @PathVariable("announcement-id") Long classAnnouncementId, @RequestBody ClassAnnouncementRequest request) {
//        return ResponseEntity.ok(ApiResponse.success(classAnnouncementService.updateClassAnnouncement(id, classAnnouncementId, request)));
//    }
//
//    @DeleteMapping("/{id}/announcements/{announcement-id}")
//    public ResponseEntity<ApiResponse<Boolean>> deleteAnnouncements(@PathVariable("id") Long id, @PathVariable("announcement-id") Long classAnnouncementId) {
//        return ResponseEntity.ok(ApiResponse.success(classAnnouncementService.deleteClassAnnouncement(id, classAnnouncementId)));
//    }
}
