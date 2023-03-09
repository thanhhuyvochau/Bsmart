package fpt.project.bsmart.controller;


import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.request.CreateAccountRequest;
import fpt.project.bsmart.entity.request.User.AccountProfileEditRequest;
import fpt.project.bsmart.entity.request.User.PersonalProfileEditRequest;
import fpt.project.bsmart.entity.request.User.SocialProfileEditRequest;
import fpt.project.bsmart.entity.request.UserRequest;
import fpt.project.bsmart.entity.response.UserResponse;
import fpt.project.bsmart.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import fpt.project.bsmart.entity.request.UploadImageRequest;
import fpt.project.bsmart.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
public class UserController {
    //    @Autowired
//    private JWTUtil util;
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
    private final IUserService iUserService;

    public UserController(IUserService iUserService) {
        this.iUserService = iUserService;
    }


//    @PostMapping("/register")
//    public ResponseEntity<Integer> RegisterAccount(@RequestBody CreateAccountRequest createAccountRequest) {
//        return ResponseEntity.ok(userService.saveUser(createAccountRequest));
//    }

//    @PostMapping("/login")
//    public ResponseEntity<UserResponse> login(@RequestBody UserRequest request) {
//
//        //Validate username/password with DB(required in case of Stateless Authentication)
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                request.getUsername(), request.getPassword()));
//        String token = util.generateToken(request.getUsername());
//        return ResponseEntity.ok(new UserResponse(token, "Token generated successfully!"));
//    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(iUserService.getUserById(id)));
    }
    @Operation(summary = "Chỉnh sửa liên kết mạng xã hội")
    @PutMapping("/{id}/social")
    public ResponseEntity<ApiResponse<Long>> editSocialProfile(@PathVariable Long id, @RequestBody SocialProfileEditRequest socialProfileEditRequest){
        return ResponseEntity.ok(ApiResponse.success(iUserService.editUserSocialProfile(id, socialProfileEditRequest)));
    }

    @Operation(summary = "Chỉnh sửa thông tình tài khoản")
    @PutMapping("/{id}/account")
    public ResponseEntity<ApiResponse<Long>> editAccountProfile(@PathVariable Long id, @RequestBody AccountProfileEditRequest accountProfileEditRequest){
        return ResponseEntity.ok(ApiResponse.success(iUserService.editUserAccountProfile(id, accountProfileEditRequest)));
    }

    @Operation(summary = "Chỉnh sửa thông tin cá nhân")
    @PutMapping("/{id}/personal")
    public ResponseEntity<ApiResponse<Long>> editPersonalProfile(@PathVariable Long id, @RequestBody PersonalProfileEditRequest personalProfileEditRequest){
        return ResponseEntity.ok(ApiResponse.success(iUserService.editUserPersonalProfile(id, personalProfileEditRequest)));
    }

//    @PostMapping("/getData")
//    public ResponseEntity<String> testAfterLogin(Principal p) {
//        return ResponseEntity.ok("You are accessing data after a valid Login. You are :" + p.getName());
//    }


    @Operation(summary = "upload dại diện - CMMD.CDCC ")
    @PostMapping("/{id}/image")
    public ResponseEntity<Long> uploadImageRegisterProfile(@PathVariable Long id, @ModelAttribute UploadImageRequest uploadImageRequest) throws IOException {
        return ResponseEntity.ok(iUserService.uploadImageProfile(id, uploadImageRequest));
    }

    @Operation(summary = "Member / Mentor register account")
    @PostMapping("/register")
    public ResponseEntity<Long> registerAccount(@RequestBody CreateAccountRequest createAccountRequest) {
        return ResponseEntity.ok(iUserService.registerAccount(createAccountRequest));
    }
}
