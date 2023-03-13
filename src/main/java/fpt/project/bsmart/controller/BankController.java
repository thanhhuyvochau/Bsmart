package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.BankDto;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.service.IBankService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/banks")
public class BankController {
    private final IBankService bankService;

    public BankController(IBankService bankService) {
        this.bankService = bankService;
    }


    @Operation(summary = "Lấy tất cả các ngân hàng")
    @GetMapping
    public ResponseEntity<ApiResponse<List<BankDto>>> getBanks() {
        return ResponseEntity.ok(ApiResponse.success(bankService.getBanks()));
    }

    @Operation(summary = "Đồng bộ các ngân hàng")
    @GetMapping("/synchronize")
    public ResponseEntity<ApiResponse<Boolean>> synchronizeBanks() {
        return ResponseEntity.ok(ApiResponse.success(bankService.synchronizeBanks()));
    }

}
