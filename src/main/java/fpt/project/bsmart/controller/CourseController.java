package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.request.subject.SubjectRequest;
import fpt.project.bsmart.service.ICourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/courses")
public class CourseController {


    private final ICourseService iCourseService ;

    public CourseController(ICourseService iCourseService) {
        this.iCourseService = iCourseService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> mentorCreateCourse(@Valid @RequestBody SubjectRequest subjectRequest){
        return ResponseEntity.ok(ApiResponse.success(iCourseService.mentorCreateCourse(subjectRequest)));
    }

}
