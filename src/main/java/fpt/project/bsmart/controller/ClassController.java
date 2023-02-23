package fpt.project.bsmart.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.common.EClassStatus;
import fpt.project.bsmart.entity.dto.*;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.response.*;
import fpt.project.bsmart.service.IClassService;
import fpt.project.bsmart.service.IForumService;
import fpt.project.bsmart.service.IMoodleService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/class")
public class ClassController {

    private final IClassService iClassService;
    private final IForumService forumService;
    private final IMoodleService moodleService;

    public ClassController(IClassService iClassService, IForumService forumService, IMoodleService moodleService) {
        this.iClassService = iClassService;
        this.forumService = forumService;
        this.moodleService = moodleService;
    }


    @Operation(summary = "Giáo viên yêu cầu tạo class (class) chờ admin phê duyệt ")
    @PostMapping({"/teacher-request-create-class"})
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ApiResponse<Long>> teacherRequestCreateClass(@Nullable @RequestBody TeacherCreateClassRequest createClassRequest) throws JsonProcessingException, ParseException {
        return ResponseEntity.ok(ApiResponse.success(iClassService.teacherRequestCreateClass(createClassRequest)));

    }

    @Operation(summary = "giao vien update lớp khi còn ở trạng thái requesting va editting")
    @PutMapping({"/{id}/for-requesting"})
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public ResponseEntity<ApiResponse<Long>> updateClassForRequesting(@PathVariable Long id, @Nullable @RequestBody CreateRequestingClassRequest createRequestingClassRequest) throws JsonProcessingException, ParseException {
        return ResponseEntity.ok(ApiResponse.success(iClassService.updateClassForRequesting(id, createRequestingClassRequest)));
    }


    @Operation(summary = "Giáo viên đã hoàn thành tạo lớp và gửi yêu cầu phê duyệt ")
    @PutMapping({"{id}/teacher-submit-request-create-class"})
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ApiResponse<Long>> teacherSubmitRequestCreateClass(@PathVariable Long id) throws JsonProcessingException, ParseException {
        return ResponseEntity.ok(ApiResponse.success(iClassService.teacherSubmitRequestCreateClass(id)));

    }


    @Operation(summary = "admin seach class tất cả type")
    @GetMapping({"/class-request"})
    @PreAuthorize("hasAnyAuthority('MANAGER','ROOT')")
    public ResponseEntity<ApiResponse<ApiPage<ClassDto>>> getClassRequesting(@Nullable ClassSearchRequest query, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.getClassRequesting(query, pageable)));
    }

    @Operation(summary = "lấy tất cả class có phân trang")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('MANAGER','ROOT')")
    public ResponseEntity<ApiResponse<ApiPage<ClassDto>>> getAllClass(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.getAllClass(pageable)));
    }

    @Operation(summary = "lấy tất cả class có phân trang cho trang chủ ")
    @GetMapping("/for-user")
    public ResponseEntity<ApiResponse<ApiPage<ClassDto>>> getAllClassForUser(Pageable pageable, GuestSearchClassRequest guestSearchClassRequest) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.getAllClassForUser(pageable, guestSearchClassRequest)));
    }

    @Operation(summary = "  ")
    @PostMapping({"/{id}/approve-class"})
    @PreAuthorize("hasAnyAuthority('MANAGER','ROOT')")
    public ResponseEntity<ApiResponse<ClassDto>> adminApproveRequestCreateClass(@PathVariable Long id) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iClassService.adminApproveRequestCreateClass(id)));
    }

    @Operation(summary = "Admin yêu cầu sửa thông tin lớp request  của teacher ")
    @PutMapping({"/{id}/request-change-class"})
    @PreAuthorize("hasAnyAuthority('MANAGER','ROOT')")
    public ResponseEntity<ApiResponse<ChangeInfoClassRequest>> adminRequestChangeInfoClass(@PathVariable Long id, @RequestBody String content) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iClassService.adminRequestChangeInfoClass(id, content)));
    }

//    @Operation(summary = "Tạo class qua moodle")
//    @PostMapping({"/create-class-to-moodle"})
//    @PreAuthorize("hasAnyAuthority('MANAGER','ROOT')")
//    public ResponseEntity<ApiResponse<Boolean>> synchronizedClassToMoodle(@RequestBody CreateCourseRequest createCourseRequest) throws JsonProcessingException {
//        return ResponseEntity.ok(ApiResponse.success(iClassService.synchronizedClassToMoodle(createCourseRequest)));
//    }


    @Operation(summary = "Hoc sinh đăng ký vào class")
    @PostMapping({"/{studentId}/student-enroll-class"})
    public ResponseEntity<ApiResponse<Boolean>> studentEnrollClass(@PathVariable Long studentId, Long classId) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iClassService.studentEnrollClass(studentId, classId)));
    }


    @Operation(summary = "lấy danh sách tất cả hoc sinh dang  chờ duyệt để vào class")
    @GetMapping({"/{classId}/student-waiting-approve"})
    public ResponseEntity<ApiResponse<List<ClassStudentDto>>> getStudentWaitingIntoClass(@PathVariable Long classId) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.getStudentWaitingIntoClass(classId)));
    }

    @Operation(summary = "Tìm Kiếm class")
    @GetMapping("/search-class")
    @PreAuthorize("hasAnyAuthority('STUDENT','TEACHER')")
    public ResponseEntity<ApiResponse<ApiPage<ClassDto>>> searchClass(@Nullable ClassSearchRequest query, Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.searchClass(query, pageable)));
    }


    @Operation(summary = "chi tiết class")
    @GetMapping("/{id}/class-detail")
    public ResponseEntity<ApiResponse<ClassDto>> classDetail(@PathVariable Long id) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iClassService.classDetail(id)));
    }

