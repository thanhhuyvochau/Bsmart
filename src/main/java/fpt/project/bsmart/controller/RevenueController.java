package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.request.RevenueSearchRequest;
import fpt.project.bsmart.entity.response.RevenueClassResponse;
import fpt.project.bsmart.entity.response.SalaryEstimatesResponse;
import fpt.project.bsmart.service.IRevenueService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/revenue")
public class RevenueController {


    private final IRevenueService iRevenueService;

    public RevenueController(IRevenueService iRevenueService) {
        this.iRevenueService = iRevenueService;
    }


    @Operation(summary = "ước tính tiền lương cho giáo viên")
    @GetMapping("/estimates-salary-for-teacher")
    public ResponseEntity<ApiResponse<List<SalaryEstimatesResponse>>> salaryEstimatesTeachers(BigDecimal priceEachStudent, Long numberStudent, Long numberMonth) {
        return ResponseEntity.ok(ApiResponse.success(iRevenueService.salaryEstimatesTeachers(priceEachStudent, numberStudent, numberMonth)));
    }

    @Operation(summary = "Xem doanh thu theo lớp")
    @GetMapping("/{classId}")
    public ResponseEntity<ApiResponse<BigDecimal>> RevenueClass(@PathVariable Long classId) {
        return ResponseEntity.ok(ApiResponse.success(iRevenueService.RevenueClass(classId)));
    }


    @Operation(summary = "Xem doanh thu theo tất cả lơp")
    @GetMapping("/classes")
    public ResponseEntity<ApiResponse<List<RevenueClassResponse>>> RevenueAllClass() {
        return ResponseEntity.ok(ApiResponse.success(iRevenueService.RevenueAllClass()));
    }

    @Operation(summary = "admin xem doanh thu ")
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<RevenueClassResponse>>> searchRevenue(@Nullable RevenueSearchRequest query) {
        return ResponseEntity.ok(ApiResponse.success(iRevenueService.searchRevenue(query)));
    }


    @Operation(summary = "Học sinh / giao vien xem số tiền học phí đã đóng / đã nhận")
    @GetMapping("/student/search-tuition-fee")
    public ResponseEntity<ApiResponse<List<RevenueClassResponse>>> studentGetTuitionFee(@Nullable RevenueSearchRequest query) {
        return ResponseEntity.ok(ApiResponse.success(iRevenueService.studentGetTuitionFee(query)));
    }
}
