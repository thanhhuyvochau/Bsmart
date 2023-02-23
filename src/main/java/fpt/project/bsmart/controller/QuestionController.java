package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.QuestionDto;
import fpt.project.bsmart.entity.dto.QuestionSimpleDto;
import fpt.project.bsmart.entity.request.CreateQuestionRequest;
import fpt.project.bsmart.entity.request.VoteRequest;
import fpt.project.bsmart.service.IQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    private final IQuestionService iQuestionService;

    public QuestionController(IQuestionService iQuestionService) {
        this.iQuestionService = iQuestionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionDto>> getQuestion(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iQuestionService.getQuestion(id)));
    }
    @PostMapping
    public ResponseEntity<ApiResponse<QuestionDto>> createQuestion(@RequestBody CreateQuestionRequest createQuestionRequest) {
        return ResponseEntity.ok(ApiResponse.success(iQuestionService.createQuestion(createQuestionRequest)));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<QuestionDto>> updateQuestion(@PathVariable Long id, @RequestBody CreateQuestionRequest createQuestionRequest) {
        return ResponseEntity.ok(ApiResponse.success(iQuestionService.updateQuestion(id, createQuestionRequest)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> closeQuestion(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iQuestionService.closeQuestion(id)));
    }

    @PutMapping("/{id}/open")
    public ResponseEntity<ApiResponse<Boolean>> openQuestion(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iQuestionService.openQuestion(id)));
    }

    @PostMapping("/vote")
    @Operation(description = "Bỏ phiếu cho câu hỏi")
    public ResponseEntity<ApiResponse<Boolean>> voteQuestion(@RequestBody VoteRequest request) {
        return ResponseEntity.ok(ApiResponse.success(iQuestionService.voteQuestion(request)));
    }

    @GetMapping("/{forumId}/questions/search")
    public ResponseEntity<ApiResponse<ApiPage<QuestionSimpleDto>>> searchQuestion(@RequestParam String q, @PathVariable Long forumId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iQuestionService.searchQuestion(forumId, q, pageable)));
    }
}
