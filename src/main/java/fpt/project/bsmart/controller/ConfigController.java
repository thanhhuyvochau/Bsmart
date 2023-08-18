package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.request.ConfigReferralCodeRequest;
import fpt.project.bsmart.entity.response.ConfigReferralCodeResponse;
import fpt.project.bsmart.service.ConfigReferralCodeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/config")
public class ConfigController {
    private final ConfigReferralCodeService configReferralCodeService;

    public ConfigController(ConfigReferralCodeService configReferralCodeService) {
        this.configReferralCodeService = configReferralCodeService;
    }

    @PostMapping("/referral-code")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @Operation(summary = "Tạo cấu hình cho mã giảm giá")
    public ResponseEntity<ApiResponse<ConfigReferralCodeResponse>> configReferralCode(@RequestBody @Valid ConfigReferralCodeRequest request) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(configReferralCodeService.config(request)));
    }

    @GetMapping("/referral-code")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @Operation(summary = "Lấy cấu hình mã giảm giá hiện tại")
    public ResponseEntity<ApiResponse<ConfigReferralCodeResponse>> getConfigReferralCode() {
        return ResponseEntity.ok(ApiResponse.success(configReferralCodeService.getActiveConfig()));
    }

}
