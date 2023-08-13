package fpt.project.bsmart.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.MentorProfileDTO;
import fpt.project.bsmart.entity.dto.UserDto;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.request.mentorprofile.UserDtoRequest;
import fpt.project.bsmart.entity.response.mentor.*;
import fpt.project.bsmart.entity.response.MentorProfileResponse;
import fpt.project.bsmart.service.IMentorProfileService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<ApiResponse<MentorProfileDTO>> getMentorProfile(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.getMentorProfile(id)));
    }

    @Operation(summary = "Lấy danh sách toàn bộ giảng viên")
    @GetMapping
    public ResponseEntity<ApiResponse<ApiPage<MentorProfileDTO>>> getAllMentors(@Nullable MentorSearchRequest mentorSearchRequest, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.getAllMentors(mentorSearchRequest, pageable)));
    }

    @Operation(summary = "Lấy toàn bộ danh sách giảng viên chỉ có id và tên")
    @GetMapping("/name")
    public ResponseEntity<ApiResponse<List<MentorProfileResponse>>> getAllMentorProfiles() {
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.getAllMentorProfiles()));
    }


    //     ################################## START MENTOR  ##########################################

    @Operation(summary = "Cập nhật hồ sơ giảng viên")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PutMapping
    public ResponseEntity<ApiResponse<Long>> updateMentorProfile(@RequestBody UpdateMentorProfileRequest updateMentorProfileRequest) {
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.updateMentorProfile(updateMentorProfileRequest)));
    }

    @Operation(summary = "CHƯA HỖ TRỢ --- Upload chứng chỉ của giảng viên")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PostMapping("/certificate")
    public ResponseEntity<ApiResponse<List<Long>>> updateMentorCertificate(List<ImageRequest> imageRequests) {
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.updateCertificate(imageRequests)));
    }

    @Operation(summary = "Lấy thông tin profile mentor chưa hoàn thiện và % ")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/completeness")
    public ResponseEntity<ApiResponse<CompletenessMentorProfileResponse>> getCompletenessMentorProfile() {
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.getCompletenessMentorProfile()));
    }

    @Operation(summary = "mentor TẠO yêu cầu sửa thông tin cá nhân")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PostMapping("/request-edit-profile")
    public ResponseEntity<ApiResponse<Long>> mentorCreateEditProfileRequest(
            @RequestBody UserDtoRequest request) throws JsonProcessingException {

        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.mentorCreateEditProfileRequest(request)));
    }
 @Operation(summary = "mentor GỬI  yêu cầu sửa thông tin cá nhân")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PostMapping("/{mentorProfileEditId}")
    public ResponseEntity<ApiResponse<Boolean>> mentorSendEditProfileRequest(@PathVariable Long mentorProfileEditId) throws JsonProcessingException {

        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.mentorSendEditProfileRequest(mentorProfileEditId)));
    }

//    @Operation(summary = "mentor tạo yêu cầu dạy thêm môn học mới")
//    @PreAuthorize("hasAnyRole('TEACHER')")
//    @PostMapping("/request-approval-skill")
//    public ResponseEntity<ApiResponse<Boolean>> mentorCreateApprovalSkill(@RequestBody MentorSendAddSkill mentorSendAddSkill) {
//        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.mentorCreateApprovalSkill(mentorSendAddSkill)));
//    }

    @Operation(summary = "mentor gửi yêu cầu phê duệt tài khoản")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PutMapping("/{id}/request-approval")
    public ResponseEntity<ApiResponse<Boolean>> mentorRequestApprovalCourse(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.mentorRequestApprovalAccount(id)));
    }

//    @Operation(summary = "mentor gửi yêu cầu dạy thêm môn học mới")
//    @PreAuthorize("hasAnyRole('TEACHER')")
//    @PutMapping("/request-approval-skill")
//    public ResponseEntity<ApiResponse<Boolean>> mentorRequestApprovalSkill(@RequestBody MentorSendSkillRequest mentorSendAddSkill) {
//        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.mentorRequestApprovalSkill(mentorSendAddSkill)));
//    }

    @Operation(summary = "mentor lấy các cầu dạy thêm môn học mới chưa gửi phê duyệt")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/request-approval")
    public ResponseEntity<ApiResponse<List<MentorGetRequestApprovalSkillResponse>>> ManagerGetRequestApprovalSkillResponse() throws Exception {
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.ManagerGetRequestApprovalSkillResponse()));
    }

    //     ################################## END MENTOR  ##########################################


    //     ################################## START MANAGER ##########################################


    @Operation(summary = "Manager Lấy danh sách giảng viên chờ duyệt")
    @PreAuthorize("hasAnyRole('MANAGER')")
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<ApiPage<UserDto>>> getPendingMentorProfile(@Nullable MentorSearchRequest request,
                                                                                 @PageableDefault(sort = "lastModified", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.getPendingMentorProfileList(request, pageable)));
    }

    @Operation(summary = "Manager( phê duyêt / từ chối / yêu cầu thay đổi )  hồ sơ mentor  + handle Phỏng vấn ")
    @PreAuthorize("hasAnyRole('MANAGER')")
    @PutMapping("/{id}/approval")
    public ResponseEntity<ApiResponse<Long>> approveMentorProfile(
            @PathVariable Long id,
            @RequestBody ManagerApprovalAccountRequest managerApprovalAccountRequest) {

        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.approveMentorProfile(id, managerApprovalAccountRequest)));
    }

    @Operation(summary = "manager lấy yêu câu  mở  thêm môn học mới của mentor ")
    @PreAuthorize("hasAnyRole('MANAGER')")
    @GetMapping("request-approval-skill")
    public ResponseEntity<ApiResponse<ApiPage<ManagerGetRequestApprovalSkillResponse>>> managerGetRequestApprovalSkill(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.managerGetRequestApprovalSkill(pageable)));
    }


    @Operation(summary = "manager phê duyệt  yêu cầu mở  thêm môn học mới")
    @PreAuthorize("hasAnyRole('MANAGER')")
    @PutMapping("{id}/request-approval-skill")
    public ResponseEntity<ApiResponse<Boolean>> managerHandleRequestApprovalSkill(@PathVariable Long id, @RequestBody ManagerApprovalSkillRequest managerApprovalSkillRequest) {
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.managerHandleRequestApprovalSkill(id, managerApprovalSkillRequest)));
    }

    @Operation(summary = "manager lấy  yêu cầu sửa thông tin cá nhân của mentor ")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/edit-profile")
    public ResponseEntity<ApiResponse<ApiPage<MentorEditProfileResponse>>>managerGetEditProfileRequest(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.managerGetEditProfileRequest(pageable)));
    }

    @Operation(summary = "manager xem chi tiết  yêu cầu sửa thông tin cá nhân của mentor và so sanh với profile cũ")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{mentorProfileEditId}/edit-profile-detail")
    public ResponseEntity<ApiResponse<MentorEditProfileDetailResponse>>managerGetEditProfileDetailRequest(
            @PathVariable Long mentorProfileEditId) throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(mentorProfileService.managerGetEditProfileDetailRequest(mentorProfileEditId)));
    }

    //     ################################## END MANAGER ##########################################

}
