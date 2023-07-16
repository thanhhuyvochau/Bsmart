package fpt.project.bsmart.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.ActivityDto;
import fpt.project.bsmart.entity.request.CourseSearchRequest;
import fpt.project.bsmart.entity.request.CreateCourseRequest;
import fpt.project.bsmart.entity.response.CourseResponse;
import fpt.project.bsmart.entity.response.course.CompletenessCourseResponse;
import fpt.project.bsmart.entity.response.course.ManagerGetCourse;
import fpt.project.bsmart.service.ICourseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
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

    @Operation(summary = "Get all courses for course page")
    @GetMapping
    public ResponseEntity<ApiResponse<ApiPage<CourseResponse>>> getCourseForCoursePage(
            @Nullable CourseSearchRequest query, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.getCourseForCoursePage(query, pageable)));
    }

    @Operation(summary = "Student get current course")
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('STUDENT')")
    public ResponseEntity<ApiResponse<ApiPage<CourseResponse>>> studentGetCurrentCourse(@Nullable CourseSearchRequest request, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.studentGetCurrentCourse(request, pageable)));
    }

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

    @Operation(summary = "Lấy tất cả khóa học của mentor")
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

    //     ################################## Manager ##########################################

    @Operation(summary = "Manager get tất cả yêu cầu mở khóa học của mentor")
    @PreAuthorize("hasAnyRole('MANAGER')")
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<ApiPage<ManagerGetCourse>>> coursePendingToApprove(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.coursePendingToApprove(pageable)));
    }

    @Operation(summary = "Giáo viên lấy hoạt động khóa học")
    @GetMapping("/{id}/activities")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ApiResponse<List<ActivityDto>>> mentorGetCourseActivities(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.getAllActivityByCourseId(id)));
    }


//    @Operation(summary = "lấy tất cả các course theo subject id")
//    @GetMapping("/subject/{subjectId}")
//    public ResponseEntity<ApiResponse<List<CourseDto>>> getCoursesBySubject(@PathVariable Long subjectId) {
//        List<CourseDto> courses = iCourseService.getCoursesBySubject(subjectId);
//
//        return ResponseEntity.ok(ApiResponse.success(courses));
//
//    }


//
//    @Operation(summary = "lấy tất cả các subcourse theo course đô lên trang khoa học")
//    @GetMapping("{id}/sub-courses")
//    public ResponseEntity<ApiResponse<ApiPage<SubCourseDetailResponse>>> getAllSubCourseOfCourse(@PathVariable Long id, Pageable pageable) {
//        return ResponseEntity.ok(ApiResponse.success(iCourseService.getAllSubCourseOfCourse(id, pageable)));
//    }
//
//    @Operation(summary = "xem chi tiết khóa học trang khoa học")
//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse<CourseSubCourseDetailResponse>> getDetailCourseForCoursePage(@PathVariable Long id) {
//        return ResponseEntity.ok(ApiResponse.success(iCourseService.getDetailCourseForCoursePage(id)));
//    }
    // ################################## Mentor ##########################################

//    @Operation(summary = "lấy tất cả khoá học có sẵn để mentor tạo  khoá hoc (course sẵn phải phải là trang thái )")
//    @PreAuthorize("hasAnyRole('TEACHER' , 'MANAGER')")
//    @GetMapping("/public")
//    public ResponseEntity<ApiResponse<ApiPage<CourseDto>>> getCoursePublic(Pageable pageable) {
//        return ResponseEntity.ok(ApiResponse.success(iCourseService.getCoursePublic(pageable)));
//    }
//
//    @Operation(summary = "mentor tao khoá học của riêng mình ")
//    @PostMapping
//    public ResponseEntity<ApiResponse<List<Long>>> mentorCreateCoursePrivate(@Valid @RequestBody CreateCourseRequest createCourseRequest) {
//        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorCreateCoursePrivate(createCourseRequest)));
//    }
//
//    @Operation(summary = "mentor tao khoá học từ khóa học public")
//    @PostMapping("public-course/{id}")
//    public ResponseEntity<ApiResponse<List<Long>>> mentorCreateCoursePublic(@PathVariable Long id, @Valid @RequestBody CreateCourseRequest createCourseRequest) {
//        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorCreateCoursePublic(id, createCourseRequest)));
//    }


