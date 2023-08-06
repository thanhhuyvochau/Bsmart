package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.feedback.FeedbackTemplateDto;
import fpt.project.bsmart.entity.request.FeedbackTemplateRequest;
import fpt.project.bsmart.entity.request.FeedbackTemplateSearchRequest;
import fpt.project.bsmart.entity.request.StudentSubmitFeedbackRequest;
import fpt.project.bsmart.entity.response.FeedbackSubmissionResponse;
import fpt.project.bsmart.entity.response.FeedbackResponse;
import fpt.project.bsmart.service.IFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.List;

@RequestMapping("api/feedback")
@RestController
public class FeedbackController {
    private final IFeedbackService feedbackService;

    public FeedbackController(IFeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @Operation(summary = "admin / manager tạo feedback template")
    @PreAuthorize("hasAnyRole('ADMIN' , 'MANAGER')")
    @PostMapping("/template")
    public ResponseEntity<ApiResponse<Long>> createFeedbackTemplate(@RequestBody FeedbackTemplateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(feedbackService.createFeedbackTemplate(request)));
    }

//    @Operation(summary = "admin / manager tạo câu hỏi mặc đinh (Đánh giá Gv va khóa học)")
//    @PreAuthorize("hasAnyRole('ADMIN' , 'MANAGER')")
//    @PostMapping("/question-default")
//    public ResponseEntity<ApiResponse<Boolean>> createQuestionTemplate(@RequestBody List<FeedbackTemplateRequest.FeedbackQuestionRequest> questions) {
//        return ResponseEntity.ok(ApiResponse.success(feedbackService.createQuestionTemplate(questions)));
//    }

    @Operation(summary = "admin / manager update feedback template")
    @PreAuthorize("hasAnyRole('ADMIN' , 'MANAGER')")
    @PutMapping("/template/{id}")
    public ResponseEntity<ApiResponse<Long>> updateFeedbackTemplate(@PathVariable Long id, @RequestBody FeedbackTemplateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(feedbackService.updateFeedbackTemplate(id, request)));
    }

    @Operation(summary = "admin / manager delete feedback template")
    @PreAuthorize("hasAnyRole('ADMIN' , 'MANAGER')")
    @DeleteMapping("/template/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteFeedbackTemplate(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(feedbackService.deleteFeedbackTemplate(id)));
    }

    @Operation(summary = "admin / manager get all feedback template")
    @PreAuthorize("hasAnyRole('ADMIN' , 'MANAGER')")
    @GetMapping("/template")
    public ResponseEntity<ApiResponse<ApiPage<FeedbackTemplateDto>>> getAllTemplate(@Nullable FeedbackTemplateSearchRequest request, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(feedbackService.getAll(request, pageable)));
    }

    @Operation(summary = "admin / manager get feedback template by ID")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN',  'MANAGER')")
    @GetMapping("/template/{id}")
    public ResponseEntity<ApiResponse<FeedbackTemplateDto>> getFeedbackTemplateById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(feedbackService.getTemplateById(id)));
    }


    @Operation(summary = "admin / manager change default template")
    @PreAuthorize("hasAnyRole('ADMIN' , 'MANAGER')")
    @PutMapping("/default/{id}")
    public ResponseEntity<ApiResponse<Boolean>> changeDefaultFeedbackTemplate(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(feedbackService.changeDefaultTemplate(id)));
    }

    @Operation(summary = "admin / manager  assign feedback for class")
    @PreAuthorize("hasAnyRole('ADMIN' , 'MANAGER')")
    @PutMapping("/{id}/classes")
    public ResponseEntity<ApiResponse<Boolean>> assignFeedbackTemplateForClass(@PathVariable Long id, @RequestBody List<Long> classId) {
        return ResponseEntity.ok(ApiResponse.success(feedbackService.assignFeedbackTemplateForClass(id, classId)));
    }

    @Operation(summary = "Học sinh làm feedback ")
    @PreAuthorize("hasAnyRole('STUDENT')")
    @PostMapping("/{classId}/submit")
    public ResponseEntity<ApiResponse<Long>> studentSubmitFeedback(@PathVariable Long classId, @RequestBody List<StudentSubmitFeedbackRequest> request) {
        return ResponseEntity.ok(ApiResponse.success(feedbackService.studentSubmitFeedback(classId, request)));
    }

//    @Operation(summary = "student update feedback")
//    @PreAuthorize("hasAnyRole('STUDENT')")
//    @PutMapping("/submit/{id}")
//    public ResponseEntity<ApiResponse<Long>> studentUpdateFeedback(@PathVariable Long id, @RequestBody StudentSubmitFeedbackRequest request) {
//        return ResponseEntity.ok(ApiResponse.success(feedbackService.studentUpdateFeedback(id, request)));
//    }

    @Operation(summary = "get class feedback")
    @PreAuthorize("hasAnyRole('TEACHER','MANAGER','ADMIN')")
    @GetMapping("submission/class/{id}")
    public ResponseEntity<ApiResponse<ApiPage<FeedbackSubmissionResponse>>> getClassFeedback(@PathVariable Long id, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(feedbackService.getClassFeedback(id, pageable)));
    }

    @Operation(summary = "get course feedback")
    @GetMapping("/rate/course/{id}")
    public ResponseEntity<ApiResponse<FeedbackResponse>> getCourseFeedback(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(feedbackService.getCourseFeedback(id)));
    }

    @Operation(summary = "get mentor feedback")
    @GetMapping("/rate/mentor/{id}")
    public ResponseEntity<ApiResponse<FeedbackResponse>> getMentorFeedbackRate(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(feedbackService.getMentorFeedback(id)));
    }


}
