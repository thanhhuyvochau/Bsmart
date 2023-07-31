package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.service.IAssignmentFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/assignment-files")
public class AssignmentFileController {
    private final IAssignmentFileService assignmentFileService;

    public AssignmentFileController(IAssignmentFileService assignmentFileService) {
        this.assignmentFileService = assignmentFileService;
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('TEACHER','MANAGER','ADMIN','STUDENT')")
    public ResponseEntity<ApiResponse<Boolean>> deleteAssignmentFile(List<Long> ids) {
        return ResponseEntity.ok(ApiResponse.success(assignmentFileService.deleteAssignmentFile(ids)));
    }
}
