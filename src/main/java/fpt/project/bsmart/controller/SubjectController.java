package fpt.project.bsmart.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.ForumDto;
import fpt.project.bsmart.entity.request.SubjectRequest;
import fpt.project.bsmart.entity.request.SubjectSearchRequest;
import fpt.project.bsmart.entity.response.SubjectResponse;
import fpt.project.bsmart.service.IForumService;
import fpt.project.bsmart.service.ISubjectService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/subjects")
public class SubjectController {

    private final ISubjectService subjectService;
    private final IForumService forumService;

    public SubjectController(ISubjectService subjectService, IForumService forumService) {
        this.subjectService = subjectService;
        this.forumService = forumService;
    }

    @Operation(summary = "Tạo mới subject")
    @PostMapping
    public ResponseEntity<ApiResponse<SubjectResponse>> createNewSubject(@Nullable @RequestBody SubjectRequest subjectRequest) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(subjectService.createNewSubject(subjectRequest)));
    }


    @Operation(summary = "Sửa subject bằng id ")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectResponse>> updateSubject(@PathVariable Long id, @Nullable @RequestBody SubjectRequest subjectRequest) {
        return ResponseEntity.ok(ApiResponse.success(subjectService.updateSubject(id, subjectRequest)));
    }

    @Operation(summary = "Xóa subject bằng id")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> deleteSubject(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(subjectService.deleteSubject(id)));
    }


    @GetMapping("/{id}")
    @Operation(summary = "Lấy môn bằng id")
    public ResponseEntity<ApiResponse<SubjectResponse>> getSubject(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(subjectService.getSubject(id)));
    }

    @GetMapping("/suggest")
    @Operation(summary = "Gợi ý subject cho học sinh")
    public ResponseEntity<ApiResponse<ApiPage<SubjectResponse>>> suggestSubjectForStudent(@RequestParam Long studentId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(subjectService.suggestSubjectForStudent(studentId, pageable)));
    }


    @Operation(summary = "Lấy tất cả các môn không phân trang")
    @GetMapping("/unpage")
    public ResponseEntity<ApiResponse<List<SubjectResponse>>> getSubjectsWithoutPaging() {
        return ResponseEntity.ok(ApiResponse.success(subjectService.getAllWithoutPaging()));
    }

    @Operation(summary = "Lấy tất cả các môn có phân trang")
    @GetMapping
    public ResponseEntity<ApiResponse<ApiPage<SubjectResponse>>> getSubjectsWithPaging(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(subjectService.getAllWithPaging(pageable)));
    }

    @Operation(summary = "Tìm Kiếm subject")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<ApiPage<SubjectResponse>>> searchSubject(@Nullable SubjectSearchRequest query,
                                                                               Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(subjectService.searchSubject(query, pageable)));
    }

    @Operation(summary = "Lấy tất cả các môn của giáo viên không phân trang")
    @GetMapping("/teacher/subjects")
    public ResponseEntity<ApiResponse<List<SubjectResponse>>> getSubjectOfTeacher() {
        return ResponseEntity.ok(ApiResponse.success(subjectService.getSubjectOfTeacher()));
    }

    @Operation(summary = "Lấy chi tiết diễn đàn của một môn học")
    @GetMapping("/forum")
    public ResponseEntity<ApiResponse<ForumDto>> getForumOfSubject(@RequestParam Long subjectId,String q) {
        return ResponseEntity.ok(ApiResponse.success(forumService.getForumBySubject(subjectId,q)));
    }
}
