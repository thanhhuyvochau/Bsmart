package fpt.project.bsmart.controller;


import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.request.AccountEditRequest;
import fpt.project.bsmart.entity.request.StudentRequest;
import fpt.project.bsmart.entity.response.AccountResponse;
import fpt.project.bsmart.entity.response.StudentResponse;
import fpt.project.bsmart.service.IAccountService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/students")
public class StudentController {

    private final IAccountService accountService;

    public StudentController(IAccountService accountService) {
        this.accountService = accountService;
    }


    @Operation(summary = "Cập nhật hồ sơ học sinh")
    @PutMapping("/{id}/account/profile")
    public ResponseEntity<AccountResponse> editProfile(@PathVariable long id, @RequestBody AccountEditRequest accountEditRequest) {
        return ResponseEntity.ok(accountService.editStudentProfile(id, accountEditRequest));
    }
    @PostMapping("/account")
    @Operation(summary = "Học sinh đăng ký tài khoản")
    public ResponseEntity<ApiResponse<StudentResponse>> studentCreateAccount(@RequestBody StudentRequest studentRequest) {
        return ResponseEntity.ok(ApiResponse.success(accountService.createStudentAccount(studentRequest)));
    }

}
