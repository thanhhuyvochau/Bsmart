package fpt.project.bsmart.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.UserDto;
import fpt.project.bsmart.entity.request.ClassFilterRequest;
import fpt.project.bsmart.entity.request.CreateAccountRequest;
import fpt.project.bsmart.entity.request.UploadImageRequest;
import fpt.project.bsmart.entity.request.User.*;
import fpt.project.bsmart.entity.request.mentorprofile.UserDtoRequest;
import fpt.project.bsmart.entity.response.SimpleClassResponse;
import fpt.project.bsmart.entity.response.WorkTimeResponse;
import fpt.project.bsmart.entity.response.member.MemberDetailResponse;
import fpt.project.bsmart.entity.response.mentor.MentorEditProfileResponse;
import fpt.project.bsmart.service.IClassService;
import fpt.project.bsmart.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private SimpMessagingTemplate template;
    private final IUserService iUserService;
    private final IClassService classService;

    public UserController(IUserService iUserService, IClassService classService) {
        this.iUserService = iUserService;

        this.classService = classService;
    }


    @Operation(summary = "Lấy thông tin user theo id")
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iUserService.getUserById(id)));
    }

    @Operation(summary = "Admin lấy toàn bộ user")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping

    public ResponseEntity<ApiResponse<ApiPage<UserDto>>> adminGetAllUser(@Nullable UserSearchRequest request, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iUserService.adminGetAllUser(request, pageable)));

    }

    @Operation(summary = "Lấy thông tin user đang đăng nhập hiện tại")
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentLoginUser() {
        return ResponseEntity.ok(ApiResponse.success(iUserService.getLoginUser()));
    }

    @Operation(summary = "Lấy thông tin profile edit")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/profile-edit")
    public ResponseEntity<ApiResponse<MentorEditProfileResponse>> getProfileEdit() throws JsonProcessingException {
        return ResponseEntity.ok(ApiResponse.success(iUserService.getProfileEdit()));
    }


    @Operation(summary = "Lấy thông tin user cho trang giáo viên")
    @GetMapping("{userId}/teacher")
    public ResponseEntity<ApiResponse<UserDto>> getUserProfileForMentorPage(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(iUserService.getUserProfileForMentorPage(userId)));
    }

    @Operation(summary = "Xóa liên kết mạng xã hội")
    @PreAuthorize("hasAnyRole('TEACHER','STUDENT')")
    @PutMapping("/social/remove")
    public ResponseEntity<ApiResponse<Long>> removeSocialLink(String link) {
        return ResponseEntity.ok(ApiResponse.success(iUserService.removeSocialLink(link)));
    }

    @Operation(summary = "Chỉnh sửa liên kết mạng xã hội")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    @PutMapping("/social")
    public ResponseEntity<ApiResponse<Long>> editSocialProfile(@RequestBody SocialProfileEditRequest socialProfileEditRequest) {
        return ResponseEntity.ok(ApiResponse.success(iUserService.editUserSocialProfile(socialProfileEditRequest)));
    }

    @Operation(summary = "Thay đổi mật khẩu")
    @PutMapping("/password")
    public ResponseEntity<ApiResponse<Long>> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.ok(ApiResponse.success(iUserService.changePassword(changePasswordRequest)));
    }

    @Operation(summary = "Member chỉnh sửa thông tin cá nhân")
    @PreAuthorize("hasAnyRole('STUDENT')")
    @PutMapping("/member-personal")
    public ResponseEntity<ApiResponse<Long>> editMemberPersonalProfile(@RequestBody PersonalProfileEditRequest personalProfileEditRequest) {
        return ResponseEntity.ok(ApiResponse.success(iUserService.editUserPersonalProfile(personalProfileEditRequest)));
    }

    @Operation(summary = "Mentor chỉnh sửa thông tin cá nhân")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PutMapping("/mentor-personal")
    public ResponseEntity<ApiResponse<Long>> editMentorPersonalProfile(@RequestBody MentorPersonalProfileEditRequest mentorPersonalProfileEditRequest) {
        return ResponseEntity.ok(ApiResponse.success(iUserService.editMentorPersonalProfile(mentorPersonalProfileEditRequest)));
    }


    @Operation(summary = "upload dại diện - CMMD.CDCC")
    @PreAuthorize("hasAnyRole('TEACHER' , 'STUDENT')")
    @PostMapping("/upload-image")
    public ResponseEntity<ApiResponse<Long>> uploadImageRegisterProfile(
            @ModelAttribute UploadImageRequest uploadImageRequest
    ) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(iUserService.uploadImageProfile(uploadImageRequest)));
    }

    @Operation(summary = "upload nhiều bằng cấp ")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PostMapping("/upload-degree")
    public ResponseEntity<ApiResponse<Boolean>> uploadDegree(@Nullable @RequestParam List<Long> degreeIdsToDelete,
                                                             @Nullable @RequestParam("files") MultipartFile[] files,
                                                             @Nullable @RequestParam("status") Boolean status) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(iUserService.uploadDegree(degreeIdsToDelete, files, status)));
    }


    @Operation(summary = "Member / Mentor register account")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Long>> registerAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest) {
        return ResponseEntity.ok(ApiResponse.success(iUserService.registerAccount(createAccountRequest)));
    }

    @Operation(summary = "Lấy lớp của học sinh / giáo viên")
    @GetMapping("/classes")
    public ResponseEntity<ApiResponse<ApiPage<SimpleClassResponse>>> getUserClasses(ClassFilterRequest request, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(classService.getUserClasses(request, pageable)));
    }

    @Operation(summary = "Admin Tạo Tài Khoản Manager")
    @PostMapping("/manager/register")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Long>> registerManagerAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest) {
        return ResponseEntity.ok(ApiResponse.success(iUserService.registerAccount(createAccountRequest)));
    }

    @Operation(summary = "Lấy tất cả lịch làm việc của học sinh / giáo viên ")
    @GetMapping("/timetables")
    public ResponseEntity<ApiResponse<List<WorkTimeResponse>>> getWorkingTimeOfUser() {
        return ResponseEntity.ok(ApiResponse.success(classService.getWorkingTime()));
    }

    @Operation(summary = "Manager / admin xem Lấy thông tin chi tiet giao vien  ")
    @GetMapping("{id}/mentor")
    public ResponseEntity<ApiResponse<UserDto>> managerGetMentorDetail(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iUserService.managerGetMentorDetail(id)));
    }

    @Operation(summary = "Manager / admin xem Lấy thông tin chi tiet hoc sinh")
    @GetMapping("{id}/member")
    public ResponseEntity<ApiResponse<MemberDetailResponse>> managerGetMemberDetail(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(iUserService.managerGetMemberDetail(id)));
    }


}