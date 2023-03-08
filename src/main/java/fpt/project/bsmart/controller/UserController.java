package fpt.project.bsmart.controller;


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

//    @PostMapping("/getData")
//    public ResponseEntity<String> testAfterLogin(Principal p) {
//        return ResponseEntity.ok("You are accessing data after a valid Login. You are :" + p.getName());
//    }


    @Operation(summary = "upload dại diện - CMMD.CDCC ")
    @PostMapping("/{id}/image")
    public ResponseEntity<Long> uploadImageRegisterProfile(@PathVariable Long id, @ModelAttribute UploadImageRequest uploadImageRequest) throws IOException {
        return ResponseEntity.ok(iUserService.uploadImageProfile(id, uploadImageRequest));
    }
}
