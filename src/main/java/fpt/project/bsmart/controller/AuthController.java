package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.constant.EVerifyStatus;
import fpt.project.bsmart.entity.request.JwtResponse;
import fpt.project.bsmart.entity.request.LoginRequest;
import fpt.project.bsmart.entity.response.VerifyResponse;
import fpt.project.bsmart.service.IAuthService;
import fpt.project.bsmart.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

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
        VerifyResponse response = userService.verifyAccount(code);
        if (!Objects.equals(response.getStatus(), EVerifyStatus.SUCCESS.name())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(response.getMessage());
        }
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/resend-verify")
    public ResponseEntity<ApiResponse<Boolean>> resendVerifyEmail() {
        return ResponseEntity.ok(ApiResponse.success(userService.resendVerifyEmail()));
    }
}

