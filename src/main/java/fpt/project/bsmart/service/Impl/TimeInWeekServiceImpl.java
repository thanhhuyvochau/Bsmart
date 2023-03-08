package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.DayOfWeek;
import fpt.project.bsmart.entity.Slot;
import fpt.project.bsmart.entity.TimeInWeek;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.dto.TimeInWeekDTO;
import fpt.project.bsmart.entity.request.TimeInWeekRequest;
import fpt.project.bsmart.repository.ClassRepository;
import fpt.project.bsmart.repository.DayOfWeekRepository;
import fpt.project.bsmart.repository.SlotRepository;
import fpt.project.bsmart.repository.TimeInWeekRepository;
import fpt.project.bsmart.service.ITimeInWeekService;
import fpt.project.bsmart.util.ConvertUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TimeInWeekServiceImpl implements ITimeInWeekService {

    private final TimeInWeekRepository timeInWeekRepository;
    private final DayOfWeekRepository dayOfWeekRepository;
    private final SlotRepository slotRepository;
    private final ClassRepository classRepository;

    public TimeInWeekServiceImpl(TimeInWeekRepository timeInWeekRepository, DayOfWeekRepository dayOfWeekRepository, SlotRepository slotRepository, ClassRepository classRepository) {
        this.timeInWeekRepository = timeInWeekRepository;
        this.dayOfWeekRepository = dayOfWeekRepository;
        this.slotRepository = slotRepository;
        this.classRepository = classRepository;
    }

    @Override
    public List<TimeInWeekDTO> getAllTimeInWeeks(Long clazzId) {
        Class clazz = classRepository.findById(clazzId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Lớp không tìm thấy với id:" + clazzId));
        List<TimeInWeek> timeInWeekList = clazz.getTimeInWeeks();
        return timeInWeekList.stream()
                .map(ConvertUtil::convertTimeInWeekToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TimeInWeekDTO createTimeInWeek(TimeInWeekRequest request) {
        Class clazz = classRepository.findById(request.getClazzId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Lớp không tìm thấy với id:" + request.getClazzId()));
        Slot slot = slotRepository.findById(request.getSlotId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Slot không tìm thấy với id:" + request.getSlotId()));
        DayOfWeek dayOfWeek = dayOfWeekRepository.findById(request.getDayOfWeekId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Ngày trong tuần không tìm thấy với id:" + request.getDayOfWeekId()));

        TimeInWeek timeInWeek = new TimeInWeek();
        timeInWeek.setDayOfWeek(dayOfWeek);
        timeInWeek.setSlot(slot);
        timeInWeek.setClazz(clazz);

        timeInWeekRepository.save(timeInWeek);
        return ConvertUtil.convertTimeInWeekToDto(timeInWeek);
    }

    @Override
    public TimeInWeekDTO updateTimeInWeek(Long id, TimeInWeekRequest request) {
        Optional<TimeInWeek> optionalTimeInWeek = timeInWeekRepository.findById(id);
        if (optionalTimeInWeek.isPresent()) {
            TimeInWeek timeInWeek = optionalTimeInWeek.get();
            Class clazz = classRepository.findById(request.getClazzId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Lớp không tìm thấy với id:" + request.getClazzId()));
            Slot slot = slotRepository.findById(request.getSlotId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Slot không tìm thấy với id:" + request.getSlotId()));
            DayOfWeek dayOfWeek = dayOfWeekRepository.findById(request.getDayOfWeekId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Ngày trong tuần không tìm thấy với id:" + request.getDayOfWeekId()));

            timeInWeek.setDayOfWeek(dayOfWeek);
            timeInWeek.setSlot(slot);
            timeInWeek.setClazz(clazz);
            return ConvertUtil.convertTimeInWeekToDto(timeInWeek);
        } else {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Ngày trong tuần không tìm thấy với id:" + id);
        }
    }

    @Override
    public boolean deleteTimeInWeek(Long id) {
        Optional<TimeInWeek> optionalTimeInWeek = timeInWeekRepository.findById(id);
        if (optionalTimeInWeek.isPresent()) {
            timeInWeekRepository.delete(optionalTimeInWeek.get());
            return true;
        } else {
            return false;
        }
    }
}
