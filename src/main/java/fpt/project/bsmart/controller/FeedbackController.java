package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.feedback.FeedbackTemplateDto;
import fpt.project.bsmart.entity.request.FeedbackTemplateRequest;
import fpt.project.bsmart.entity.request.StudentSubmitFeedbackRequest;
import fpt.project.bsmart.entity.response.FeedbackSubmissionResponse;
import fpt.project.bsmart.entity.response.MentorFeedbackResponse;
import fpt.project.bsmart.service.IFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;

@RequestMapping("api/feedback")
@RestController
public class FeedbackController {
    private final IFeedbackService feedbackService;

    public FeedbackController(IFeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @Operation(summary = "admin create feedback template")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/template")
    public ResponseEntity<ApiResponse<Long>> createFeedbackTemplate(@RequestBody FeedbackTemplateDto request){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.createFeedbackTemplate(request)));
    }

    @Operation(summary = "admin update feedback template")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/template")
    public ResponseEntity<ApiResponse<Long>> updateFeedbackTemplate(@RequestBody FeedbackTemplateDto request){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.updateFeedbackTemplate(request)));
    }

    @Operation(summary = "admin delete feedback template")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/template/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteFeedbackTemplate(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.deleteFeedbackTemplate(id)));
    }

    @Operation(summary = "admin get all template")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/template")
    public ResponseEntity<ApiResponse<ApiPage<FeedbackTemplateDto>>> getAllTemplate(@Nullable FeedbackTemplateRequest request, Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.getAll(request, pageable)));
    }

    @Operation(summary = "get feedback template by ID")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    @GetMapping("/template/{id}")
    public ResponseEntity<ApiResponse<FeedbackTemplateDto>> getFeedbackTemplateById(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.getTemplateById(id)));
    }

    @Operation(summary = "admin change default template")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/default/{id}")
    public ResponseEntity<ApiResponse<Boolean>> changeDefaultFeedbackTemplate(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.changeDefaultTemplate(id)));
    }

    @Operation(summary = "admin assign feedback for class")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/template/{templateId}/class/{classId}")
    public ResponseEntity<ApiResponse<Boolean>> assignFeedbackTemplateForClass(@PathVariable Long templateId, @PathVariable Long classId){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.assignFeedbackTemplateForClass(templateId, classId)));
    }

    @Operation(summary = "student submit feedback")
    @PreAuthorize("hasAnyRole('STUDENT')")
    @PostMapping("/submit/class/{classId}")
    public ResponseEntity<ApiResponse<Long>> studentSubmitFeedback(@PathVariable Long classId,@RequestBody StudentSubmitFeedbackRequest request){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.studentSubmitFeedback(classId, request)));
    }

    @Operation(summary = "teacher view class feedback")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("submission/class/{id}")
    public ResponseEntity<ApiResponse<ApiPage<FeedbackSubmissionResponse>>> teacherViewClassFeedback(@PathVariable Long id, Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.teacherViewClassFeedback(id, pageable)));
    }

    @GetMapping("/rate/mentor/{id}")
    public ResponseEntity<ApiResponse<MentorFeedbackResponse>> getMentorFeedbackRate(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.getMentorFeedback(id)));
    }
}
