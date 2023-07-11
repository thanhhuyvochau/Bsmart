package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.DayOfWeek;
import fpt.project.bsmart.entity.Slot;
import fpt.project.bsmart.entity.TimeTable;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EDayOfWeekCode;
import fpt.project.bsmart.entity.dto.SlotDto;
import fpt.project.bsmart.entity.request.TimeInWeekRequest;
import fpt.project.bsmart.entity.request.timetable.GenerateScheduleRequest;
import fpt.project.bsmart.entity.request.timetable.GenerateScheduleResponse;
import fpt.project.bsmart.repository.ClassRepository;
import fpt.project.bsmart.repository.DayOfWeekRepository;
import fpt.project.bsmart.repository.SlotRepository;
import fpt.project.bsmart.repository.TimeTableRepository;
import fpt.project.bsmart.service.ITimeTableService;
import fpt.project.bsmart.util.ConvertUtil;
import fpt.project.bsmart.util.MessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fpt.project.bsmart.util.Constants.ErrorMessage.*;

@Service
@Transactional
public class TimeTableServiceImpl implements ITimeTableService {
    private final TimeTableRepository timeTableRepository;
    private final ClassRepository classRepository;
    private final MessageUtil messageUtil;

    private final DayOfWeekRepository dayOfWeekRepository;

    private final SlotRepository slotRepository;

    public TimeTableServiceImpl(TimeTableRepository timeTableRepository, ClassRepository classRepository, MessageUtil messageUtil, DayOfWeekRepository dayOfWeekRepository, SlotRepository slotRepository) {
        this.timeTableRepository = timeTableRepository;
        this.classRepository = classRepository;
        this.messageUtil = messageUtil;
        this.dayOfWeekRepository = dayOfWeekRepository;
        this.slotRepository = slotRepository;
    }


    @Override
    public List<GenerateScheduleResponse> generateScheduleForClass(GenerateScheduleRequest request) {

        List<DayOfWeek> allDayOfWeek = dayOfWeekRepository.findAll();

        Map<Long, DayOfWeek> dayOfWeekMap = new HashMap<>();
        Map<EDayOfWeekCode, DayOfWeek> dayOfWeekCodeMap = new HashMap<>();

        for (DayOfWeek dayOfWeek : allDayOfWeek) {
            dayOfWeekMap.put(dayOfWeek.getId(), dayOfWeek);
            dayOfWeekCodeMap.put(dayOfWeek.getCode(), dayOfWeek);
        }
        List<Slot> allSlot = slotRepository.findAll();
        Map<Long, Slot> slotMap = new HashMap<>();
        for (Slot slot : allSlot) {
            slotMap.put(slot.getId(), slot);
        }


        List<DayOfWeek> dayOfWeeks = new ArrayList<>();
        List<Slot> slots = new ArrayList<>();
        List<TimeInWeekRequest> timeInWeekRequests = request.getTimeInWeekRequests();
        for (TimeInWeekRequest timeInWeekRequest : timeInWeekRequests) {
            Long dayOfWeekId = timeInWeekRequest.getDayOfWeekId();
            DayOfWeek dayOfWeek = dayOfWeekMap.get(dayOfWeekId);

            dayOfWeeks.add(dayOfWeek);

            Long slotId = timeInWeekRequest.getSlotId();
            Slot slot = slotMap.get(slotId);
            slots.add(slot);
        }

        // lấy tất cả ngày ở giữa start-end
        List<LocalDate> localDatesBetween = getLocalDatesBetween(instantToLocalDate(request.getStartDate()), instantToLocalDate(request.getEndDate()));
        int totalDatesBetween = localDatesBetween.size();

        List<GenerateScheduleResponse> result = new ArrayList<>();

        int numberOfSlot = 1;

        for (LocalDate date : localDatesBetween) {

            java.time.DayOfWeek dayOfWeek = date.getDayOfWeek();
            EDayOfWeekCode dayOfWeekCode = EDayOfWeekCode.valueOf(dayOfWeek.toString());
            List<EDayOfWeekCode> collectCodeDOW = dayOfWeeks.stream().map(DayOfWeek::getCode).collect(Collectors.toList());


            if (collectCodeDOW.contains(dayOfWeekCode)) {
                GenerateScheduleResponse generateScheduleResponse = new GenerateScheduleResponse();
                generateScheduleResponse.setDate(date);
                generateScheduleResponse.setNumberOfSlot(++numberOfSlot);
                generateScheduleResponse.setDayOfWeek(ConvertUtil.convertDayOfWeekToDto(dayOfWeekCodeMap.get(dayOfWeekCode)));

                DayOfWeek dayOfWeekInRequest = dayOfWeekCodeMap.get(dayOfWeekCode);
                List<TimeInWeekRequest> collect = timeInWeekRequests.stream()
                        .filter(timeInWeekRequest -> timeInWeekRequest.getDayOfWeekId().equals(dayOfWeekInRequest.getId())).collect(Collectors.toList());
                TimeInWeekRequest inWeekRequest = collect.stream().findFirst().get();
                Slot slot = slotMap.get(inWeekRequest.getSlotId());
                SlotDto slotDto = ConvertUtil.convertSlotToSlotDto(slot);

                generateScheduleResponse.setSlot(slotDto);

                result.add(generateScheduleResponse);

            }
            if (result.size() == request.getNumberOfSlot()){
                return result ;
            }
        }

//         Kiểm tra tổng ngày giữa start - end với number of slot
        if (result.size() < request.getNumberOfSlot()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Số lượng ngày học từ ngày bắt đầu đến ngày kết thúc không đủ cho số slot dạy! Vui lòng chỉnh sửa"));
        }
        return result;



    }