//
//    @Operation(summary = "lấy tất cả hoc sinh request vao lớp ")
//    @GetMapping({"{classId}/students-approve-class"})
//    public ResponseEntity<ApiResponse<List<ClassDto>>> studentWaitingApproveIntoClass(@PathVariable Long classId) {
//        return ResponseEntity.ok(ApiResponse.success(iClassService.studentWaitingApproveIntoClass(classId)));
//    }

    @Operation(summary = "học sinh / giáo viên xem lớp bằng thời gian (was study, is studying, will study) ")
    @GetMapping("/{accountId}/class-of-account")
    public ResponseEntity<ApiResponse<ApiPage<ClassDto>>> accountFilterClass(@Nullable ClassSearchRequest query, Pageable pageable) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iClassService.accountFilterClass(query, pageable)));
    }


    @Operation(summary = "Gợi ý lớp học - hs.phụ huynh đăng ký tìm gia sư ")
    @GetMapping("{infoFindTutorId}/class-suggestion")
    public ResponseEntity<ApiResponse<ApiPage<ClassDto>>> classSuggestion(@PathVariable long infoFindTutorId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.classSuggestion(infoFindTutorId, pageable)));
    }

    @Operation(summary = "Admin tạo class để tuyển giáo viên ")
    @PostMapping({"/for-recruiting"})
    @PreAuthorize("hasAnyAuthority('MANAGER','ROOT')")
    public ResponseEntity<ApiResponse<Long>> createClassForRecruiting(@Nullable @RequestBody CreateRecruitingClassRequest createRecruitingClassRequest) throws JsonProcessingException, ParseException {
        return ResponseEntity.ok(ApiResponse.success(iClassService.createClassForRecruiting(createRecruitingClassRequest)));
    }

    @Operation(summary = "Admin update lớp khi còn ở trạng thái recruiting ")
    @PutMapping({"/{id}/for-recruiting"})
    @PreAuthorize("hasAnyAuthority('MANAGER','ROOT')")
    public ResponseEntity<ApiResponse<Long>> updateClassForRecruiting(@PathVariable Long id, @Nullable @RequestBody CreateRecruitingClassRequest createRecruitingClassRequest) throws JsonProcessingException, ParseException {
        return ResponseEntity.ok(ApiResponse.success(iClassService.updateClassForRecruiting(id, createRecruitingClassRequest)));
    }

    @Operation(summary = "Giáo viên ứng tuyển dạy")

    @PostMapping({"/apply"})
    public ResponseEntity<ApiResponse<Boolean>> applyToRecruitingClass(@RequestBody Long classId) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iClassService.applyToRecruitingClass(classId)));
    }

    @Operation(summary = "Chọn giáo viên cho lớp")
    @PutMapping({"/choose-candicate"})
    public ResponseEntity<ApiResponse<AccountResponse>> chooseCandicateTeacher(@RequestBody ClassCandicateRequest request) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iClassService.chooseCandicateForClass(request)));
    }

    @Operation(summary = "Lấy các giáo viên ứng viên của lớp")
    @GetMapping({"/{classId}/candicates"})
    public ResponseEntity<ApiResponse<ApiPage<CandicateResponse>>> getClassCandicates(@PathVariable Long classId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.getClassCandicate(classId, pageable)));
    }

    @Operation(summary = "Lấy các lớp đang tuyển gia sư")
    @GetMapping({"/status/recruiting"})
    public ResponseEntity<ApiResponse<ApiPage<ClassDto>>> getRecruitingClass(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.getRecruitingClasses(pageable)));
    }

    @Operation(summary = "(search) lấy tất cả class của giáo viên / học sinh")
    @GetMapping("/search-class/account")
    public ResponseEntity<ApiResponse<ApiPage<ClassDto>>> getClassByAccount(ClassSearchRequest request, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.getClassByAccount(request, pageable)));
    }

    @Operation(summary = "admin seach lơp")
    @GetMapping("/search-class/admin")
    public ResponseEntity<ApiResponse<ApiPage<ClassDto>>> adminGetClass(EClassStatus status, Pageable pageable) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iClassService.adminGetClass(status, pageable)));
    }

    @Operation(summary = "hoc sinh/ giao vien xem chi tiết class")
    @GetMapping("/{id}/account/class-detail")
    public ResponseEntity<ApiResponse<ClassDetailDto>> accountGetClassDetail(@PathVariable Long id) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iClassService.accountGetClassDetail(id)));
    }

    @Operation(summary = "hoc sinh/ giao vien xem chi tiết resource của lớp")
    @GetMapping("/{id}/resource")
    public ResponseEntity<ApiResponse<ClassResourcesResponse>> accountGetResourceOfClass(@PathVariable Long id) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iClassService.accountGetResourceOfClass(id)));
    }

    @Operation(summary = "giao vien xem  hoc sinh của lớp")
    @GetMapping("/{id}/students")
    @PreAuthorize("hasAnyAuthority('TEACHER','MANAGER')")
    public ResponseEntity<ApiResponse<ApiPage<AccountResponse>>> accountGetStudentOfClass(@PathVariable Long id, Pageable pageable) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iClassService.accountGetStudentOfClass(id, pageable)));
    }

