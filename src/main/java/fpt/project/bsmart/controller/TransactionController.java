package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.TransactionDto;
import fpt.project.bsmart.entity.request.PayCourseRequest;
import fpt.project.bsmart.entity.request.TransactionRequest;
import fpt.project.bsmart.entity.request.VpnPayRequest;
import fpt.project.bsmart.entity.request.WithdrawRequest;
import fpt.project.bsmart.entity.response.RevenueResponse;
import fpt.project.bsmart.entity.response.VnPayResponse;
import fpt.project.bsmart.entity.request.PayCartRequest;
import fpt.project.bsmart.entity.request.PayRequest;
import fpt.project.bsmart.entity.request.ProcessWithdrawRequest;
import fpt.project.bsmart.entity.request.WithdrawRequest;
import fpt.project.bsmart.entity.response.WithDrawResponse;
import fpt.project.bsmart.payment.PaymentResponse;
import fpt.project.bsmart.service.ITransactionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Transactional(rollbackFor = {Exception.class})
public class TransactionController {
    private final ITransactionService iTransactionService;

    public TransactionController(ITransactionService iTransactionService) {
        this.iTransactionService = iTransactionService;
    }

    @Operation(summary = "Thành viên tự lấy thông tin giao dịch về ví tiền")
    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT','TEACHER')")
    public ResponseEntity<ApiResponse<ApiPage<TransactionDto>>> getSelfTransaction(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iTransactionService.getSelfTransactions(pageable)));
    }

    @Operation(summary = "Admin lấy thông tin giao dịch về ví tiền của thành viên")
    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<ApiResponse<ApiPage<TransactionDto>>> getSelfTransaction(Pageable pageable, @PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(iTransactionService.getUserTransactions(pageable, userId)));
    }
//
//    @Operation(summary = "Thành viên nạp tiền vào vi")
//    @PostMapping("/deposit")
//    @PreAuthorize("hasAnyRole('STUDENT')")
//    public ResponseEntity<ApiResponse<Boolean>> deposit(@Valid @RequestBody DepositRequest request) {
//        return ResponseEntity.ok(ApiResponse.success(iTransactionService.deposit(request)));
//    }

    @Operation(summary = "Thành viên rút tiền từ vi")
    @PostMapping("/withdraw")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ApiResponse<Boolean>> withdraw(@Valid @RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(ApiResponse.success(iTransactionService.withdraw(request)));
    }

    @Operation(summary = "Quản lý lất yêu cầu rút tiền")
    @GetMapping("/withdraw/requests")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<List<WithDrawResponse>>> managerGetWithdrawRequest(){
        return ResponseEntity.ok(ApiResponse.success(iTransactionService.managerGetWithDrawRequest()));
    }

    @Operation(summary = "Quản lý cập nhật thông tin chuyển tiền")
    @PutMapping("withdraw/requests")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> managerProcessWithdrawRequest(@RequestBody List<ProcessWithdrawRequest> requests){
        return ResponseEntity.ok(ApiResponse.success(iTransactionService.managerProcessWithdrawRequest(requests)));
    }

    @Operation(summary = "Thanh toán khóa học từ giỏ hàng")
    @PostMapping("/pay")
    @PreAuthorize("hasAnyRole('STUDENT')")
    public ResponseEntity<ApiResponse<PaymentResponse>> payCourseFromCart(@RequestBody PayCartRequest request) throws UnsupportedEncodingException {
        return ResponseEntity.ok(ApiResponse.success(iTransactionService.payCourseFromCart(request)));
    }

    @Operation(summary = "Thanh toán khóa học nhanh")
    @PostMapping("/pay-quick")
    @PreAuthorize("hasAnyRole('STUDENT')")
    public ResponseEntity<ApiResponse<PaymentResponse>> payQuickCourse(@RequestBody PayRequest payRequest) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(iTransactionService.payQuickCourse(payRequest)));
    }

    @Operation(summary = "Thanh toán khóa học từ giỏ hàng")
    @GetMapping("/pay/vnpay/result")
    public ResponseEntity<ApiResponse<Boolean>> getResultOfPayByVnPay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return ResponseEntity.ok(ApiResponse.success(iTransactionService.executeAfterVnPayReturn(request)));
    }

    @Operation(summary = "Lấy thông tinh doanh thu cho trang admin")
    @GetMapping("/revenue")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<RevenueResponse>>> getRevenueForAdminPage(@Nullable TransactionRequest request){
        return ResponseEntity.ok(ApiResponse.success(iTransactionService.getRevenueForAdminPage(request)));
    }
}
