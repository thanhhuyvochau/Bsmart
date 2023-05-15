package fpt.project.bsmart.controller;

import fpt.project.bsmart.config.security.jwt.JwtUtils;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.request.JwtResponse;
import fpt.project.bsmart.entity.request.LoginRequest;
import fpt.project.bsmart.repository.RoleRepository;
import fpt.project.bsmart.repository.UserRepository;
import fpt.project.bsmart.service.IAuthService;
import fpt.project.bsmart.util.SecurityUtil;
import fpt.project.bsmart.util.email.EmailUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;


    private final RoleRepository roleRepository;


    private final PasswordEncoder encoder;


    private final IAuthService iAuthService;
    private final EmailUtil emailUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, IAuthService iAuthService, EmailUtil emailUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.iAuthService = iAuthService;
        this.emailUtil = emailUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> userLogin(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(ApiResponse.success(iAuthService.userLogin(loginRequest)));
    }

    @GetMapping("/send-mock-verify")
    public ResponseEntity<ApiResponse<Boolean>> sendMockMail() {
        return ResponseEntity.ok(ApiResponse.success(emailUtil.sendMockVerifyEmailTo(SecurityUtil.getCurrentUser())));
    }
}