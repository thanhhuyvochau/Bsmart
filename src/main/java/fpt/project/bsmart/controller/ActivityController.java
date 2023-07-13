package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.constant.ECourseActivityType;
import fpt.project.bsmart.entity.dto.ActivityDetailDto;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.request.activity.MentorCreateAnnouncementForClass;
import fpt.project.bsmart.entity.request.activity.MentorCreateResourceRequest;
import fpt.project.bsmart.service.IActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {
    private final IActivityService activityService;

    public ActivityController(IActivityService activityService) {
        this.activityService = activityService;
    }

    /**
     * ----------- Create Activity ------------
     */
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

    @PostMapping("/section")
    @PreAuthorize("hasAnyRole('TEACHER','MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> addSectionActivity(@RequestBody ActivityRequest request) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(activityService.addActivity(request, ECourseActivityType.SECTION)));
    }

    @PostMapping("/lesson")
    @PreAuthorize("hasAnyRole('TEACHER','MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> addLessonActivity(@Valid @RequestBody LessonRequest request) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(activityService.addActivity(request, ECourseActivityType.LESSON)));
    }

    /**
     * ----------- Edit Activity ------------
     */
    @PutMapping("/{id}/assignment")
    @PreAuthorize("hasAnyRole('TEACHER','MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> editAssignmentActivity(@PathVariable long id, @ModelAttribute AssignmentRequest request) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(activityService.editActivity(id, request, ECourseActivityType.ASSIGNMENT)));
    }


    @PutMapping("/{id}/quiz")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ApiResponse<Boolean>> editQuizActivity(@PathVariable long id, @RequestBody AddQuizRequest addQuizRequest) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(activityService.editActivity(id, addQuizRequest, ECourseActivityType.QUIZ)));
    }

    @PutMapping("/{id}/announcement")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ApiResponse<Boolean>> editAnnouncementActivity(@PathVariable long id, @RequestBody MentorCreateAnnouncementForClass request) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(activityService.editActivity(id, request, ECourseActivityType.ANNOUNCEMENT)));
    }

    @PutMapping("/{id}/resource")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ApiResponse<Boolean>> editResourceActivity(@PathVariable long id, @ModelAttribute MentorCreateResourceRequest request) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(activityService.editActivity(id, request, ECourseActivityType.RESOURCE)));
    }

    @PutMapping("/{id}/section")
    @PreAuthorize("hasAnyRole('TEACHER','MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> editSectionActivity(@PathVariable long id, @RequestBody ActivityRequest request) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(activityService.editActivity(id, request, ECourseActivityType.SECTION)));
    }

    @PutMapping("/{id}/lesson")
    @PreAuthorize("hasAnyRole('TEACHER','MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> editLessonActivity(@PathVariable long id, @Valid @RequestBody LessonRequest request) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(activityService.editActivity(id, request, ECourseActivityType.LESSON)));
    }

    /**
     * Other Action With Activity
     */
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
}
