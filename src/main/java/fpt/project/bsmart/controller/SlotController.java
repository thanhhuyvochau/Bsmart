package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.SlotDto;
import fpt.project.bsmart.service.SlotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/slots")
public class SlotController {

    private final SlotService slotService;

    public SlotController(SlotService slotService) {
        this.slotService = slotService;
    }
    @PostMapping
    public ResponseEntity<ApiResponse<SlotDto>> createSlot(@Valid @RequestBody SlotDto slotDto) {
        SlotDto createdSlotDto = slotService.createSlot(slotDto);
        return ResponseEntity.ok(ApiResponse.success(createdSlotDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SlotDto>> getSlotById(@Valid @PathVariable("id") Long id) {
        SlotDto slotDto = slotService.getSlotById(id);
        return ResponseEntity.ok(ApiResponse.success(slotDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SlotDto>> updateSlot(@PathVariable("id") Long id, @Valid @RequestBody SlotDto slotDto) {
        if (!id.equals(slotDto.getId())) {
            return ResponseEntity.badRequest().build();
        }
        SlotDto updatedSlotDto = slotService.updateSlot(slotDto);
        return ResponseEntity.ok(ApiResponse.success(updatedSlotDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteSlot(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ApiResponse.success(slotService.deleteSlot(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SlotDto>>> getSlots() {
        return ResponseEntity.ok(ApiResponse.success(slotService.getAllSlots()));
    }
}
