package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.dto.SlotDto;

import java.util.List;

public interface SlotService {
    SlotDto createSlot(SlotDto slot);

    SlotDto getSlotById(Long id);

    SlotDto updateSlot(SlotDto slot);

    Boolean deleteSlot(Long id);

    List<SlotDto> getAllSlots();
}
