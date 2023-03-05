package fpt.project.bsmart.service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import fpt.project.bsmart.entity.Slot;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.util.ConvertUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import fpt.project.bsmart.entity.dto.SlotDto;
import fpt.project.bsmart.repository.SlotRepository;
import fpt.project.bsmart.service.SlotService;

import javax.transaction.Transactional;

@Service
@Transactional
public class SlotServiceImpl implements SlotService {


    private final SlotRepository slotRepository;

    public SlotServiceImpl(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    @Override
    public SlotDto createSlot(SlotDto slot) {
        Slot entity = ConvertUtil.convertSlotDtoToSlot(slot);
        entity = slotRepository.save(entity);
        return ConvertUtil.convertSlotToSlotDto(entity);
    }

    @Override
    public SlotDto getSlotById(Long id) {
        Slot slot = slotRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Slot not found with id " + id));
        return ConvertUtil.convertSlotToSlotDto(slot);
    }

    @Override
    public SlotDto updateSlot(SlotDto slotDto) {
        Slot slot = slotRepository.findById(slotDto.getId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Slot not found with id " + slotDto.getId()));
        slot.setName(slotDto.getName());
        slot.setCode(slotDto.getCode());
        slot.setStartTime(slotDto.getStartTime());
        slot.setEndTime(slotDto.getEndTime());
        slot = slotRepository.save(slot);
        return ConvertUtil.convertSlotToSlotDto(slot);
    }

    @Override
    public Boolean deleteSlot(Long id) {
        Slot slot = slotRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Slot not found with id " + id));
        slotRepository.delete(slot);
        return true;
    }

    @Override
    public List<SlotDto> getAllSlots() {
        List<Slot> entities = slotRepository.findAll();
        return entities.stream().map(ConvertUtil::convertSlotToSlotDto).collect(Collectors.toList());
    }


}