    public LocalDate instantToLocalDate(Instant instant) {
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        return zonedDateTime.toLocalDate().minusDays(1);
    }

    public List<LocalDate> getLocalDatesBetween(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> datesInRange = new ArrayList<>();
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        for (int i = 0; i <= daysBetween; i++) {
            LocalDate date = startDate.plusDays(i);
            datesInRange.add(date);
        }
        return datesInRange;
    }


//    @Override
//    public Boolean editTimeTable(EditClassTimeTableRequest request) {
//        TimeTable timeTable = getOrThrow(request.getTimeTableId());
//        Class clazz = timeTable.getClazz();
//
//        if (isValidTimeTable(timeTable, clazz)) {
//
//        } else {
//            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Dữ liệu chỉnh sửa thời khóa biểu không hợp lệ vui lòng thử lại");
//        }
//
//        return null;
//    }

//    @Override
//    public ApiPage<TimeTableResponse> getTimeTableByClass(Long id, Pageable pageable) {
//        Class clazz = classRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CLASS_NOT_FOUND_BY_ID) + id));
//        User currentUser = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
//        if (ClassValidator.isMentorOfClass(currentUser, clazz) || ClassValidator.isStudentOfClass(clazz, currentUser)) {
//            List<TimeTable> timeTables = clazz.getTimeTables();
//            List<TimeTableResponse> timeTableResponses = new ArrayList<>();
//            for (TimeTable timeTable : timeTables) {
//                TimeTableResponse timeTableResponse = ConvertUtil.convertTimeTableToResponse(timeTable);
//                timeTableResponses.add(timeTableResponse);
//            }
//            return PageUtil.convert(new PageImpl<>(timeTableResponses, pageable, timeTableResponses.size()));
//        }
//        return new ApiPage<>();
//    }


    private TimeTable getOrThrow(Long id) {
        return timeTableRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(TIME_TABLE_NOT_FOUND_BY_ID) + id));
    }

//    private Boolean isValidTimeTable(TimeTable timeTable, Class clazz) {
//        return this.isHasRightToEdit(clazz) && this.isClazzValidOfTimeTable(timeTable, clazz);
//    }

//    private Boolean isHasRightToEdit(Class clazz) {
//        User currentUser = SecurityUtil.getCurrentUser();
//        User mentor = clazz.getSubCourse().getMentor();
//        boolean isManager = currentUser.getRoles().stream().anyMatch(role -> Objects.equals(role.getCode(), EUserRole.MANAGER));

//        if (Objects.equals(currentUser.getId(), mentor.getId())) {
//            return true;
//        } else if (isManager) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    private Boolean isClazzValidOfTimeTable(TimeTable timeTable, Class clazz) {
        return Objects.equals(timeTable.getClazz().getId(), clazz.getId());
    }


}
