package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.FeedbackTemplateDto;
import fpt.project.bsmart.entity.request.UpdateSubCourseFeedbackTemplateRequest;
import fpt.project.bsmart.entity.request.feedback.AddFeedbackTemplateRequest;
import fpt.project.bsmart.entity.request.feedback.AddQuestionRequest;
import fpt.project.bsmart.entity.request.feedback.SubCourseFeedbackRequest;
import fpt.project.bsmart.service.IFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/feedbacks")
public class FeedbackController {
    private final IFeedbackService feedbackService;

    public FeedbackController(IFeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @Operation(summary = "Thêm câu hỏi feedback")
    @PostMapping("/question")
    public ResponseEntity<ApiResponse<Long>> addNewQuestion(@RequestBody AddQuestionRequest addQuestionRequest){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.addNewQuestion(addQuestionRequest)));
    }

    //@Operation(summary = "Học sinh feedback subcourse")
    //@PostMapping("/sub-courses")
    public ResponseEntity<ApiResponse<Long>> addNewSubCourseFeedback(@RequestBody SubCourseFeedbackRequest subCourseFeedbackRequest){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.addNewSubCourseFeedback(subCourseFeedbackRequest)));
    }

    @Operation(summary = "Lấy template feedback")
    @GetMapping("/template/{id}")
    public ResponseEntity<ApiResponse<FeedbackTemplateDto>> getFeedbackTemplateById(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.getFeedbackTemplateById(id)));
    }

    @Operation(summary = "Update template feedback cho sub course")
    @PostMapping("sub-courses/feedback-templates")
    public ResponseEntity<ApiResponse<Long>> updateFeedbackTemplateToSubCourse(@RequestBody UpdateSubCourseFeedbackTemplateRequest updateSubCourseFeedbackTemplateRequest){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.updateFeedbackTemplateToSubCourse(updateSubCourseFeedbackTemplateRequest)));
    }

    @Operation(summary = "Thêm 1 template feedback")
    @PostMapping("/template")
    public ResponseEntity<ApiResponse<Long>> addNewFeedbackTemplate(@RequestBody AddFeedbackTemplateRequest addFeedbackTemplateRequest){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.addNewFeedbackTemplate(addFeedbackTemplateRequest)));
    }
}
