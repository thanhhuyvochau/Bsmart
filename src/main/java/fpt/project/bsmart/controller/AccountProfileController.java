package fpt.project.bsmart.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.common.EAccountDetailStatus;
import fpt.project.bsmart.entity.dto.ProvincesDto;
import fpt.project.bsmart.entity.dto.ResourceDto;
import fpt.project.bsmart.entity.request.AccountDetailEditRequest;
import fpt.project.bsmart.entity.request.AccountDetailRequest;
import fpt.project.bsmart.entity.request.RequestEditAccountDetailRequest;
import fpt.project.bsmart.entity.request.UploadAvatarRequest;
import fpt.project.bsmart.entity.response.AccountDetailResponse;
import fpt.project.bsmart.entity.response.GenderResponse;
import fpt.project.bsmart.entity.response.ResponseAccountDetailResponse;
import fpt.project.bsmart.entity.response.VoiceResponse;
import fpt.project.bsmart.service.IAccountDetailService;
import fpt.project.bsmart.service.ISendMailService;
import fpt.project.bsmart.util.GenderUtil;
import fpt.project.bsmart.util.VoiceUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/account-profile")
public class AccountProfileController {


    private final RestTemplate restTemplate;
    private final IAccountDetailService iAccountDetailService;

    private final ISendMailService iSendMailService;

    @Value("${provinces}")
    private String province;

    public AccountProfileController(RestTemplate restTemplate, IAccountDetailService iAccountDetailService, ISendMailService iSendMailService) {
        this.restTemplate = restTemplate;
        this.iAccountDetailService = iAccountDetailService;
        this.iSendMailService = iSendMailService;
    }

    @Operation(summary = "Đăng ký làm gia sư cho vườn đậu")
    @PostMapping("/register-tutor")
    public ResponseEntity<ApiResponse<Long>> registerTutor(@RequestBody AccountDetailRequest accountDetailRequest) {
        return ResponseEntity.ok(ApiResponse.success(iAccountDetailService.registerTutor(accountDetailRequest)));
    }

    @Operation(summary = "upload dại diện - bằng cấp - CMMD.CDCC  để đk giảng dạy")
    @PostMapping("/{id}/image-register-profile")
    public ResponseEntity<List<ResourceDto>> uploadImageRegisterProfile(@PathVariable Long id, @ModelAttribute UploadAvatarRequest uploadImageRequest) throws IOException {
        return ResponseEntity.ok(iAccountDetailService.uploadImageRegisterProfile(id, uploadImageRequest));
    }

    @Operation(summary = "Admin phê duyệt request đăng ký giang dạy của giao vien")
    @PutMapping("/approve-request-register-profile")
    @PreAuthorize("hasAnyAuthority('MANAGER','ROOT')")
    public ResponseEntity<ResponseAccountDetailResponse> approveRegisterAccount(@RequestBody RequestEditAccountDetailRequest editAccountDetailRequest) {
        return ResponseEntity.ok(iAccountDetailService.approveRegisterAccount(editAccountDetailRequest));
    }

    @Operation(summary = "Admin từ chối request đăng ký giang dạy của giao vien")
    @PutMapping("/refuse-request-register-profile")
    @PreAuthorize("hasAnyAuthority('MANAGER','ROOT')")
    public ResponseEntity<ResponseAccountDetailResponse> refuseRegisterAccount(@RequestBody RequestEditAccountDetailRequest editAccountDetailRequest) {
        return ResponseEntity.ok(iAccountDetailService.refuseRegisterAccount(editAccountDetailRequest));
    }

    @Operation(summary = "Giáo viên cập nhật hồ sơ khi bị admin từ chối hoặc yêu cầu thay đổi thông tin . ")
    @PutMapping
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public ResponseEntity<Long> teacherUpdateProfileForAdmin(@RequestBody  AccountDetailEditRequest editAccountDetailRequest) {
        return ResponseEntity.ok(iAccountDetailService.teacherUpdateProfileForAdmin(editAccountDetailRequest));
    }

    @Operation(summary = "Admin yêu đầu thay đổi thông tin  request đăng ký giang dạy của giao vien")
    @PutMapping("/request-edit-register-profile")
    @PreAuthorize("hasAnyAuthority('MANAGER','ROOT')")
    public ResponseEntity<ResponseAccountDetailResponse> requestEditRegisterAccount(@RequestBody RequestEditAccountDetailRequest editAccountDetailRequest) {
        return ResponseEntity.ok(iAccountDetailService.requestEditRegisterAccount(editAccountDetailRequest));
    }

    @GetMapping("/teacher-detail")
    @Operation(summary = "Giáo viên xem thông tin hồ sơ mình vừa đăng ký ")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public ResponseEntity<ApiResponse<ResponseAccountDetailResponse>> teacherGetInfo() {
        return ResponseEntity.ok(ApiResponse.success(iAccountDetailService.teacherGetInfo()));
    }

    @GetMapping
    @Operation(summary = "Lấy tất cả request tạo tk để đang ký giang dạy theo trạng thái (đã duyệt/chờ)")
    @PreAuthorize("hasAnyAuthority('MANAGER','ROOT')")
    public ResponseEntity<ApiResponse<ApiPage<AccountDetailResponse>>> getRequestToActiveAccount(EAccountDetailStatus status, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iAccountDetailService.getRequestToActiveAccount(status, pageable)));
    }


    @GetMapping("/genders")
    @Operation(summary = "Lấy tất cả giới tính")
    public ResponseEntity<ApiResponse<List<GenderResponse>>> getGenderAsList() {
        return ResponseEntity.ok(ApiResponse.success(GenderUtil.getGendersAsList()));
    }

    @GetMapping("/voice")
    @Operation(summary = "Lấy tất cả giọng nói")
    public ResponseEntity<ApiResponse<List<VoiceResponse>>> getVoice() {
        return ResponseEntity.ok(ApiResponse.success(VoiceUtil.getVoice()));

    }
//        @PostMapping("/sendMail")
//    public ResponseEntity<ApiResponse<Boolean>> sendMail(List<EmailDto> emailDto) {
//        return ResponseEntity.ok(ApiResponse.success(iSendMailService.sendMail(emailDto)));
//
//    }


    @GetMapping("/provinces")
    @Operation(summary = "Lấy tât cả tỉnh thành ở viêt nam")
    public List<ProvincesDto> getProvinces() throws JsonProcessingException {

        ResponseEntity<List<ProvincesDto>> response = restTemplate.exchange("https://provinces.open-api.vn/api/?depth=1", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ProvincesDto>>() {
                });

        return response.getBody();
    }


//

    @GetMapping("/{accountId}/account-detail")
    @Operation(summary = "Lấy tất cả thông tin của giáo viên")
    @PreAuthorize("hasAnyAuthority('MANAGER','ROOT')")
    public ResponseEntity<ApiResponse<AccountDetailResponse>> getAccountDetail(@PathVariable Long accountId) {
        return ResponseEntity.ok(ApiResponse.success(iAccountDetailService.getAccountDetail(accountId)));
    }


}