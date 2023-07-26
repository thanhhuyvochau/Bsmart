package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.constant.ECourseActivityType;
import fpt.project.bsmart.entity.dto.ActivityDetailDto;
import fpt.project.bsmart.entity.dto.AssignmentSubmitionDto;
import fpt.project.bsmart.entity.dto.QuizDto;
import fpt.project.bsmart.entity.dto.QuizSubmittionDto;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.request.activity.MentorCreateAnnouncementForClass;
import fpt.project.bsmart.entity.request.activity.MentorCreateResourceRequest;
import fpt.project.bsmart.entity.response.QuizSubmissionResultResponse;
import fpt.project.bsmart.service.IActivityService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
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

    @Operation(summary = "hoc sinh lam quiz")
    @PostMapping("/{id}/quiz/attempt")
    @PreAuthorize("hasAnyRole('STUDENT')")
    public ResponseEntity<ApiResponse<QuizDto>> studentAttemptQuiz(@PathVariable("id") Long id, @RequestBody StudentAttemptQuizRequest request) {
        return ResponseEntity.ok(ApiResponse.success(activityService.studentAttemptQuiz(id, request)));
    }

    @Operation(summary = "học sinh nộp quiz")
    @PreAuthorize("hasAnyRole('STUDENT')")
    @PostMapping("/{id}/quiz/submit")
    public ResponseEntity<ApiResponse<Boolean>> studentSubmitQuiz(@PathVariable("id") Long id, @RequestBody SubmitQuizRequest request) {
        return ResponseEntity.ok(ApiResponse.success(activityService.studentSubmitQuiz(id, request)));
    }

    @Operation(summary = "xem kết quả quiz")
    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/quiz/result/{id}")
    public ResponseEntity<ApiResponse<QuizSubmissionResultResponse>> studentViewQuizResult(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(activityService.studentViewQuizResult(id)));
    }

    @Operation(summary = "Giáo viên xem kết quả quiz")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/quiz/{id}/result")
    public ResponseEntity<ApiResponse<ApiPage<QuizSubmissionResultResponse>>> teacherViewQuizResult(@PathVariable Long id, @Nullable QuizResultRequest request, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(activityService.teacherViewQuizResult(id, request, pageable)));
    }

    @Operation(summary = "giáo viên/học sinh xem bài làm quiz")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    @GetMapping("quiz/review/{id}")
    public ResponseEntity<ApiResponse<QuizSubmittionDto>> reviewQuiz(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ApiResponse.success(activityService.reviewQuiz(id)));
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


    //
    @PutMapping("/assignments/{assignmentId}/grading")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ApiResponse<Boolean>> gradeAssignmentSubmit(@PathVariable Long assignmentId, @RequestBody List<GradeAssignmentSubmitionRequest> gradeAssignmentSubmitionRequestList) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(activityService.gradeAssignmentSubmit(assignmentId, gradeAssignmentSubmitionRequestList)));
    }

    @GetMapping("/assignments/{assignmentId}/submits")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ApiResponse<ApiPage<AssignmentSubmitionDto>>> getAssignmentSubmit(@PathVariable Long assignmentId, List<Long> classIds, Pageable pageable) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(activityService.getAllAssignmentSubmit(assignmentId, classIds, pageable)));
    }

}
