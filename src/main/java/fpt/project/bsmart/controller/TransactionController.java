package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.TransactionDto;
import fpt.project.bsmart.entity.request.DepositRequest;
import fpt.project.bsmart.entity.request.PayCourseRequest;
import fpt.project.bsmart.entity.request.WithdrawRequest;
import fpt.project.bsmart.service.ITransactionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final ITransactionService iTransactionService;

    public TransactionController(ITransactionService iTransactionService) {
        this.iTransactionService = iTransactionService;
    }

    @Operation(summary = "Thành viên tự lấy thông tin giao dịch về ví tiền")
    @GetMapping
    public ResponseEntity<ApiResponse<ApiPage<TransactionDto>>> getSelfTransaction(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(iTransactionService.getSelfTransactions(pageable)));
    }

    @Operation(summary = "Admin lấy thông tin giao dịch về ví tiền của thành viên")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<ApiPage<TransactionDto>>> getSelfTransaction(Pageable pageable, @PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(iTransactionService.getUserTransactions(pageable, userId)));
    }

    @Operation(summary = "Thành viên nạp tiền vào vi")
    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<Boolean>> deposit(@Valid @RequestBody DepositRequest request) {
        return ResponseEntity.ok(ApiResponse.success(iTransactionService.deposit(request)));
    }

    @Operation(summary = "Thành viên rút tiền từ vi")
    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<Boolean>> withdraw(@Valid @RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(ApiResponse.success(iTransactionService.withdraw(request)));
    }

    @Operation(summary = "Thanh toán khóa học nhanh")
    @PostMapping("/pay-quick")
    public ResponseEntity<ApiResponse<Boolean>> payCourseQuickly(@RequestBody PayCourseRequest request) {
        return ResponseEntity.ok(ApiResponse.success(iTransactionService.payQuickCourse(request)));
    }

    @Operation(summary = "Thanh toán khóa học từ giỏ hàng")
    @PostMapping("/pay")
    public ResponseEntity<ApiResponse<Boolean>> payCourseFromCart(@RequestBody PayCourseRequest request) {
        return ResponseEntity.ok(ApiResponse.success(iTransactionService.payCourseFromCart(request)));
    }

}
