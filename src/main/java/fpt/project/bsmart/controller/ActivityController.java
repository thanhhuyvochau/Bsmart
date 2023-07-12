package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.constant.ECourseActivityType;
import fpt.project.bsmart.entity.dto.ActivityDetailDto;
import fpt.project.bsmart.entity.request.ActivityRequest;
import fpt.project.bsmart.entity.request.AddQuizRequest;
import fpt.project.bsmart.entity.request.AssignmentRequest;
import fpt.project.bsmart.entity.request.SubmitAssignmentRequest;
import fpt.project.bsmart.entity.request.activity.MentorCreateAnnouncementForClass;
import fpt.project.bsmart.entity.request.activity.MentorCreateResourceRequest;
import fpt.project.bsmart.entity.request.activity.MentorCreateSectionForCourse;
import fpt.project.bsmart.entity.response.Avtivity.MentorDeleteSectionForCourse;
import fpt.project.bsmart.entity.response.Avtivity.MentorGetSectionForCourse;
import fpt.project.bsmart.entity.response.Avtivity.MentorUpdateSectionForCourse;
import fpt.project.bsmart.service.IActivityService;
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
        return ResponseEntity.ok(ApiResponse.success(activityService.addActivity(request, ECourseActivityType.ASSIGNMENT)));
    }


    @PostMapping("/quiz")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ApiResponse<Boolean>> addQuizActivity(@RequestBody AddQuizRequest addQuizRequest) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(activityService.addActivity(addQuizRequest, ECourseActivityType.QUIZ)));
    }

    @PostMapping("/announcement")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ApiResponse<Boolean>> addAnnouncementActivity(@RequestBody MentorCreateAnnouncementForClass request) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(activityService.addActivity(request, ECourseActivityType.ANNOUNCEMENT)));
    }

    @PostMapping("/resource")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ApiResponse<Boolean>> addResourceActivity(@ModelAttribute MentorCreateResourceRequest request) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(activityService.addActivity(request, ECourseActivityType.RESOURCE)));
    }

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
    public ResponseEntity<ApiResponse<ActivityDetailDto>> getDetailActivity(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ApiResponse.success(activityService.getDetailActivity(id)));
    }

    @PostMapping("/assignments/{assignmentId}/submit")
    @PreAuthorize("hasAnyRole('STUDENT')")
    public ResponseEntity<ApiResponse<Boolean>> submitAssignment(@PathVariable Long assignmentId, @ModelAttribute SubmitAssignmentRequest request) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(activityService.submitAssignment(assignmentId, request)));
    }

    @PostMapping("/section")
    @PreAuthorize("hasAnyRole('TEACHER','MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> addSectionActivity(@RequestBody ActivityRequest request) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(activityService.addActivity(request, ECourseActivityType.SECTION)));
    }

    @PostMapping("/lesson")
    @PreAuthorize("hasAnyRole('TEACHER','MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> addLessonActivity(@RequestBody AssignmentRequest request) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(activityService.addActivity(request, ECourseActivityType.LESSON)));
    }
}
