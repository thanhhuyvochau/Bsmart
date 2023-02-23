package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.SlotDto;
import fpt.project.bsmart.service.ISlotService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/slot")
public class SlotController {

    private final ISlotService iSlotService ;

    public SlotController(ISlotService iSlotService) {
        this.iSlotService = iSlotService;
    }

    @Operation(summary = "Lấy tất cảs lot")
    @GetMapping
    public ResponseEntity<ApiResponse<List<SlotDto>>> getAllSlot() {
        return ResponseEntity.ok(ApiResponse.success(iSlotService.getAllSlot()));
    }



}
