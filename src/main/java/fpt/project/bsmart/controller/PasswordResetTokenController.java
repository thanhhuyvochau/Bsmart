package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.request.ResetPasswordRequest;
import fpt.project.bsmart.service.IPasswordResetTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reset-password")
public class PasswordResetTokenController {
    private final IPasswordResetTokenService iPasswordResetTokenService;

    public PasswordResetTokenController(IPasswordResetTokenService iPasswordResetTokenService) {
        this.iPasswordResetTokenService = iPasswordResetTokenService;
    }

    @GetMapping("/{resetToken}")
    public ResponseEntity<ApiResponse<Boolean>> checkResetTokenExist(@PathVariable String resetToken){
        return ResponseEntity.ok(ApiResponse.success(iPasswordResetTokenService.checkResetTokenExist(resetToken)));
    }

    @PostMapping("/{email}/generate-token")
    public ResponseEntity<ApiResponse<Boolean>> generateResetToken(@PathVariable String email){
        return ResponseEntity.ok(ApiResponse.success(iPasswordResetTokenService.generateResetToken(email)));
    }

    @PutMapping("/{token}")
    public ResponseEntity<ApiResponse<Boolean>> resetPassword(@PathVariable String token, @RequestBody ResetPasswordRequest request){
        return ResponseEntity.ok(ApiResponse.success(iPasswordResetTokenService.resetPassword(token, request)));
    }
}
