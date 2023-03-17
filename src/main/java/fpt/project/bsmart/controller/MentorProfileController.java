package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.MentorProfileDTO;
import fpt.project.bsmart.entity.request.ImageRequest;
import fpt.project.bsmart.entity.request.UpdateMentorProfileRequest;
import fpt.project.bsmart.service.IMentorProfileService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/mentorProfiles")
public class MentorProfileController {
    private final IMentorProfileService mentorProfileService;

    public MentorProfileController(IMentorProfileService mentorProfileService) {
        this.mentorProfileService = mentorProfileService;
    }

    @Operation(summary = "Lấy thông tin giảng viên")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MentorProfileDTO>> getMentorProfile(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.getMentorProfile(id)));
    }

    @Operation(summary = "Lấy danh sách giảng viên chờ duyệt")
    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<MentorProfileDTO>>> getPendingMentorProfile(){
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.getPendingMentorProfileList()));
    }

    @Operation(summary = "Duyệt hồ sơ giảng viên")
    @PreAuthorize("hasAuthority('MANAGER')")
    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<Long>> approveMentorProfile(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.approveMentorProfile(id)));
    }

    @Operation(summary = "Cập nhật hồ sơ giảng viên")
    @PreAuthorize("hasAuthority('TEACHER')")
    @PutMapping
    public ResponseEntity<ApiResponse<Long>> updateMentorProfile(UpdateMentorProfileRequest updateMentorProfileRequest){
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.updateMentorProfile(updateMentorProfileRequest)));
    }

    @Operation(summary = "CHƯA HỖ TRỢ --- Upload chứng chỉ của giảng viên")
    @PreAuthorize("hasAuthority('TEACHER')")
    @PostMapping("/certificate")
    public ResponseEntity<ApiResponse<List<Long>>> updateMentorCertificate(List<ImageRequest> imageRequests){
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.updateCertificate(imageRequests)));
    }
}