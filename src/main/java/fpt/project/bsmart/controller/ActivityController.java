package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.request.activity.MentorCreateSectionForCourse;
import fpt.project.bsmart.entity.request.clazz.MentorCreateClass;
import fpt.project.bsmart.service.IActivityService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

//
//import fpt.project.bsmart.entity.common.ApiResponse;
//import fpt.project.bsmart.entity.dto.ActivityDto;
//import fpt.project.bsmart.entity.request.AssignmentRequest;
//import fpt.project.bsmart.service.IActivityService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//
@RestController
@RequestMapping("/api/activity")
public class ActivityController {
    private final IActivityService activityService;

    public ActivityController(IActivityService activityService) {
        this.activityService = activityService;
    }

    @Operation(summary = "mentor tao class cho course (step 2) ")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PostMapping("course/{id}")
    public ResponseEntity<ApiResponse<List<Long>>> mentorCreateSectionForCourse(@PathVariable Long id,
                                                                                @Valid @RequestBody List<MentorCreateSectionForCourse> sessions) {
        return ResponseEntity.ok(ApiResponse.success(activityService.mentorCreateSectionForCourse(id, sessions)));
    }
//
//    @PostMapping("/assignment")
//    @PreAuthorize("hasAnyRole('TEACHER','MANAGER','ADMIN')")
//    public ResponseEntity<ApiResponse<Boolean>> addAssignmentActivity(@ModelAttribute AssignmentRequest request) throws IOException {
//        return ResponseEntity.ok(ApiResponse.success(activityService.addActivity(request)));
//    }
//    @PutMapping("/assignment/{id}")
//    @PreAuthorize("hasAnyRole('TEACHER','MANAGER','ADMIN')")
//    public ResponseEntity<ApiResponse<Boolean>> editAssignmentActivity(@ModelAttribute AssignmentRequest request, @PathVariable("id") Long id) throws IOException {
//        return ResponseEntity.ok(ApiResponse.success(activityService.editActivity(id,request)));
//    }
//
//    @PutMapping("/visible/{id}")
//    @PreAuthorize("hasAnyRole('TEACHER','MANAGER','ADMIN')")
//    public ResponseEntity<ApiResponse<Boolean>> changeActivityVisible(@PathVariable("id") Long id) {
//        return ResponseEntity.ok(ApiResponse.success(activityService.changeActivityVisible(id)));
//    }
//
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('TEACHER','MANAGER','ADMIN')")
//    public ResponseEntity<ApiResponse<Boolean>> deleteActivity(@PathVariable("id") Long id) {
//        return ResponseEntity.ok(ApiResponse.success(activityService.deleteActivity(id)));
//    }
//
//    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('TEACHER','MANAGER','ADMIN','STUDENT')")
//    public ResponseEntity<ApiResponse<ActivityDto>> getDetailActivity(@PathVariable("id") Long id) {
//        return ResponseEntity.ok(ApiResponse.success(activityService.getDetailActivity(id)));
//    }
}
