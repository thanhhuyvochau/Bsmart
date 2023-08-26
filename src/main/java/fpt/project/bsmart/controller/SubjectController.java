package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.SubjectDto;
import fpt.project.bsmart.entity.request.SubjectRequest;
import fpt.project.bsmart.service.ISubjectService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    private final ISubjectService iSubjectService;



    public SubjectController(ISubjectService iSubjectService) {
        this.iSubjectService = iSubjectService;
    }

    @Operation(summary = "Lấy tất cả subject")
    @GetMapping
    public ResponseEntity<ApiResponse<List<SubjectDto>>> getAllSubject() {
        return ResponseEntity.ok(ApiResponse.success(iSubjectService.getAllSubject()));
    }

    @Operation(summary = "Lấy subject theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectDto>> getSubject(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iSubjectService.getSubject(id)));
    }

    @Operation(summary = "Lấy subject theo category")
    @GetMapping("/category/{id}")
    public ResponseEntity<ApiResponse<List<SubjectDto>>> getSubjectsByCategory(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(iSubjectService.getSubjectsByCategory(id)));
    }

    @Operation(summary = "Tạo 1 subject")
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createSubject(@Valid @RequestBody SubjectRequest subjectRequest) {
        return ResponseEntity.ok(ApiResponse.success(iSubjectService.createSubject(subjectRequest)));
    }

    @Operation(summary = "Cập nhật thông tin 1 subject")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> updateSubject(@PathVariable Long id, @Valid @RequestBody SubjectRequest subjectRequest) {
        return ResponseEntity.ok(ApiResponse.success(iSubjectService.updateSubject(id, subjectRequest)));
    }

    @Operation(summary = "Xóa 1 subject")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> deleteSubject(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iSubjectService.deleteSubject(id)));
    }

    @Operation(summary = "Lấy subject (skill) của mentor ")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/mentor-skills")
    public ResponseEntity<ApiResponse<List<SubjectDto>>> getSubjectsByMentorSkill(){
        return ResponseEntity.ok(ApiResponse.success(iSubjectService.getSubjectsByMentorSkill()));
    }

}
