package fpt.project.bsmart.controller;


import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.UserDto;
import fpt.project.bsmart.entity.request.CreateAccountRequest;
import fpt.project.bsmart.entity.request.UploadImageRequest;
import fpt.project.bsmart.entity.request.User.ChangePasswordRequest;
import fpt.project.bsmart.entity.request.User.MentorPersonalProfileEditRequest;
import fpt.project.bsmart.entity.request.User.PersonalProfileEditRequest;
import fpt.project.bsmart.entity.request.User.SocialProfileEditRequest;
import fpt.project.bsmart.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final IUserService iUserService;

    public UserController(IUserService iUserService) {
        this.iUserService = iUserService;
    }



    @Operation(summary = "Lấy thông tin user theo id")
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(iUserService.getUserById(id)));
    }

    @Operation(summary = "Lấy thông tin user đang đăng nhập hiện tại")
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentLoginUser(){
        return ResponseEntity.ok(ApiResponse.success(iUserService.getLoginUser()));
    }

    @Operation(summary = "Xóa liên kết mạng xã hội")
    @PreAuthorize("hasAnyRole('TEACHER','STUDENT')")
    @PutMapping("/social/remove")
    public ResponseEntity<ApiResponse<Long>> removeSocialLink(String link){
        return ResponseEntity.ok(ApiResponse.success(iUserService.removeSocialLink(link)));
    }
    @Operation(summary = "Chỉnh sửa liên kết mạng xã hội")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    @PutMapping("/social")
    public ResponseEntity<ApiResponse<Long>> editSocialProfile(@RequestBody SocialProfileEditRequest socialProfileEditRequest){
        return ResponseEntity.ok(ApiResponse.success(iUserService.editUserSocialProfile(socialProfileEditRequest)));
    }

    @Operation(summary = "Thay đổi mật khẩu")
    @PutMapping("/password")
    public ResponseEntity<ApiResponse<Long>> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest){
        return ResponseEntity.ok(ApiResponse.success(iUserService.changePassword(changePasswordRequest)));
    }

    @Operation(summary = "Member chỉnh sửa thông tin cá nhân")
    @PreAuthorize("hasAnyRole('STUDENT')")
    @PutMapping("/member-personal")
    public ResponseEntity<ApiResponse<Long>> editMemberPersonalProfile(@RequestBody PersonalProfileEditRequest personalProfileEditRequest){
        return ResponseEntity.ok(ApiResponse.success(iUserService.editUserPersonalProfile(personalProfileEditRequest)));
    }

    @Operation(summary = "Mentor chỉnh sửa thông tin cá nhân")
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PutMapping("/mentor-personal")
    public ResponseEntity<ApiResponse<Long>> editMentorPersonalProfile(@RequestBody MentorPersonalProfileEditRequest mentorPersonalProfileEditRequest){
        return ResponseEntity.ok(ApiResponse.success(iUserService.editMentorPersonalProfile(mentorPersonalProfileEditRequest)));
    }


    @Operation(summary = "upload dại diện - CMMD.CDCC")
    @PreAuthorize("hasAnyRole('TEACHER' , 'STUDENT')")
    @PostMapping("/upload-image")
    public ResponseEntity<ApiResponse<Long>> uploadImageRegisterProfile( @ModelAttribute UploadImageRequest uploadImageRequest) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(iUserService.uploadImageProfile( uploadImageRequest)));
    }

    @Operation(summary = "upload nhiều bằng cấp ")
    @PreAuthorize("hasAnyRole('TEACHER' , 'STUDENT')")
    @PostMapping("/upload-degree")
    public ResponseEntity<ApiResponse<Boolean>> uploadDegree( @RequestParam List<Long> degreeIdsToDelete ,  @RequestParam("files") MultipartFile[] files) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(iUserService.uploadDegree( degreeIdsToDelete, files)));
    }


    @Operation(summary = "Member / Mentor register account")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Long>> registerAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest) {
        return ResponseEntity.ok(ApiResponse.success(iUserService.registerAccount(createAccountRequest)));
    }



}
