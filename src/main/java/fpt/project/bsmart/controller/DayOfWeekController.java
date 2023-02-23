package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.DayOfWeekDto;
import fpt.project.bsmart.service.IDayOfWeekService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/dayofweek")
public class DayOfWeekController {

    private final IDayOfWeekService iDayOfWeekService ;

    public DayOfWeekController(IDayOfWeekService iDayOfWeekService) {
        this.iDayOfWeekService = iDayOfWeekService;
    }

    @Operation(summary = "Lấy tất cả ngày trong tuần")
    @GetMapping
    public ResponseEntity<ApiResponse<List<DayOfWeekDto>>> getAllDayOfWeek() {
        return ResponseEntity.ok(ApiResponse.success(iDayOfWeekService.getAllDayOfWeek()));
    }



}
