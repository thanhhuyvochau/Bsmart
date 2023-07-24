package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.DayOfWeek;
import fpt.project.bsmart.entity.Slot;
import fpt.project.bsmart.entity.TimeInWeek;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.request.SubCourseTimeRequest;
import fpt.project.bsmart.entity.request.TimeInWeekRequest;
import fpt.project.bsmart.repository.ClassRepository;
import fpt.project.bsmart.repository.DayOfWeekRepository;
import fpt.project.bsmart.repository.SlotRepository;
import fpt.project.bsmart.repository.TimeInWeekRepository;
import fpt.project.bsmart.service.ITimeInWeekService;
import fpt.project.bsmart.util.MessageUtil;
import fpt.project.bsmart.util.ObjectUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.*;

@Service
@Transactional
public class TimeInWeekServiceImpl implements ITimeInWeekService {

    private final TimeInWeekRepository timeInWeekRepository;
    private final DayOfWeekRepository dayOfWeekRepository;
    private final SlotRepository slotRepository;
    private final ClassRepository classRepository;
    private final MessageUtil messageUtil;



    public TimeInWeekServiceImpl(TimeInWeekRepository timeInWeekRepository, DayOfWeekRepository dayOfWeekRepository, SlotRepository slotRepository, ClassRepository classRepository, MessageUtil messageUtil) {
        this.timeInWeekRepository = timeInWeekRepository;
        this.dayOfWeekRepository = dayOfWeekRepository;
        this.slotRepository = slotRepository;
        this.classRepository = classRepository;
        this.messageUtil = messageUtil;

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
//        SubCourse subCourse = subCourseRepository.findById(request.getSubCourseId())
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(SUB_COURSE_NOT_FOUND_BY_ID) + request.getSubCourseId()));
//        if (!subCourse.getTimeInWeeks().isEmpty()) {
//            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(SUB_COURSE_CAN_NOT_CREATE_TIME_IN_WEEK));
//        }
        List<TimeInWeekRequest> timeInWeekRequests = request.getTimeInWeekRequests();
        TimeInWeekRequest duplicateElement = ObjectUtil.isHasDuplicate(timeInWeekRequests);
        if (duplicateElement == null) {
            List<Long> slotIds = timeInWeekRequests.stream().map(TimeInWeekRequest::getSlotId).collect(Collectors.toList());
            List<Long> dowIds = timeInWeekRequests.stream().map(TimeInWeekRequest::getDayOfWeekId).collect(Collectors.toList());
            Map<Long, Slot> slotMap = slotRepository.findAllById(slotIds).stream().collect(Collectors.toMap(Slot::getId, Function.identity()));
            Map<Long, DayOfWeek> dayOfWeekMap = dayOfWeekRepository.findAllById(dowIds).stream().collect(Collectors.toMap(DayOfWeek::getId, Function.identity()));
            for (TimeInWeekRequest timeInWeekRequest : timeInWeekRequests) {
                TimeInWeek timeInWeek = new TimeInWeek();
                DayOfWeek dayOfWeek = Optional.ofNullable(dayOfWeekMap.get(timeInWeekRequest.getDayOfWeekId()))
                        .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(TIME_OF_WEEK_NOT_FOUND_BY_ID) + timeInWeekRequest.getDayOfWeekId()));
                timeInWeek.setDayOfWeek(dayOfWeek);

                Slot slot = Optional.ofNullable(slotMap.get(timeInWeekRequest.getSlotId()))
                        .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(SLOT_NOT_FOUND_BY_ID) + timeInWeekRequest.getSlotId()));
                timeInWeek.setSlot(slot);

//                timeInWeek.setSubCourse(subCourse);
//                subCourse.addTimeInWeek(timeInWeek);
            }
            return true;
        } else {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(DUPLICATE_SLOT_AND_TIME_IN_WEEK));
        }
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
