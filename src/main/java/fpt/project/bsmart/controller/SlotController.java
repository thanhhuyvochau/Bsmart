package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.dto.SlotDto;
import fpt.project.bsmart.service.ISlotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/slots")
public class SlotController {

    private final ISlotService ISlotService;

    public SlotController(ISlotService ISlotService) {
        this.ISlotService = ISlotService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SlotDto>> createSlot(@Valid @RequestBody SlotDto slotDto) {
        SlotDto createdSlotDto = ISlotService.createSlot(slotDto);
        return ResponseEntity.ok(ApiResponse.success(createdSlotDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SlotDto>> getSlotById(@Valid @PathVariable("id") Long id) {
        SlotDto slotDto = ISlotService.getSlotById(id);
        return ResponseEntity.ok(ApiResponse.success(slotDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SlotDto>> updateSlot(@PathVariable("id") Long id, @Valid @RequestBody SlotDto slotDto) {
        if (!id.equals(slotDto.getId())) {
            return ResponseEntity.badRequest().build();
        }
        SlotDto updatedSlotDto = ISlotService.updateSlot(slotDto);
        return ResponseEntity.ok(ApiResponse.success(updatedSlotDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteSlot(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ApiResponse.success(ISlotService.deleteSlot(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SlotDto>>> getSlots() {
        return ResponseEntity.ok(ApiResponse.success(ISlotService.getAllSlots()));
    }
}
