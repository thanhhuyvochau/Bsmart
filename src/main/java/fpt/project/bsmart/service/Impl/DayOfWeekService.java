package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.DayOfWeek;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.dto.DayOfWeekDTO;
import fpt.project.bsmart.repository.DayOfWeekRepository;
import fpt.project.bsmart.service.IDayOfWeekService;
import fpt.project.bsmart.util.ConvertUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DayOfWeekService implements IDayOfWeekService {


    private final DayOfWeekRepository dayOfWeekRepository;

    public DayOfWeekService(DayOfWeekRepository dayOfWeekRepository) {
        this.dayOfWeekRepository = dayOfWeekRepository;
    }

    public List<DayOfWeekDTO> getAllDaysOfWeek() {
        List<DayOfWeek> daysOfWeek = dayOfWeekRepository.findAll();
        return daysOfWeek.stream()
                .map(ConvertUtil::convertDayOfWeekToDto)
                .collect(Collectors.toList());
    }

    public DayOfWeekDTO getDayOfWeekById(Long id) {
        DayOfWeek dayOfWeek = dayOfWeekRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Day of week not found with id:" + id));
        return ConvertUtil.convertDayOfWeekToDto(dayOfWeek);
    }

    @Override
    public DayOfWeekDTO createDayOfWeek(DayOfWeekDTO request) {
        DayOfWeek dayOfWeek = new DayOfWeek();
        dayOfWeek.setName(request.getName());
        dayOfWeek.setCode(request.getCode());
        DayOfWeek savedDayOfWeek = dayOfWeekRepository.save(dayOfWeek);
        return ConvertUtil.convertDayOfWeekToDto(savedDayOfWeek);
    }

    public DayOfWeekDTO updateDayOfWeek(Long id, DayOfWeekDTO request) {
        Optional<DayOfWeek> optionalDayOfWeek = dayOfWeekRepository.findById(id);
        if (optionalDayOfWeek.isPresent()) {
            DayOfWeek dayOfWeek = optionalDayOfWeek.get();
            dayOfWeek.setName(request.getName());
            dayOfWeek.setCode(request.getCode());
            DayOfWeek updatedDayOfWeek = dayOfWeekRepository.save(dayOfWeek);
            return ConvertUtil.convertDayOfWeekToDto(updatedDayOfWeek);
        } else {
            return null; // or throw an exception
        }
    }

    public void deleteDayOfWeek(Long id) {
        dayOfWeekRepository.deleteById(id);
    }
}
