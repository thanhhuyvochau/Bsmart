package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.FeedBackDto;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.service.IAdminService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final IAdminService iAdminService;

    public AdminController(IAdminService iAdminService) {
        this.iAdminService = iAdminService;
    }

    // MANGER ACCOUNT



    @Operation(summary = "xem hoc sinh feedback lớp ")
    @GetMapping("/{classId}/view-feadback")
    @PreAuthorize("hasAnyAuthority('MANAGER','ROOT')")
    public ResponseEntity<ApiResponse<FeedBackDto>> viewStudentFeedbackClass(@PathVariable long classId) {
        return ResponseEntity.ok(ApiResponse.success(iAdminService.viewStudentFeedbackClass(classId)));
    }




}
