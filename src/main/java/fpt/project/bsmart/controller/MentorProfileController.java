package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.constant.EMentorProfileStatus;
import fpt.project.bsmart.entity.dto.MentorProfileDTO;
import fpt.project.bsmart.entity.dto.UserDto;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.response.mentor.CompletenessMentorProfileResponse;
import fpt.project.bsmart.entity.response.MentorProfileResponse;
import fpt.project.bsmart.service.IMentorProfileService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.List;

@RestController
@RequestMapping("api/mentor-profiles")
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

    @Operation(summary = "Lấy danh sách toàn bộ giảng viên")
    @GetMapping
    public ResponseEntity<ApiResponse<ApiPage<MentorProfileDTO>>> getAllMentors(@Nullable MentorSearchRequest mentorSearchRequest, Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.getAllMentors(mentorSearchRequest, pageable)));
    }

    @Operation(summary = "Lấy toàn bộ danh sách giảng viên chỉ có id và tên")
    @GetMapping("/name")
    public ResponseEntity<ApiResponse<List<MentorProfileResponse>>> getAllMentorProfiles(){
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.getAllMentorProfiles()));
    }

    @Operation(summary = "Manager Lấy danh sách giảng viên chờ duyệt")
    @PreAuthorize("hasAnyRole('MANAGER')")
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<ApiPage<UserDto>>> getPendingMentorProfile(@Nullable MentorSearchRequest request, Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.getPendingMentorProfileList(request , pageable)));
    }



    @Operation(summary = "Manager phê duyêt / từ chối / yêu cầu thay đổi  hồ sơ mentor")
    @PreAuthorize("hasAnyRole('MANAGER')")
    @PutMapping("/{id}/approval")
    public ResponseEntity<ApiResponse<Long>> approveMentorProfile(@PathVariable Long id , @RequestBody ManagerApprovalAccountRequest managerApprovalAccountRequest){
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.approveMentorProfile(id, managerApprovalAccountRequest)));
    }

    @Operation(summary = "Cập nhật hồ sơ giảng viên")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PutMapping
    public ResponseEntity<ApiResponse<Long>> updateMentorProfile(@RequestBody UpdateMentorProfileRequest updateMentorProfileRequest){
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.updateMentorProfile(updateMentorProfileRequest)));
    }

    @Operation(summary = "CHƯA HỖ TRỢ --- Upload chứng chỉ của giảng viên")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PostMapping("/certificate")
    public ResponseEntity<ApiResponse<List<Long>>> updateMentorCertificate(List<ImageRequest> imageRequests){
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.updateCertificate(imageRequests)));
    }

//    @Operation(summary = "CHƯA HỖ TRỢ --- Upload chứng chỉ của giảng viên")
//    @PreAuthorize("hasAnyRole('TEACHER')")
//    @PostMapping("/certificate")
//    public ResponseEntity<ApiResponse<List<Long>>> updateAvatar(List<ImageRequest> imageRequests){
//        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.updateCertificate(imageRequests)));
//    }

    @Operation(summary = "Lấy thông tin profile mentor chưa hoàn thiện và % ")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/completeness")
    public ResponseEntity<ApiResponse<CompletenessMentorProfileResponse>> getCompletenessMentorProfile(){
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.getCompletenessMentorProfile()));
    }

    @Operation(summary = "mentor gửi yêu cầu phê duệt tài khoản")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PutMapping("/{id}/request-approval")
    public ResponseEntity<ApiResponse<Boolean>> mentorRequestApprovalCourse(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.mentorRequestApprovalAccount(id)));
    }

}
