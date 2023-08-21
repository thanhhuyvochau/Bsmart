package fpt.project.bsmart.controller;


import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.response.ReferralCodeResponse;
import fpt.project.bsmart.service.ReferralCodeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static fpt.project.bsmart.util.Constants.UrlConstants.COMMON_REFERRAL_CODE;
import static fpt.project.bsmart.util.Constants.UrlConstants.COMMON_ROOT;


@RestController
@RequestMapping(COMMON_ROOT + COMMON_REFERRAL_CODE)
public class ReferralCodeController {


    private final ReferralCodeService referralCodeService;

    public ReferralCodeController(ReferralCodeService referralCodeService) {
        this.referralCodeService = referralCodeService;
    }

    @GetMapping
    @Operation(summary = "Lấy mã giảm giá và kiểm tra để áp dụng cho học sinh mua khóa học")
    public ResponseEntity<ApiResponse<ReferralCodeResponse>> getReferralCodeToApplyDiscount(@RequestParam String code, @RequestParam Long courseId) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(referralCodeService.getReferralCodeToApplyDiscount(code, courseId)));
    }

}
