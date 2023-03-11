package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.CategoryDto;
import fpt.project.bsmart.entity.request.category.CategoryRequest;
import fpt.project.bsmart.service.IBankService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/banks")
public class BankController {
    private final IBankService bankService;

    public BankController(IBankService bankService) {
        this.bankService = bankService;
    }


//    @Operation(summary = "Lấy tất cả category")
//    @GetMapping
//    public ResponseEntity<ApiResponse<List<CategoryDto>>> getAllCategories() {
//        return ResponseEntity.ok(ApiResponse.success(bankService.getAllCategories()));
//    }
    @Operation(summary = "Đồng bộ các ngân hàng")
    @GetMapping("/synchronize")
    public ResponseEntity<ApiResponse<Boolean>> synchronizeBanks() {
        return ResponseEntity.ok(ApiResponse.success(bankService.synchronizeBanks()));
    }

}
