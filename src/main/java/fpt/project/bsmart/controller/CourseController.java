package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.request.CreateCourseRequest;
import fpt.project.bsmart.service.ICourseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
