package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.constant.ECourseType;
import fpt.project.bsmart.entity.dto.CourseDto;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.response.CourseResponse;
import fpt.project.bsmart.entity.response.CourseSubCourseDetailResponse;
import fpt.project.bsmart.entity.response.CourseSubCourseResponse;
import fpt.project.bsmart.entity.response.SubCourseDetailResponse;
import fpt.project.bsmart.service.ICourseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final ICourseService iCourseService;

    public CourseController(ICourseService iCourseService) {
        this.iCourseService = iCourseService;
    }

    @Operation(summary = "lấy tất cả các course theo subject id")
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<ApiResponse<List<CourseDto>>> getCoursesBySubject(@PathVariable Long subjectId) {
        List<CourseDto> courses = iCourseService.getCoursesBySubject(subjectId);
        return ResponseEntity.ok(ApiResponse.success(courses));

    }

    @Operation(summary = "Get all courses for course page")
    @GetMapping
    public ResponseEntity<ApiResponse<ApiPage<CourseResponse>>> getCourseForCoursePage(
            @Nullable CourseSearchRequest query, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.getCourseForCoursePage(query, pageable)));
    }

    @Operation(summary = "lấy tất cả các subcourse theo course đô lên trang khoa học")
    @GetMapping("{id}/sub-courses")
    public ResponseEntity<ApiResponse<ApiPage<SubCourseDetailResponse>>> getAllSubCourseOfCourse(@PathVariable Long id, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.getAllSubCourseOfCourse(id, pageable)));
    }

    @Operation(summary = "xem chi tiết khóa học trang khoa học")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseSubCourseDetailResponse>> getDetailCourseForCoursePage(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.getDetailCourseForCoursePage(id)));
    }
    // ################################## Mentor ##########################################

    @Operation(summary = "lấy tất cả khoá học có sẵn để mentor tạo  khoá hoc (course sẵn phải phải là trang thái )")
    @PreAuthorize("hasAnyRole('TEACHER' , 'MANAGER')")
    @GetMapping("/public")
    public ResponseEntity<ApiResponse<ApiPage<CourseDto>>> getCoursePublic(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.getCoursePublic(pageable)));
    }

    @Operation(summary = "mentor tao khoá học")
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> mentorCreateCourse(@Nullable Long id , @Valid @RequestBody CreateCourseRequest createCourseRequest) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorCreateCourse(id , createCourseRequest)));
    }


    @Operation(summary = "mentor xoá khoa học trang thái REQUESTING (chưa yêu cầu phê duyêt)")
    @DeleteMapping("/{subCourseId}")
    public ResponseEntity<ApiResponse<Boolean>> mentorDeleteCourse(@PathVariable Long subCourseId) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorDeleteCourse(subCourseId)));
    }


    @Operation(summary = "mentor sửa khoá học con ")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PutMapping("/{subCourseId}")
    public ResponseEntity<ApiResponse<Boolean>> mentorUpdateCourse(@PathVariable Long subCourseId, @Nullable @RequestBody UpdateSubCourseRequest updateCourseRequest) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorUpdateCourse(subCourseId, updateCourseRequest)));
    }

    @Operation(summary = "mentor xem tất cả course của mình")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/mentor")
    public ResponseEntity<ApiResponse<ApiPage<CourseSubCourseResponse>>> mentorGetCourse(ECourseStatus status, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorGetCourse(status, pageable)));
    }


    @Operation(summary = "mentor gửi yêu cầu phê duệt khoá hoc ")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PutMapping("/{subCourseId}/request-approval")
    public ResponseEntity<ApiResponse<Boolean>> mentorRequestApprovalCourse(@PathVariable Long subCourseId) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorRequestApprovalCourse(subCourseId)));
    }

    // ################################## Member ##########################################

    @Operation(summary = "Member xem khóa học đã đăng ký")
    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/member")
    public ResponseEntity<ApiResponse<ApiPage<CourseSubCourseResponse>>> memberGetCourse(ECourseStatus status, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.memberGetCourse(status, pageable)));
    }

    @Operation(summary = "Khóa học gợi ý cho member va guest")
    @GetMapping("/suggest")
    public ResponseEntity<ApiResponse<ApiPage<CourseSubCourseResponse>>> memberGetCourseSuggest(@PageableDefault(sort = "created", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.memberGetCourseSuggest(pageable)));
    }



    // ################################## Manager ##########################################

    @Operation(summary = "Manager get tất cả yêu cầu mở khóa học của mentor")
    @PreAuthorize("hasAnyRole('MANAGER')")
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<ApiPage<CourseSubCourseResponse>>> coursePendingToApprove(ECourseStatus status, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.coursePendingToApprove(status, pageable)));
    }

    @Operation(summary = "Manager phê duyêt / từ chối / yêu cầu thay đổi khoá học của mentor  ")
    @PreAuthorize("hasAnyRole('MANAGER')")
    @PutMapping("/{subCourseId}/approval")
    public ResponseEntity<ApiResponse<Boolean>> managerApprovalCourseRequest(@PathVariable Long subCourseId
            , @RequestBody ManagerApprovalCourseRequest approvalCourseRequest) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.managerApprovalCourseRequest(subCourseId, approvalCourseRequest)));
    }

    @Operation(summary = "Manager tạo khóa học chung cho các mentor (public)")
    @PreAuthorize("hasAnyRole('MANAGER')")
    @PostMapping("/public")
    public ResponseEntity<ApiResponse<Boolean>> managerCreateCourse(@RequestBody CreateCoursePublicRequest createCourseRequest) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.managerCreateCourse(createCourseRequest)));
    }



}
