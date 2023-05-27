package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.FeedbackQuestionDto;
import fpt.project.bsmart.entity.dto.FeedbackTemplateDto;
import fpt.project.bsmart.entity.request.feedback.FeedbackTemplateRequest;
import fpt.project.bsmart.entity.request.feedback.FeedbackQuestionRequest;
import fpt.project.bsmart.entity.request.feedback.SubCourseFeedbackRequest;
import fpt.project.bsmart.service.IFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/feedbacks")
public class FeedbackController {
    private final IFeedbackService feedbackService;

    public FeedbackController(IFeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @Operation(summary = "Lấy toàn bộ câu hỏi feedback")
    @GetMapping("/question")
    public ResponseEntity<ApiResponse<List<FeedbackQuestionDto>>> getAllFeedbackQuestions(){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.getAllFeedbackQuestions()));
    }

    @Operation(summary = "Lấy câu hỏi feedback theo id")
    @GetMapping("/question/{id}")
    public ResponseEntity<ApiResponse<FeedbackQuestionDto>> getFeedbackQuestionById(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.getFeedbackQuestionById(id)));
    }

    @Operation(summary = "Thêm câu hỏi feedback")
    @PostMapping("/question")
    public ResponseEntity<ApiResponse<Long>> addNewQuestion(@RequestBody FeedbackQuestionRequest feedbackQuestionRequest){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.addNewQuestion(feedbackQuestionRequest)));
    }

    @Operation(summary = "Cập nhật câu hỏi feedback")
    @PutMapping("/question/{id}")
    public ResponseEntity<ApiResponse<Long>> updateFeedbackQuestion(@PathVariable Long id, @RequestBody FeedbackQuestionRequest feedbackQuestionRequest){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.updateFeedbackQuestion(id, feedbackQuestionRequest)));
    }

    @Operation(summary = "Xóa câu hỏi feedback")
    @DeleteMapping("/question/{id}")
    public ResponseEntity<ApiResponse<Long>> deleteFeedbackQuestion(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.deleteFeedbackQuestion(id)));
    }

    //@Operation(summary = "Học sinh feedback subcourse")
    //@PostMapping("/sub-courses")
    public ResponseEntity<ApiResponse<Long>> addNewSubCourseFeedback(@RequestBody SubCourseFeedbackRequest subCourseFeedbackRequest){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.addNewSubCourseFeedback(subCourseFeedbackRequest)));
    }

    @Operation(summary = "Lấy toàn bộ feedback template")
    @GetMapping("/template")
    public ResponseEntity<ApiResponse<List<FeedbackTemplateDto>>> getAllFeedbackTemplate(){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.getAllFeedbackTemplate()));
    }

    @Operation(summary = "Lấy template feedback theo id")
    @GetMapping("/template/{id}")
    public ResponseEntity<ApiResponse<FeedbackTemplateDto>> getFeedbackTemplateById(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.getFeedbackTemplateById(id)));
    }

    @Operation(summary = "Thêm 1 template feedback")
    @PostMapping("/template")
    public ResponseEntity<ApiResponse<Long>> addNewFeedbackTemplate(@RequestBody FeedbackTemplateRequest feedbackTemplateRequest){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.addNewFeedbackTemplate(feedbackTemplateRequest)));
    }

    @Operation(summary = "Cập nhật template feedback")
    @PutMapping("/template/{id}")
    public ResponseEntity<ApiResponse<Long>> updateFeedbackTemplate(@PathVariable Long id, @RequestBody FeedbackTemplateRequest feedbackTemplateRequest){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.updateFeedbackTemplate(id, feedbackTemplateRequest)));
    }

    @Operation(summary = "Xóa template feedback")
    @DeleteMapping("/template/{id}")
    public ResponseEntity<ApiResponse<Long>> deleteFeedbackTemplate(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.deleteFeedbackTemplate(id)));
    }


    @Operation(summary = "Update template feedback cho sub course")
    @PostMapping("template/{templateId}/sub-course/{subCourseId}")
    public ResponseEntity<ApiResponse<Long>> updateFeedbackTemplateToSubCourse(@PathVariable Long templateId, @PathVariable Long subCourseId){
        return ResponseEntity.ok(ApiResponse.success(feedbackService.updateFeedbackTemplateToSubCourse(templateId, subCourseId)));
    }
}
