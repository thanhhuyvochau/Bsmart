package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.dto.CourseDto;
import fpt.project.bsmart.entity.request.CourseSearchRequest;
import fpt.project.bsmart.entity.request.CreateCourseRequest;
import fpt.project.bsmart.entity.request.UpdateSubCourseRequest;
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

    @GetMapping("/subject/{id}")
    public ResponseEntity<ApiResponse<List<CourseDto>>> getCoursesBySubject(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.getCoursesBySubject(id)));
    }

    @Operation(summary = "lấy tất cả các course đổ lên trang khoa học")
    @GetMapping
    public ResponseEntity<ApiResponse<ApiPage<CourseResponse>>> getCourseForCoursePage(@Nullable CourseSearchRequest query, Pageable pageable) {
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

    @Operation(summary = "mentor tao khoá học")
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> mentorCreateCourse(@Valid @RequestBody CreateCourseRequest createCourseRequest) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorCreateCourse(createCourseRequest)));
    }



    @Operation(summary = "mentor xoá khoa học trang thái REQUESTING (chưa yêu cầu phê duyêt)")
    @DeleteMapping("/{subCourseId}")
    public ResponseEntity<ApiResponse<Boolean>> mentorDeleteCourse(@PathVariable Long subCourseId) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorDeleteCourse(subCourseId)));
    }




    @Operation(summary = "mentor sửa khoá học con ")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PutMapping("/subCourseId")
    public ResponseEntity<ApiResponse<Boolean>> mentorUpdateCourse(  @PathVariable Long subCourseId ,@Valid @RequestBody UpdateSubCourseRequest updateCourseRequest ) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorUpdateCourse(subCourseId , updateCourseRequest)));
    }

    @Operation(summary = "mentor xem tất cả course của mình")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/mentor")
    public ResponseEntity<ApiResponse<ApiPage<CourseSubCourseResponse>>> mentorGetCourse(ECourseStatus status, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorGetCourse(status, pageable)));
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
    public ResponseEntity<ApiResponse<ApiPage<CourseSubCourseResponse>>> memberGetCourseSuggest( @PageableDefault(sort = "created", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.memberGetCourseSuggest( pageable)));
    }

//    @Operation(summary = "mentor upload hình cho khoá học")
//    @PreAuthorize("hasAnyRole('TEACHER')")
//    @PostMapping("/image")
//    public ResponseEntity<ApiResponse<Boolean>> mentorUploadImageCourse(@ModelAttribute ImageRequest ImageRequest) {
//        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorUploadImageCourse(ImageRequest)));
//    }


//    @Operation(summary = "mentor thêm ảnh đại diện cho course")
//    @PreAuthorize("hasAnyRole('TEACHER')")
//    @PostMapping("{id}/image")
//    public ResponseEntity<ApiResponse<Boolean>> mentorUploadImageForCourse(@PathVariable Long id  , @ModelAttribute FileDto request) {
//        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorUploadImageForCourse(id , request)));
//    }


}
