package fpt.project.bsmart.controller;


import fpt.project.bsmart.config.security.jwt.util.JWTUtil;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.request.CreateAccountRequest;
import fpt.project.bsmart.entity.request.User.SocialProfileEditRequest;
import fpt.project.bsmart.entity.request.UserRequest;
import fpt.project.bsmart.entity.response.UserResponse;
import fpt.project.bsmart.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final IUserService userService;
    @Autowired
    private JWTUtil util;
    @Autowired
    private AuthenticationManager authenticationManager;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Long> RegisterAccount(@RequestBody CreateAccountRequest createAccountRequest) {
        return ResponseEntity.ok(userService.saveUser(createAccountRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody UserRequest request) {

        //Validate username/password with DB(required in case of Stateless Authentication)
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()));
        String token = util.generateToken(request.getUsername());
        return ResponseEntity.ok(new UserResponse(token, "Token generated successfully!"));
    }

    @PostMapping("/edit/{id}/social")
    public ResponseEntity<ApiResponse<Long>> editSocialProfile(@PathVariable Long id, @RequestBody SocialProfileEditRequest socialProfileEditRequest){
        return ResponseEntity.ok(ApiResponse.success(userService.editUserSocialProfile(id, socialProfileEditRequest)));
    }

    @PostMapping("/getData")
    public ResponseEntity<String> testAfterLogin(Principal p) {
        return ResponseEntity.ok("You are accessing data after a valid Login. You are :" + p.getName());
    }
}
