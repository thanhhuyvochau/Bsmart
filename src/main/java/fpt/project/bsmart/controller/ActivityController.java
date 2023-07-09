package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.ActivityDto;
import fpt.project.bsmart.entity.request.AssignmentRequest;
import fpt.project.bsmart.entity.response.Avtivity.MentorDeleteSectionForCourse;
import fpt.project.bsmart.entity.response.Avtivity.MentorGetSectionForCourse;
import fpt.project.bsmart.entity.response.Avtivity.MentorUpdateSectionForCourse;
import fpt.project.bsmart.service.IActivityService;
import fpt.project.bsmart.entity.request.activity.MentorCreateSectionForCourse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {
    private final IActivityService activityService;

    public ActivityController(IActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping("/assignment")
    @PreAuthorize("hasAnyRole('TEACHER','MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> addAssignmentActivity(@ModelAttribute AssignmentRequest request) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(activityService.addActivity(request)));
    }
    @Operation(summary = "mentor tao nội dung cho course (step 2) ")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PostMapping("course/{id}")
    public ResponseEntity<ApiResponse<List<Long>>> mentorCreateSectionForCourse(@PathVariable Long id,
                                                                                @Valid @RequestBody MentorCreateSectionForCourse sessions) {
        return ResponseEntity.ok(ApiResponse.success(activityService.mentorCreateSectionForCourse(id, sessions)));
    }

    @Operation(summary = "mentor sửa nội dung cho course  ")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PutMapping("course/{id}")
    public ResponseEntity<ApiResponse<Boolean>> mentorUpdateSectionForCourse(@PathVariable Long id,
                                                                                @Valid @RequestBody MentorUpdateSectionForCourse updateRequest) {
        return ResponseEntity.ok(ApiResponse.success(activityService.mentorUpdateSectionForCourse(id, updateRequest)));
    }

    @Operation(summary = "mentor xóa nội dung cho course  ")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @DeleteMapping("course/{id}")
    public ResponseEntity<ApiResponse<Boolean>> mentorDeleteSectionForCourse(@PathVariable Long id,
                                                                             @Valid @RequestBody List<MentorDeleteSectionForCourse> deleteRequest) {
        return ResponseEntity.ok(ApiResponse.success(activityService.mentorDeleteSectionForCourse(id, deleteRequest)));
    }

    @Operation(summary = "mentor lấy  nội dung của course ")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("course/{id}")
    public ResponseEntity<ApiResponse<List<MentorGetSectionForCourse>>> mentorGetSectionOfCourse(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(activityService.mentorGetSectionOfCourse(id)));
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

    @PutMapping("/visible/{id}")
    @PreAuthorize("hasAnyRole('TEACHER','MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> changeActivityVisible(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ApiResponse.success(activityService.changeActivityVisible(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER','MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> deleteActivity(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ApiResponse.success(activityService.deleteActivity(id)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER','MANAGER','ADMIN','STUDENT')")
    public ResponseEntity<ApiResponse<ActivityDto>> getDetailActivity(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ApiResponse.success(activityService.getDetailActivity(id)));
    }
}
