package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.CourseDto;
import fpt.project.bsmart.entity.dto.SlotDto;
import fpt.project.bsmart.entity.request.CreateCourseRequest;
import fpt.project.bsmart.service.ICourseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/courses")
public class CourseController {


    private final ICourseService iCourseService;

    public CourseController(ICourseService iCourseService) {
        this.iCourseService = iCourseService;
    }


    @Operation(summary = "mentor tao khoá học")
    @PreAuthorize("hasAuthority('TEACHER')")
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> mentorCreateCourse(@Valid @RequestBody CreateCourseRequest createCourseRequest) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorCreateCourse(createCourseRequest)));
    }

    @Operation(summary = "mentor xem tất cả course của mình")
    @PreAuthorize("hasAuthority('TEACHER')")
    @GetMapping("/mentor")
    public ResponseEntity<ApiResponse<ApiPage<CourseDto>>> mentorGetCourse(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorGetCourse(pageable)));
    }

}