//    @Operation(summary = "mentor xoá khoa học trang thái REQUESTING (chưa yêu cầu phê duyêt)")
//    @DeleteMapping("/{subCourseId}")
//    public ResponseEntity<ApiResponse<Boolean>> mentorDeleteCourse(@PathVariable Long subCourseId) {
//        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorDeleteCourse(subCourseId)));
//    }
//
//
//    @Operation(summary = "mentor sửa khoá học  ")
//    @PreAuthorize("hasAnyRole('TEACHER')")
//    @PutMapping("/{subCourseId}")
//    public ResponseEntity<ApiResponse<Boolean>> mentorUpdateCourse(@PathVariable Long subCourseId, @Nullable @RequestBody UpdateSubCourseRequest updateCourseRequest) {
//        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorUpdateCourse(subCourseId, updateCourseRequest)));
//    }

//    @Operation(summary = "mentor xem tất cả course của mình")
//    @PreAuthorize("hasAnyRole('TEACHER')")
//    @GetMapping("/mentor")
//    public ResponseEntity<ApiResponse<ApiPage<CourseSubCourseResponse>>> mentorGetAllCourse(ECourseStatus status, Pageable pageable) {
//        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorGetAllCourse(status, pageable)));
//    }
//
//    @Operation(summary = "mentor / member xem chi tiết 1 course của mình")
//    @PreAuthorize("hasAnyRole('TEACHER' , 'STUDENT')")
//    @GetMapping("/{subCourseId}/mentor")
//    public ResponseEntity<ApiResponse<CourseSubCourseResponse>> mentorGetCourse(@PathVariable Long subCourseId) {
//        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorGetCourse(subCourseId)));
//    }
//
//    @Operation(summary = "mentor gửi yêu cầu phê duệt khoá hoc ")
//    @PreAuthorize("hasAnyRole('TEACHER')")
//    @PutMapping("/{subCourseId}/request-approval")
//    public ResponseEntity<ApiResponse<Boolean>> mentorRequestApprovalCourse(@PathVariable Long subCourseId) {
//        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorRequestApprovalCourse(subCourseId)));
//    }


//    @Operation(summary = "mentor tao nội dung khoá học ")
//    @PostMapping("/{id}/content")
//    public  ResponseEntity<ApiResponse<Boolean>> mentorCreateContentCourse(@PathVariable Long id , @Valid @RequestBody List<CourseContentDto> request ) throws JsonProcessingException {
//        return ResponseEntity.ok(ApiResponse.success( iCourseService.mentorCreateContentCourse(id ,request)));
//
//    }
//
//
//    // ################################## Member ##########################################
//
//    @Operation(summary = "Member xem khóa học đã đăng ký")
//    @PreAuthorize("hasAnyRole('STUDENT')")
//    @GetMapping("/member")
//    public ResponseEntity<ApiResponse<ApiPage<CourseSubCourseResponse>>> memberGetCourse(ECourseStatus status, Pageable pageable) {
//        return ResponseEntity.ok(ApiResponse.success(iCourseService.memberGetCourse(status, pageable)));
//    }
//
//    @Operation(summary = "Khóa học gợi ý cho member va guest")
//    @GetMapping("/suggest")
//    public ResponseEntity<ApiResponse<ApiPage<CourseSubCourseResponse>>> memberGetCourseSuggest(@PageableDefault(sort = "created", direction = Sort.Direction.DESC) Pageable pageable) {
//        return ResponseEntity.ok(ApiResponse.success(iCourseService.memberGetCourseSuggest(pageable)));
//    }


//
//    @Operation(summary = "Manager phê duyêt / từ chối / yêu cầu thay đổi khoá học của mentor  ")
//    @PreAuthorize("hasAnyRole('MANAGER')")
//    @PutMapping("/{subCourseId}/approval")
//    public ResponseEntity<ApiResponse<Boolean>> managerApprovalCourseRequest(@PathVariable Long subCourseId
//            , @RequestBody ManagerApprovalCourseRequest approvalCourseRequest) {
//        return ResponseEntity.ok(ApiResponse.success(iCourseService.managerApprovalCourseRequest(subCourseId, approvalCourseRequest)));
//    }
//
//    @Operation(summary = "Manager tạo khóa học chung cho các mentor (public)")
//    @PreAuthorize("hasAnyRole('MANAGER')")
//    @PostMapping("/public")
//    public ResponseEntity<ApiResponse<Boolean>> managerCreateCourse(@RequestBody CreateCoursePublicRequest createCourseRequest) {
//        return ResponseEntity.ok(ApiResponse.success(iCourseService.managerCreateCourse(createCourseRequest)));
//    }
}
