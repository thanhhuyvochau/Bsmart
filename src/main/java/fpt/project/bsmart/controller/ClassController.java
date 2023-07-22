package fpt.project.bsmart.controller;


import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.common.ValidationErrorsException;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.request.MentorCreateClassRequest;
import fpt.project.bsmart.entity.request.clazz.MentorCreateClass;

import fpt.project.bsmart.entity.response.Class.ManagerGetCourseClassResponse;
import fpt.project.bsmart.entity.response.Class.MentorGetClassDetailResponse;
import fpt.project.bsmart.entity.response.ClassResponse;
import fpt.project.bsmart.entity.response.MentorGetCourseClassResponse;

import fpt.project.bsmart.entity.response.AttendanceStudentResponse;
import fpt.project.bsmart.entity.response.Class.BaseClassResponse;
import fpt.project.bsmart.entity.response.Class.ManagerGetClassDetailResponse;

import fpt.project.bsmart.service.AttendanceService;

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
    private final AttendanceService attendanceService;

    public ClassController(IClassService iClassService, AttendanceService attendanceService) {
        this.iClassService = iClassService;
        this.attendanceService = attendanceService;
    }


//    @Operation(summary = "mentor tao khoá học của riêng mình ")
//    @PreAuthorize("hasAnyRole('TEACHER')")
//    @PostMapping
//    public ResponseEntity<ApiResponse<List<String>>> mentorCreateCoursePrivate(@Valid @RequestBody MentorCreateClassRequest mentorCreateClassRequest) {
//        return ResponseEntity.ok(ApiResponse.success(iClassService.mentorCreateCoursePrivate(mentorCreateClassRequest)));
//    }

    @Operation(summary = "mentor tao class cho course (step 3 ) ")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PostMapping("course/{courseId}")
    public ResponseEntity<ApiResponse<Long>> mentorCreateClassForCourse(@PathVariable Long courseId,
                                                                        @Valid @RequestBody MentorCreateClass mentorCreateClassRequest) throws ValidationErrorsException {
        return ResponseEntity.ok(ApiResponse.success(iClassService.mentorCreateClassForCourse(courseId, mentorCreateClassRequest)));
    }

    @Operation(summary = " lấy tất cả các class của course load lên trang khoa học")
    @GetMapping("/course/{id}")
    public ResponseEntity<ApiResponse<MentorGetCourseClassResponse>> getAllClassOfCourse(@PathVariable Long id) {
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


    @Operation(summary = "mentor lấy các class  ")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/mentor")
    public ResponseEntity<ApiResponse<ApiPage<MentorGetClassDetailResponse>>> mentorGetClass(ECourseStatus  status , Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.mentorGetClass(status, pageable)));
    }

    @Operation(summary = "Quản lý lấy danh sách các lớp học đang chờ và đã bắt đầu")
    @GetMapping()
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<ApiResponse<ApiPage<BaseClassResponse>>> getAllClassesForManager(Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(iClassService.getAllClassesForManager(pageable)));
    }

    @Operation(summary = "manager lấy thông tin chi tiết lớp")
    @PreAuthorize("hasAnyRole('MANAGER')")
    @GetMapping("/{id}/detail")
    public ResponseEntity<ApiResponse<ManagerGetClassDetailResponse>> managerGetClassDetail(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(iClassService.managerGetClassDetail(id)));
    }
    @Operation(summary = "Lấy lớp chi tiết, bao gồm nội dung của lớp đang giảng dạy hoặc đã kết thúc")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','STUDENT','TEACHER')")
    public ResponseEntity<ApiResponse<ClassResponse>> getClassDetail(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.getDetailClass(id)));
    }
    //     ################################## Manager ##########################################

    @Operation(summary = "MANAGER lấy tất cả các class của course để phê duyệt")
    @GetMapping("/pending/course/{id}")
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<ApiResponse<ManagerGetCourseClassResponse>> getAllClassOfCourseForManager(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.getAllClassOfCourseForManager(id)));
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

    @Operation(summary = "Học sinh lấy điểm danh của lớp học")
    @GetMapping("/{classId}/student/attendances")
    public ResponseEntity<ApiResponse<AttendanceStudentResponse>> getDetailStudentAttendance(@PathVariable long classId) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getAttendanceByClassForStudent(classId)));
    }

//    @GetMapping("/{id}/time-tables")
//    public ResponseEntity<ApiResponse<ApiPage<TimeTableResponse>>> getTimeTables(Long id, Pageable pageable) {
//        return ResponseEntity.ok(ApiResponse.success(timeTableService.getTimeTableByClass(id, pageable)));
//    }

}
