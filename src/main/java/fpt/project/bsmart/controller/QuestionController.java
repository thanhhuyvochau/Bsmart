package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.QuestionDto;
import fpt.project.bsmart.entity.request.AddQuestionRequest;
import fpt.project.bsmart.entity.request.EditQuestionRequest;
import fpt.project.bsmart.entity.request.QuestionFilter;
import fpt.project.bsmart.service.IQuestionService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("api/question")
public class QuestionController {

    private final IQuestionService questionService;

    public QuestionController(IQuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/import")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> importQuestions(@RequestParam("file") MultipartFile file, @RequestParam("subjectId") Long subjectId) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(questionService.importQuestionToQuestionBank(file, subjectId)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> addQuestion(@RequestBody AddQuestionRequest request) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(questionService.addQuestionToQuestionBank(request)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public ResponseEntity<ApiResponse<ApiPage<QuestionDto>>> getQuestions(@RequestBody @Valid QuestionFilter filter, Pageable pageable) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(questionService.getQuestions(filter, pageable)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public ResponseEntity<ApiResponse<QuestionDto>> getQuestions(@PathVariable("id") Long id) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(questionService.getQuestion(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> updateQuestion(@PathVariable("id") Long id, @RequestBody @Valid EditQuestionRequest editQuestionRequest) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(questionService.editQuestionToQuestionBank(id, editQuestionRequest)));
    }
}
