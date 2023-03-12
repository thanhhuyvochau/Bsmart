package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.TransactionDto;
import fpt.project.bsmart.entity.request.WithdrawRequest;
import fpt.project.bsmart.service.ITransactionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse<Boolean>> deposit(@RequestBody BigDecimal amount) {
        return ResponseEntity.ok(ApiResponse.success(iTransactionService.deposit(amount)));
    }

    @Operation(summary = "Thành viên rút tiền từ vi")
    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<Boolean>> withdraw(@RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(ApiResponse.success(iTransactionService.withdraw(request)));
    }

}
