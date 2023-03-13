package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.CourseDto;


import fpt.project.bsmart.entity.request.CourseSearchRequest;
import fpt.project.bsmart.entity.request.CreateCourseRequest;
import fpt.project.bsmart.entity.request.ImageRequest;
import fpt.project.bsmart.entity.response.CourseDetailResponse;
import fpt.project.bsmart.entity.response.CourseResponse;
import fpt.project.bsmart.service.ICourseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/subject/{id}")
    public ResponseEntity<ApiResponse<List<CourseDto>>> getCoursesBySubject(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.getCoursesBySubject(id)));
    }

    @Operation(summary = "mentor tao khoá học")
    @PreAuthorize("hasAuthority('TEACHER')")
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> mentorCreateCourse(@Valid @RequestBody CreateCourseRequest createCourseRequest) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorCreateCourse(createCourseRequest)));
    }

//    @Operation(summary = "mentor upload hình cho khoá học")
//    @PreAuthorize("hasAuthority('TEACHER')")
//    @PostMapping("/image")
//    public ResponseEntity<ApiResponse<Boolean>> mentorUploadImageCourse(@ModelAttribute ImageRequest ImageRequest) {
//        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorUploadImageCourse(ImageRequest)));
//    }


//    @Operation(summary = "mentor thêm ảnh đại diện cho course")
//    @PreAuthorize("hasAuthority('TEACHER')")
//    @PostMapping("{id}/image")
//    public ResponseEntity<ApiResponse<Boolean>> mentorUploadImageForCourse(@PathVariable Long id  , @ModelAttribute FileDto request) {
//        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorUploadImageForCourse(id , request)));
//    }


    @Operation(summary = "mentor xem tất cả course của mình")
    @PreAuthorize("hasAuthority('TEACHER')")
    @GetMapping("/mentor")
    public ResponseEntity<ApiResponse<ApiPage<CourseDto>>> mentorGetCourse(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorGetCourse(pageable)));
    }

    @Operation(summary = "lấy tất cả các course đổ lên trang khoa học")
    @GetMapping
    public ResponseEntity<ApiResponse<ApiPage<CourseResponse>>> getCourseForCoursePage(@Nullable CourseSearchRequest query, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.getCourseForCoursePage(query, pageable)));
    }

    @Operation(summary = "xem chi tiết khóa học trang khoa học")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseDetailResponse>> getDetailCourseForCoursePage(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.getDetailCourseForCoursePage(id)));
    }

    @Operation(summary = "mmeber đăng ký khoá học")
    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping("/{id}/member-register")
    public ResponseEntity<ApiResponse<Boolean>> memberRegisterCourse(@PathVariable Long id ) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.memberRegisterCourse(id)));
    }
}
