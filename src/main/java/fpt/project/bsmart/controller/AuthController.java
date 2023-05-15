package fpt.project.bsmart.controller;

import fpt.project.bsmart.config.security.jwt.JwtUtils;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.request.JwtResponse;
import fpt.project.bsmart.entity.request.LoginRequest;
import fpt.project.bsmart.repository.RoleRepository;
import fpt.project.bsmart.repository.UserRepository;
import fpt.project.bsmart.service.IAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, IAuthService iAuthService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.iAuthService = iAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> userLogin(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(ApiResponse.success(iAuthService.userLogin(loginRequest)));
    }
}