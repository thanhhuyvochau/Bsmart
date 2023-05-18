package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.request.JwtResponse;
import fpt.project.bsmart.entity.request.LoginRequest;
import fpt.project.bsmart.entity.response.VerifyResponse;
import fpt.project.bsmart.service.IAuthService;
import fpt.project.bsmart.service.IUserService;
import fpt.project.bsmart.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IAuthService iAuthService;
    private final IUserService userService;

    public AuthController(IAuthService iAuthService, IUserService userService) {
        this.iAuthService = iAuthService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> userLogin(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(ApiResponse.success(iAuthService.userLogin(loginRequest)));
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<VerifyResponse>> verifyAccount(@RequestParam("code") String code) {
        return ResponseEntity.ok(ApiResponse.success(userService.verifyAccount(code)));
    }

    @GetMapping("/resend-verify")
    public ResponseEntity<ApiResponse<Boolean>> resendVerifyEmail() {
        return ResponseEntity.ok(ApiResponse.success(userService.resendVerifyEmail()));
    }
}

