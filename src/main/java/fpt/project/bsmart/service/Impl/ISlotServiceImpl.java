package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Slot;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.dto.SlotDto;
import fpt.project.bsmart.repository.SlotRepository;
import fpt.project.bsmart.service.ISlotService;
import fpt.project.bsmart.util.ConvertUtil;
import fpt.project.bsmart.util.MessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.SLOT_NOT_FOUND_BY_ID;

@Service
@Transactional
public class ISlotServiceImpl implements ISlotService {

    private final MessageUtil messageUtil;
    private final SlotRepository slotRepository;

    public ISlotServiceImpl(MessageUtil messageUtil, SlotRepository slotRepository) {
        this.messageUtil = messageUtil;
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
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(SLOT_NOT_FOUND_BY_ID) + id));
        return ConvertUtil.convertSlotToSlotDto(slot);
    }

    @Override
    public SlotDto updateSlot(SlotDto slotDto) {
        Slot slot = slotRepository.findById(slotDto.getId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(SLOT_NOT_FOUND_BY_ID) + slotDto.getId()));
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
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(SLOT_NOT_FOUND_BY_ID) + id));
        slotRepository.delete(slot);
        return true;
    }

    @Override
    public List<SlotDto> getAllSlots() {
        List<Slot> entities = slotRepository.findAll();
        return entities.stream().map(ConvertUtil::convertSlotToSlotDto).collect(Collectors.toList());
    }


}
