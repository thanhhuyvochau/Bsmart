package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.SubjectDto;
import fpt.project.bsmart.entity.request.SubjectRequest;
import fpt.project.bsmart.service.ISubjectService;
import org.springframework.http.ResponseEntity;
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


    @GetMapping
    public ResponseEntity<ApiResponse<List<SubjectDto>>> getAllSubject(){
        return ResponseEntity.ok(ApiResponse.success(iSubjectService.getAllSubject()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectDto>> getSubject(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(iSubjectService.getSubject(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createSubject(@Valid @RequestBody SubjectRequest subjectRequest){
        return ResponseEntity.ok(ApiResponse.success(iSubjectService.createSubject(subjectRequest)));
    }

    @PutMapping("/{id}")
    public  ResponseEntity<ApiResponse<Long>> updateSubject(@PathVariable Long id, @Valid @RequestBody SubjectRequest subjectRequest){
        return ResponseEntity.ok(ApiResponse.success(iSubjectService.updateSubject(id, subjectRequest)));
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<ApiResponse<Long>> deleteSubject(@PathVariable Long id){

        return ResponseEntity.ok(ApiResponse.success(iSubjectService.deleteSubject(id)));

    }
}
