package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ValidationErrors;
import fpt.project.bsmart.entity.common.ValidationErrorsException;
import fpt.project.bsmart.entity.constant.EDayOfWeekCode;
import fpt.project.bsmart.entity.request.TimeInWeekRequest;
import fpt.project.bsmart.entity.request.timetable.GenerateScheduleRequest;
import fpt.project.bsmart.entity.request.timetable.MentorCreateScheduleRequest;
import fpt.project.bsmart.entity.response.TimeTableResponse;
import fpt.project.bsmart.entity.response.timetable.GenerateScheduleResponse;
import fpt.project.bsmart.repository.ClassRepository;
import fpt.project.bsmart.repository.DayOfWeekRepository;
import fpt.project.bsmart.repository.SlotRepository;
import fpt.project.bsmart.repository.TimeTableRepository;
import fpt.project.bsmart.service.ITimeTableService;
import fpt.project.bsmart.util.ConvertUtil;
import fpt.project.bsmart.util.MessageUtil;
import fpt.project.bsmart.util.SecurityUtil;
import fpt.project.bsmart.util.TimeUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.CLASS_NOT_FOUND_BY_ID;
import static fpt.project.bsmart.util.Constants.ErrorMessage.TIME_TABLE_NOT_FOUND_BY_ID;

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
        List<TimeInWeekRequest> timeInWeekRequests = request.getTimeInWeekRequests();
        Map<Long, Slot> slotMap = slotRepository.findAll().stream().collect(Collectors.toMap(Slot::getId, Function.identity()));
        Map<Long, DayOfWeek> dowMap = dayOfWeekRepository.findAll().stream().collect(Collectors.toMap(DayOfWeek::getId, Function.identity()));

        List<EDayOfWeekCode> availableDOW = timeInWeekRequests.stream().map(timeInWeek -> dowMap.get(timeInWeek.getDayOfWeekId()).getCode())
                .distinct()
                .collect(Collectors.toList());

        List<GenerateScheduleResponse> generateScheduleResponses = new ArrayList<>();

        Instant date = request.getStartDate();
        Integer numberOfSlot = request.getNumberOfSlot();
        int i = numberOfSlot;
        while (i > 0) {
            EDayOfWeekCode dayOfWeekCode = TimeUtil.getDayOfWeek(date);
            if (dayOfWeekCode == null) {
                throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không thể nhận diện thứ trong tuần, lỗi hệ thống vui lòng liên hệ Admin!");
            }
            if (availableDOW.contains(dayOfWeekCode)) {
                List<TimeInWeekRequest> dateOfWeeks = timeInWeekRequests.stream().filter(timeInWeek -> {
                    DayOfWeek dayOfWeek = dowMap.get(timeInWeek.getDayOfWeekId());
                    return Objects.equals(dayOfWeek.getCode(), dayOfWeekCode);
                }).collect(Collectors.toList());
                for (TimeInWeekRequest dow : dateOfWeeks) {
                    if (i <= 0) break;
                    GenerateScheduleResponse generateScheduleResponse = new GenerateScheduleResponse();
                    generateScheduleResponse.setDate(date);
                    generateScheduleResponse.setNumberOfSlot((numberOfSlot - i) + 1);
                    Slot slot = slotMap.get(dow.getSlotId());
                    generateScheduleResponse.setSlot(ConvertUtil.convertSlotToSlotDto(slot));
                    generateScheduleResponses.add(generateScheduleResponse);
                    DayOfWeek dayOfWeek = dowMap.get(dow.getDayOfWeekId());
                    generateScheduleResponse.setDayOfWeek(ConvertUtil.convertDayOfWeekToDto(dayOfWeek));
                    i--;
                }
            }
            date = date.plus(1, ChronoUnit.DAYS);
        }
        return generateScheduleResponses;


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

    @Override
    public Boolean mentorCreateScheduleForClass(Long classId, List<MentorCreateScheduleRequest> request) throws ValidationErrorsException {
        Class aClass = classRepository.findById(classId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(CLASS_NOT_FOUND_BY_ID) + classId));

        List<Slot> allSlot = slotRepository.findAll();
        Map<Long, Slot> slotMap = new HashMap<>();
        for (Slot slot : allSlot) {
            slotMap.put(slot.getId(), slot);
        }

        List<TimeTable> timeTables = new ArrayList<>();
        ArrayList<MentorCreateScheduleRequest> duplicateDate = new ArrayList<>();
        ValidationErrors<MentorCreateScheduleRequest> vaErr = new ValidationErrors<>();
        for (MentorCreateScheduleRequest mentorCreateScheduleRequest : request) {

            Long checkDuplicate = timeTableRepository.countByClassIdAndDateAndSlotId(classId, mentorCreateScheduleRequest.getDate(), mentorCreateScheduleRequest.getSlot().getId());
            if (checkDuplicate > 0) {
                ValidationErrors.ValidationError<MentorCreateScheduleRequest> validationError = new ValidationErrors.ValidationError<>();
                validationError.setMessage("Trùng ngày dạy ");

                duplicateDate.add(mentorCreateScheduleRequest);
                validationError.setInvalidParams(duplicateDate);
                vaErr.setError(validationError);

            }
            // Check for duplicate entries
            TimeTable timeTable = new TimeTable();
            timeTable.setDate(mentorCreateScheduleRequest.getDate());
            timeTable.setCurrentSlotNum(mentorCreateScheduleRequest.getNumberOfSlot());
            Slot slot = slotMap.get(mentorCreateScheduleRequest.getSlot().getId());
            timeTable.setSlot(slot);
            timeTable.setClazz(aClass);
            timeTables.add(timeTable);

        }
        if (duplicateDate.size() > 0) {
            throw new ValidationErrorsException(vaErr.getError().getInvalidParams(), vaErr.getError().getMessage());
        }

        timeTableRepository.saveAll(timeTables);
//        aClass.setTimeTables(timeTables);
//        classRepository.save(aClass) ;
        return true;
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

    @Override
    public List<TimeTableResponse> getTimeTableByClass(Long id) {
        Class clazz = classRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CLASS_NOT_FOUND_BY_ID) + id));
        User currentUser = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
//        if (ClassValidator.isMentorOfClass(currentUser, clazz) || ClassValidator.isStudentOfClass(clazz, currentUser)) {
//            List<TimeTable> timeTables = clazz.getTimeTables();
//            List<TimeTableResponse> timeTableResponses = new ArrayList<>();
//            for (TimeTable timeTable : timeTables) {
//                TimeTableResponse timeTableResponse = ConvertUtil.convertTimeTableToResponse(timeTable);
//                timeTableResponses.add(timeTableResponse);
//            }
//            return PageUtil.convert(new PageImpl<>(timeTableResponses, pageable, timeTableResponses.size()));
//        }
        List<TimeTable> timeTables = clazz.getTimeTables();
        List<TimeTableResponse> timeTableResponses = new ArrayList<>();
        for (TimeTable timeTable : timeTables) {
            TimeTableResponse timeTableResponse = ConvertUtil.convertTimeTableToResponse(timeTable);
            timeTableResponses.add(timeTableResponse);
        }
        return timeTableResponses;
    }


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
