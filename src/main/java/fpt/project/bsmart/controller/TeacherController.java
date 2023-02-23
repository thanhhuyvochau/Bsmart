package fpt.project.bsmart.controller;



import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.request.AccountEditRequest;
import fpt.project.bsmart.entity.request.AccountExistedTeacherRequest;
import fpt.project.bsmart.entity.response.AccountDetailResponse;
import fpt.project.bsmart.entity.response.AccountResponse;
import fpt.project.bsmart.entity.response.AccountTeacherResponse;
import fpt.project.bsmart.service.IAccountService;
import fpt.project.bsmart.service.ITeacherService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("api/teachers")
public class TeacherController {
    private final ITeacherService iTeacherService;
    private final IAccountService accountService;

    public TeacherController(ITeacherService iTeacherService, IAccountService accountService) {
        this.iTeacherService = iTeacherService;
        this.accountService = accountService;
    }

    @Operation(summary = "Cập nhật hồ sơ giáo viên")
    @PutMapping("/{id}/account/profile")
    public ResponseEntity<AccountResponse> editProfile(@PathVariable long id, @RequestBody AccountEditRequest accountEditRequest) {
        return ResponseEntity.ok(accountService.editTeacherProfile(id, accountEditRequest));
    }
    @Operation(summary = "Tạo tài khoản cho giáo viên")
    @PostMapping("/account")
    public ResponseEntity<ApiResponse<AccountTeacherResponse>> createTeacherAccount(@RequestBody AccountExistedTeacherRequest accountRequest) {
        return ResponseEntity.ok(ApiResponse.success(accountService.createTeacherAccount(accountRequest)));
    }

    @Operation(summary = "Xem thông tin giáo viên tiêu biểu - đã có account ")
    @GetMapping("")
    public ResponseEntity<ApiPage<AccountDetailResponse>> getAllInfoTeacher(Pageable pageable) {
        return ResponseEntity.ok(accountService.getAllInfoTeacher(pageable));
    }


    @Operation(summary = "Lấy danh sách tất cả các giáo viên")
    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('MANAGER','ROOT')")
    public ResponseEntity<ApiPage<AccountDetailResponse>> getAllTeacher(Pageable pageable) {
        return ResponseEntity.ok(iTeacherService.getAllTeacher(pageable));
    }


}
