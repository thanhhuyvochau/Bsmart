package fpt.project.bsmart.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.constant.ECourseClassStatus;
import fpt.project.bsmart.entity.dto.ActivityDto;
import fpt.project.bsmart.entity.request.CourseSearchRequest;
import fpt.project.bsmart.entity.request.CreateCourseRequest;
import fpt.project.bsmart.entity.request.ManagerApprovalCourseRequest;
import fpt.project.bsmart.entity.response.CourseResponse;
import fpt.project.bsmart.entity.response.course.CompletenessCourseResponse;
import fpt.project.bsmart.entity.response.course.ManagerGetCourse;
import fpt.project.bsmart.service.ICourseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.util.List;

import static fpt.project.bsmart.util.Constants.UrlConstants.COMMON_COURSES;
import static fpt.project.bsmart.util.Constants.UrlConstants.COMMON_ROOT;

@RestController
@RequestMapping(COMMON_ROOT + COMMON_COURSES)
@Transactional(rollbackFor = {Exception.class})
public class CourseController {

    private final ICourseService iCourseService;

    public CourseController(ICourseService iCourseService) throws JsonProcessingException {
        this.iCourseService = iCourseService;
    }

    //     ################################## START CLIENT  ##########################################

    @Operation(summary = "Get all courses for course page")
    @GetMapping
    public ResponseEntity<ApiResponse<ApiPage<CourseResponse>>> getCourseForCoursePage(
            @Nullable CourseSearchRequest query, @PageableDefault(sort = "lastModified", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.getCourseForCoursePage(query, pageable)));
    }

    @Operation(summary = "Student get current course")
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('STUDENT')")
    public ResponseEntity<ApiResponse<ApiPage<CourseResponse>>> studentGetCurrentCourse(@Nullable CourseSearchRequest request, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.studentGetCurrentCourse(request, pageable)));
    }
    //     ################################## END CLIENT  ##########################################


    //     ################################## START MENTOR  ##########################################

    @Operation(summary = "mentor tao khoá học (step 1 : course) ")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> mentorCreateCoursePublic(@Valid @RequestBody CreateCourseRequest createCourseRequest) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorCreateCourse(createCourseRequest)));
    }

    @Operation(summary = "mentor sửa khoá học")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> mentorUpdateCourse(@PathVariable Long id, @Valid @RequestBody CreateCourseRequest createCourseRequest) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorUpdateCourse(id, createCourseRequest)));
    }

    @Operation(summary = "mentor xóa  khoá học")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> mentorDeleteCourse(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorDeleteCourse(id)));
    }

    @Operation(summary = " Mentor lấy tất cả khóa học của minh")

    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/mentor")
    public ResponseEntity<ApiResponse<ApiPage<CourseResponse>>> getCourseOfMentor(
            @Nullable CourseSearchRequest query, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.getCourseOfMentor(query, pageable)));
    }

    @Operation(summary = "mentor gửi yêu cầu phê duyệt khoá hoc ")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PutMapping("/{id}/request-approval")
    public ResponseEntity<ApiResponse<Boolean>> mentorRequestApprovalCourse(@PathVariable Long id, @RequestBody List<Long> classIds) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorRequestApprovalCourse(id, classIds)));
    }

    @Operation(summary = "Lấy % hông tin khoa học hoàn thiện ")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/{id}/completeness")
    public ResponseEntity<ApiResponse<CompletenessCourseResponse>> getCompletenessMentorProfile(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.getCompletenessCourse(id)));
    }

    @Operation(summary = "Giáo viên lấy hoạt động khóa học")
    @GetMapping("/{id}/activities")
    public ResponseEntity<ApiResponse<List<ActivityDto>>> mentorGetCourseActivities(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.getAllActivityByCourseId(id)));
    }

    //     ################################## END MENTOR  ##########################################


    //     ################################## START MANAGER ##########################################

    @Operation(summary = "Manager get tất cả yêu cầu mở khóa học của mentor")
    @PreAuthorize("hasAnyRole('MANAGER')")
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<ApiPage<ManagerGetCourse>>> coursePendingToApprove(ECourseClassStatus status,
                                                                                         @Nullable CourseSearchRequest query,
                                                                                         @PageableDefault(sort = "lastModified", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.coursePendingToApprove(status, pageable)));
    }


    @Operation(summary = "Manager phê duyêt / từ chối / yêu cầu thay đổi khoá học của mentor  ")
    @PreAuthorize("hasAnyRole('MANAGER')")
    @PutMapping("/{id}/approval")
    public ResponseEntity<ApiResponse<Boolean>> managerApprovalCourseRequest(@PathVariable Long id
            , @RequestBody ManagerApprovalCourseRequest approvalCourseRequest) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.managerApprovalCourseRequest(id, approvalCourseRequest)));
    }


    @Operation(summary = "Manager block khóa học + lớp đang bắt đầu khi khóa học có lỗi  ")
    @PreAuthorize("hasAnyRole('MANAGER')")
    @PutMapping("/{id}/block")
    public ResponseEntity<ApiResponse<Boolean>> managerBlockCourse(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.managerBlockCourse(id)));
    }

    //     ################################## END MANAGER ##########################################
    @Operation(summary = "Mentor chuyển trạng thái khóa học để chỉnh sửa")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PutMapping("/{id}/change-status-waiting")
    public ResponseEntity<ApiResponse<Boolean>> changeCourseStatusToWaiting(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.changeCourseToWaitingForEdit(id)));
    }
}
