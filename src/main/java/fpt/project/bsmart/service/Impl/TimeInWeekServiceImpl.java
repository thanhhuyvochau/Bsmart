package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.DayOfWeek;
import fpt.project.bsmart.entity.Slot;
import fpt.project.bsmart.entity.SubCourse;
import fpt.project.bsmart.entity.TimeInWeek;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.request.SubCourseTimeRequest;
import fpt.project.bsmart.entity.request.TimeInWeekRequest;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.ITimeInWeekService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class TimeInWeekServiceImpl implements ITimeInWeekService {

    private final TimeInWeekRepository timeInWeekRepository;
    private final DayOfWeekRepository dayOfWeekRepository;
    private final SlotRepository slotRepository;
    private final ClassRepository classRepository;

    private final SubCourseRepository subCourseRepository;

    public TimeInWeekServiceImpl(TimeInWeekRepository timeInWeekRepository, DayOfWeekRepository dayOfWeekRepository, SlotRepository slotRepository, ClassRepository classRepository, SubCourseRepository subCourseRepository) {
        this.timeInWeekRepository = timeInWeekRepository;
        this.dayOfWeekRepository = dayOfWeekRepository;
        this.slotRepository = slotRepository;
        this.classRepository = classRepository;
        this.subCourseRepository = subCourseRepository;
    }

//    @Override
//    public List<TimeInWeekDTO> getAllTimeInWeeks(Long clazzId) {
//        Class clazz = classRepository.findById(clazzId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Lớp không tìm thấy với id:" + clazzId));
//        List<TimeInWeek> timeInWeekList = clazz.getTimeInWeeks();
//        return timeInWeekList.stream()
//                .map(ConvertUtil::convertTimeInWeekToDto)
//                .collect(Collectors.toList());
//    }

    @Override
    public Boolean createTimeInWeek(SubCourseTimeRequest request) {
        List<Long> slotIds = request.getTimeInWeekRequests().stream().map(TimeInWeekRequest::getSlotId).collect(Collectors.toList());
        List<Long> dowIds = request.getTimeInWeekRequests().stream().map(TimeInWeekRequest::getDayOfWeekId).collect(Collectors.toList());

        Map<Long, Slot> slotMap = slotRepository.findAllById(slotIds).stream().collect(Collectors.toMap(Slot::getId, Function.identity()));
        Map<Long, DayOfWeek> dayOfWeekMap = dayOfWeekRepository.findAllById(dowIds).stream().collect(Collectors.toMap(DayOfWeek::getId, Function.identity()));

        SubCourse subCourse = subCourseRepository.findById(request.getSubCourseId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm khóa học cần tạo lịch!"));
        List<TimeInWeek> timeInWeeks = new ArrayList<>();
        for (TimeInWeekRequest timeInWeekRequest : request.getTimeInWeekRequests()) {
            TimeInWeek timeInWeek = new TimeInWeek();
            DayOfWeek dayOfWeek = Optional.ofNullable(dayOfWeekMap.get(timeInWeekRequest.getDayOfWeekId()))
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy ngày trong tuần đã chọn vui lòng thử lại!"));
            timeInWeek.setDayOfWeek(dayOfWeek);

            Slot slot = Optional.ofNullable(slotMap.get(timeInWeekRequest.getSlotId()))
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy slot đã chọn vui lòng thử lại!"));
            timeInWeek.setSlot(slot);

            timeInWeek.setSubCourse(subCourse);
            subCourse.addTimeInWeek(timeInWeek);
        }
        return true;
    }

//    @Override
//    public TimeInWeekDTO updateTimeInWeek(Long id, TimeInWeekRequest request) {
//        Optional<TimeInWeek> optionalTimeInWeek = timeInWeekRepository.findById(id);
//        if (optionalTimeInWeek.isPresent()) {
//            TimeInWeek timeInWeek = optionalTimeInWeek.get();
//            Class clazz = classRepository.findById(request.getClazzId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Lớp không tìm thấy với id:" + request.getClazzId()));
//            Slot slot = slotRepository.findById(request.getSlotId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Slot không tìm thấy với id:" + request.getSlotId()));
//            DayOfWeek dayOfWeek = dayOfWeekRepository.findById(request.getDayOfWeekId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Ngày trong tuần không tìm thấy với id:" + request.getDayOfWeekId()));
//
//            timeInWeek.setDayOfWeek(dayOfWeek);
//            timeInWeek.setSlot(slot);
//            timeInWeek.setClazz(clazz);
//            return ConvertUtil.convertTimeInWeekToDto(timeInWeek);
//        } else {
//            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Ngày trong tuần không tìm thấy với id:" + id);
//        }
//    }
//
//    @Override
//    public boolean deleteTimeInWeek(Long id) {
//        Optional<TimeInWeek> optionalTimeInWeek = timeInWeekRepository.findById(id);
//        if (optionalTimeInWeek.isPresent()) {
//            timeInWeekRepository.delete(optionalTimeInWeek.get());
//            return true;
//        } else {
//            return false;
//        }
//    }
}