//    @Operation(summary = "giao vien/ học sinh xem chi tiết  thời khoá biểu của lớp")
//    @GetMapping("/{id}/timetable")
//    public ResponseEntity<ApiResponse<List<ClassTimeTableResponse>>> accountGetTimeTableOfClass(@PathVariable Long id) throws JsonProcessingException {
//        return ResponseEntity.ok(ApiResponse.success(iClassService.accountGetTimeTableOfClass(id)));
//    }

    @Operation(summary = "giao vien/ học sinh xem thời khoá biểu/điểm danh của môt lớp")
    @GetMapping("/{id}/attendance")
    public ResponseEntity<ApiResponse<ClassAttendanceResponse>> accountGetAttendanceOfClass(@PathVariable Long id) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iClassService.accountGetAttendanceOfClass(id)));

    }


    @Operation(summary = "học sinh xem thông tin giáo viên của lớp")
    @GetMapping("/{id}/teacher")
    public ResponseEntity<ApiResponse<ClassTeacherResponse>> studentGetTeacherInfoOfClass(@PathVariable Long id) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iClassService.studentGetTeacherInfoOfClass(id)));
    }

    @Operation(summary = "Lấy chi tiết diễn đàn của một lớp học")
    @GetMapping("/forum")
    public ResponseEntity<ApiResponse<ForumDto>> getForumOfClass(@RequestParam Long classId) {
        return ResponseEntity.ok(ApiResponse.success(forumService.getForumByClass(classId)));
    }

    @Operation(summary = "(search) lấy tất cả class của giáo viên / học sinh không phân trang")
    @GetMapping("/search-class/account/list")
    public ResponseEntity<ApiResponse<List<ClassDto>>> getClassByAccount(EClassStatus status) {
        return ResponseEntity.ok(ApiResponse.success(iClassService.getClassByAccountAsList(status)));
    }

    @PutMapping("/{id}/confirm-appreciation")
    @Operation(summary = "Giáo viên xác nhận lớp đã sẵn sàng để admin duyệt!")
    public ResponseEntity<ApiResponse<ClassDto>> confirmAppreciation(@PathVariable Long id) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iClassService.confirmAppreciation(id)));
    }

    @Operation(summary = "Admin từ chối yêu cầu mờ lớp của giáo viên ")
    @PostMapping({"/{id}/reject"})
    @PreAuthorize("hasAnyAuthority('MANAGER','ROOT')")
    public ResponseEntity<ApiResponse<ClassDto>> adminRejectRequestCreateClass(@PathVariable Long id) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iClassService.adminRejectRequestCreateClass(id)));
    }

    @Operation(summary = "Quản lý enrol một học sinh vào lớp bất kì ! ")
    @PostMapping({"/{id}/enrol"})
    @PreAuthorize("hasAnyAuthority('MANAGER','ROOT')")
    public ResponseEntity<ApiResponse<Boolean>> adminEnrolStudentIntoClass(@PathVariable Long id, @RequestParam Long studentId) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iClassService.adminEnrolStudentIntoClass(studentId, id)));
    }

    @Operation(summary = "Xác nhận nhận lớp dạy của giáo viên")
    @PostMapping({"/confirm"})
    public ResponseEntity<ApiResponse<Boolean>> confirmTeaching(@RequestParam String code) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iClassService.confirmTeaching(code)));
    }

    @Operation(summary = "Hủy lớp đang PENDING")
    @DeleteMapping({"/{classId}/pending-class"})
    public ResponseEntity<ApiResponse<ClassDto>> deletePendingClass(@PathVariable Long classId) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iClassService.cancelPendingClass(classId)));
    }
}
