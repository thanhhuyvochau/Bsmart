package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.CommentDto;
import fpt.project.bsmart.entity.request.CreateCommentRequest;
import fpt.project.bsmart.entity.request.VoteRequest;
import fpt.project.bsmart.service.ICommentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final ICommentService commentService;

    public CommentController(ICommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('STUDENT','MANAGER','TEACHER')")
    public ResponseEntity<ApiResponse<List<CommentDto>>> getCommentsByQuestion(@RequestParam Long questionId) {
        List<CommentDto> comments = commentService.getCommentByQuestion(questionId);
        return ResponseEntity.ok(ApiResponse.success(comments));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('STUDENT','MANAGER','TEACHER')")
    public ResponseEntity<ApiResponse<CommentDto>> createComment(@RequestBody CreateCommentRequest createCommentRequest) {
        return ResponseEntity.ok(ApiResponse.success(commentService.createComment(createCommentRequest)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('STUDENT','MANAGER','TEACHER')")
    public ResponseEntity<ApiResponse<CommentDto>> putComment(@PathVariable Long id, @RequestBody CreateCommentRequest createCommentRequest) {
        return ResponseEntity.ok(ApiResponse.success(commentService.updateComment(id, createCommentRequest)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER','ROOT')")
    public ResponseEntity<ApiResponse<Boolean>> deleteComment(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(commentService.deleteComment(id)));
    }

    @PostMapping("/vote")
    @Operation(description = "Bỏ phiếu cho bình luận")
    @PreAuthorize("hasAnyAuthority('STUDENT','MANAGER','TEACHER')")
    public ResponseEntity<ApiResponse<Boolean>> voteComment(@RequestBody VoteRequest request) {
        return ResponseEntity.ok(ApiResponse.success(commentService.voteComment(request)));
    }
}
