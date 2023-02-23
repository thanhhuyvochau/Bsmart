package fpt.project.bsmart.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.EAccountRole;
import fpt.project.bsmart.entity.common.EClassStatus;
import fpt.project.bsmart.entity.dto.*;
import fpt.project.bsmart.entity.request.TimeTableRequest;
import fpt.project.bsmart.entity.request.TimeTableSearchRequest;
import fpt.project.bsmart.entity.response.ClassAttendanceResponse;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.IMoodleService;
import fpt.project.bsmart.service.ITimeTableService;
import fpt.project.bsmart.util.*;
import fpt.project.bsmart.util.specification.TimeTableSpecificationBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.DayUtil.getDatesBetweenUsingJava8;


@Service
public class TimeTableServiceImpl implements ITimeTableService {

    private final IMoodleService moodleService;

    private final ClassRepository classRepository;

    private final SlotRepository slotRepository;

    private final AccountUtil accountUtil;

    private final DayOfWeekRepository dayOfWeekRepository;

    private final ArchetypeRepository archetypeRepository;

    private final ArchetypeTimeRepository archetypeTimeRepository;

    private final MessageUtil messageUtil;

    private final StudentClassRepository studentClassRepository;

    private final TimeTableRepository timeTableRepository;

    private final AccountRepository accountRepository;

    private final SecurityUtil SecurityUtil;

    private final AttendanceRepository attendanceRepository;

    private final ClassServiceImpl classServiceImpl;
    @Value("${moodle-course-url}")
    private  String moodleViewLink;


    public TimeTableServiceImpl(IMoodleService moodleService, ClassRepository classRepository, SlotRepository slotRepository, AccountUtil accountUtil, DayOfWeekRepository dayOfWeekRepository, ArchetypeRepository archetypeRepository, ArchetypeTimeRepository archetypeTimeRepository, MessageUtil messageUtil, StudentClassRepository studentClassRepository, TimeTableRepository timeTableRepository, AccountRepository accountRepository, fpt.project.bsmart.util.SecurityUtil securityUtil, AttendanceRepository attendanceRepository, ClassServiceImpl classServiceImpl) {
        this.moodleService = moodleService;
        this.classRepository = classRepository;
        this.slotRepository = slotRepository;
        this.accountUtil = accountUtil;
        this.dayOfWeekRepository = dayOfWeekRepository;
        this.archetypeRepository = archetypeRepository;
        this.archetypeTimeRepository = archetypeTimeRepository;
        this.messageUtil = messageUtil;
        this.studentClassRepository = studentClassRepository;
        this.timeTableRepository = timeTableRepository;
        this.accountRepository = accountRepository;
        SecurityUtil = securityUtil;
        this.attendanceRepository = attendanceRepository;
        this.classServiceImpl = classServiceImpl;
    }


    @Override
    public String createTimeTableClass(Long classId, Long numberSlot, TimeTableRequest timeTableRequest) throws ParseException, JsonProcessingException {
        Account currentUser = SecurityUtil.getCurrentUserThrowNotFoundException();


        Class aClass = new Class();
        if (currentUser.getRole().getCode().equals(EAccountRole.MANAGER)) {
            aClass = classRepository.findById(classId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay class" + classId));
            aClass.setStatus(EClassStatus.RECRUITING);
        } else {
            aClass = classRepository.findByIdAndAccount(classId, currentUser);
        }

        if (aClass == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Class không tồn tai"));
        }

        if (!aClass.getStatus().equals(EClassStatus.REQUESTING) && !aClass.getStatus().equals(EClassStatus.RECRUITING) &&
                !aClass.getStatus().equals(EClassStatus.NOTSTART)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Không thể tạo thời khoá biểu cho lớp này, vì trang thái lớp không cho phép"));
        }

        if (aClass.getTimeTables().size() > 0) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("class đã có thời khoá biểu"));
        }

        if (timeTableRequest.getSlotDow().size() > 8 || timeTableRequest.getSlotDow().size() < 2) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Vui lòng kiểm tra lại số buổi trong tuần "));
        }

        if (archetypeRepository.existsByCode(aClass.getCode())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("time table code da ton tai"));
        }
        if (timeTableRequest.getSlotDow().size() != numberSlot) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Thêm số buổi dạy trong tuần đang lớn / nhỏ hơn với số buổi bạn đã chọn "));
        }

        //Set Archetype
        Archetype archetype = new Archetype();
        archetype.setCode(aClass.getCode());
        archetype.setName(timeTableRequest.getArchetypeName());
        if (aClass.getAccount() != null) {
            archetype.setCreatedByTeacherId(aClass.getAccount().getId());
        }


        List<SlotDowDto> slotDows = timeTableRequest.getSlotDow();


        List<Slot> slotList = slotRepository.findAll();
        Map<Long, Slot> slotMap = HashMapUtil.convertSlotListToMap(slotList);

        List<fpt.project.bsmart.entity.DayOfWeek> dayOfWeekList = dayOfWeekRepository.findAll();
        Map<Long, fpt.project.bsmart.entity.DayOfWeek> dayOfWeekMap = HashMapUtil.convertDoWListToMap(dayOfWeekList);

        List<TimeTable> timeTableList = new ArrayList<>();


        Instant startDate = aClass.getStartDate();

        Instant endDate = aClass.getEndDate();

        // kiem tra slot va thu trùng nhau 
        List<Integer> allSlotNumber = slotDows.stream().map(SlotDowDto::getSlotNumber).collect(Collectors.toList());
        Set<Integer> items = new HashSet<>();
        List<Integer> checkDuplicateSlotNumber = allSlotNumber.stream().filter(n -> !items.add(n)).collect(Collectors.toList());
        if (checkDuplicateSlotNumber.size() > 0) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Slot number không thể trùng"));
        }

        int slotNumber = 1;

        List<Long> allDayOfWeek = slotDows.stream().map(SlotDowDto::getDayOfWeekId).collect(Collectors.toList());
        Set<Long> itemDayOfWeek = new HashSet<>();
        List<Long> checkDuplicateDayOfWeek = allDayOfWeek.stream().filter(n -> !itemDayOfWeek.add(n)).collect(Collectors.toList());
        if (checkDuplicateDayOfWeek.size() > 0) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Thứ không thể trùng"));
        }

        List<TimeTable> timeTableList1 = setDateOfWeek(timeTableRequest.getSlotDow(), slotNumber, startDate, endDate, archetype, aClass, timeTableList);

        aClass.getTimeTables().clear();
        aClass.getTimeTables().addAll(timeTableList1);

        moodleService.enrolUserToCourseMoodle(aClass, aClass.getAccount());
        classRepository.save(aClass);


        return moodleViewLink+aClass.getMoodleClassId();
    }

    private List<TimeTable> setDateOfWeek(List<SlotDowDto> slotDowDtoList, int slotNumber, Instant startDate,
                                          Instant endDate, Archetype archetype, Class aClass, List<TimeTable> timeTableList) throws ParseException {

        List<Slot> slotList = slotRepository.findAll();
        Map<Long, Slot> slotMap = HashMapUtil.convertSlotListToMap(slotList);

        List<fpt.project.bsmart.entity.DayOfWeek> dayOfWeekList = dayOfWeekRepository.findAll();
        Map<Long, fpt.project.bsmart.entity.DayOfWeek> dayOfWeekMap = HashMapUtil.convertDoWListToMap(dayOfWeekList);

        Instant date = null;
        for (SlotDowDto slotDowDto : slotDowDtoList) {
            fpt.project.bsmart.entity.DayOfWeek dayOfWeek = dayOfWeekMap.get(slotDowDto.getDayOfWeekId());
            if (dayOfWeek == null) {
                throw ApiException.create(HttpStatus.BAD_REQUEST)
                        .withMessage(messageUtil.getLocalMessage("Không tìm thấy thứ "));
            }
            Slot slot = null;
            LocalDate datesBetweenUsingJava81 = getDatesBetweenUsingJava8(startDate.toString(), DayOfWeek.valueOf(dayOfWeek.getCode().name()));
            if (datesBetweenUsingJava81 == null) {
                throw ApiException.create(HttpStatus.BAD_REQUEST)
                        .withMessage(messageUtil.getLocalMessage("Khong tim thấy thứ trong tuần"));
            }
            {
                slot = slotMap.get(slotDowDto.getSlotId());
            }
            ArchetypeTime archetypeTime = new ArchetypeTime();
            archetypeTime.setSlot(slot);


            archetypeTime.setDayOfWeek(dayOfWeek);
            archetypeTime.setArchetype(archetype);


            //Set TimeTable
            TimeTable timeTable = new TimeTable();
            timeTable.setSlotNumber(slotNumber);
            ++slotNumber;

            date = datesBetweenUsingJava81.atStartOfDay().toInstant(ZoneOffset.UTC);
            if (date != null) {
                timeTable.setDate(DayUtil.convertDayInstant(date.toString()));
            }
            timeTable.setClazz(aClass);
            timeTable.setArchetypeTime(archetypeTime);


            List<StudentClass> studentClasses = aClass.getStudentClasses();
            List<Account> students = studentClasses.stream().map(StudentClass::getAccount).collect(Collectors.toList());
            List<Attendance> attendanceList = new ArrayList<>();
            students.forEach(account -> {
                Attendance attendance = new Attendance();
                attendance.setTimeTable(timeTable);
                StudentClassKey studentClassKey = new StudentClassKey();
                studentClassKey.setClassId(aClass.getId());
                studentClassKey.setStudentId(account.getId());
                attendance.setStudentClassKeyId(studentClassKey);
                attendanceList.add(attendance);
            });
            timeTable.setAttendances(attendanceList);
            if (date != null) {
                if (date.isAfter(endDate)) {
                    return timeTableList;
                } else {
                    timeTableList.add(timeTable);
                }
            }
        }
        if (date != null) {
            if (date.isAfter(endDate)) {
                return timeTableList;
            } else {
                setDateOfWeek(slotDowDtoList, slotNumber, startDate.plus(7, ChronoUnit.DAYS), endDate, archetype, aClass, timeTableList);
            }
        }
        return timeTableList;
    }

    @Override
    public ApiPage<TimeTableDto> getTimeTableInDay(TimeTableSearchRequest timeTableSearchRequest, Pageable pageable) {
        Account currentUser = SecurityUtil.getCurrentUserThrowNotFoundException();

        Class aClass = classRepository.findById(timeTableSearchRequest.getClassId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay class" + timeTableSearchRequest.getClassId()));

        if (currentUser.getRole() != null) {
            if (currentUser.getRole().getCode().equals(EAccountRole.STUDENT)) {
                List<StudentClass> studentClasses = aClass.getStudentClasses();
                List<Account> studentAccount = studentClasses.stream().map(StudentClass::getAccount).collect(Collectors.toList());
                if (!studentAccount.contains(currentUser)) {
                    throw ApiException.create(HttpStatus.BAD_REQUEST)
                            .withMessage(messageUtil.getLocalMessage("Bạn không có trong lớp nay"));
                }

            }
            if (currentUser.getRole().getCode().equals(EAccountRole.TEACHER)) {
                Account account = aClass.getAccount();
                if (!currentUser.getId().equals(account.getId())) {
                    throw ApiException.create(HttpStatus.BAD_REQUEST)
                            .withMessage(messageUtil.getLocalMessage("Bạn không có dạy lớp nay"));
                }
            }
        }


        TimeTableSpecificationBuilder timeTableSpecificationBuilder = TimeTableSpecificationBuilder.specification()
                .queryTeacherInClass(currentUser)
                .queryClass(aClass)
                .date(timeTableSearchRequest.getDateFrom(), timeTableSearchRequest.getDateTo());

        List<TimeTable> timeTableInDay = timeTableRepository.findAll(timeTableSpecificationBuilder.build());
        List<TimeTableDto> timeTableDtoList = new ArrayList<>();

        timeTableInDay.forEach(timeTable -> {
            TimeTableDto timeTableDto = ObjectUtil.copyProperties(timeTable, new TimeTableDto(), TimeTableDto.class);
            ArchetypeTimeDto archetypeTimeDto = new ArchetypeTimeDto();
            ArchetypeTime archetypeTime = timeTable.getArchetypeTime();
            if (archetypeTime != null) {
                Slot slot = archetypeTime.getSlot();
                if (slot != null) {
                    archetypeTimeDto.setSlot(ObjectUtil.copyProperties(slot, new SlotDto(), SlotDto.class));
                }
                fpt.project.bsmart.entity.DayOfWeek dayOfWeek = archetypeTime.getDayOfWeek();
                if (dayOfWeek != null) {
                    archetypeTimeDto.setDayOfWeek(ObjectUtil.copyProperties(dayOfWeek, new DayOfWeekDto(), DayOfWeekDto.class));
                }
            }

            timeTableDto.setArchetypeTime(archetypeTimeDto);
            timeTableDtoList.add(timeTableDto);
        });
        Page<TimeTableDto> page = new PageImpl<>(timeTableDtoList, pageable, timeTableDtoList.size());

        return PageUtil.convert(page);
    }

    @Override
    public List<ClassAttendanceResponse> accountGetAllTimeTable() {
        Account currentUser = SecurityUtil.getCurrentUserThrowNotFoundException();
        List<ClassAttendanceResponse> classAttendanceResponseList = new ArrayList<>();
        List<Class> classList = null;
        if (currentUser.getRole().getCode().equals(EAccountRole.TEACHER)) {
            classList = currentUser.getTeacherClass();
        } else if (currentUser.getRole().getCode().equals(EAccountRole.STUDENT)) {
            classList = currentUser.getStudentClasses()
                    .stream().map(StudentClass::getaClass).collect(Collectors.toList());
        }
        classList.forEach(aClass -> {
            List<TimeTable> timeTables = aClass.getTimeTables();
            ClassAttendanceResponse classAttendanceResponse = new ClassAttendanceResponse();
            classAttendanceResponse.setClassId(aClass.getId());

            List<AttendanceDto> attendanceDtoList = new ArrayList<>();
            timeTables.forEach(timeTable -> {

                List<Attendance> attendances = timeTable.getAttendances();
                AttendanceDto attendanceDto = new AttendanceDto();
                attendanceDto.setTimeTableId(timeTable.getId());
                attendanceDto.setDate(timeTable.getDate());
                attendanceDto.setSlotNumber(timeTable.getSlotNumber());
                attendances.forEach(attendance -> {

                    attendanceDto.setId(attendance.getId());
                    attendanceDto.setPresent(attendance.getPresent());

                    ArchetypeTime archetypeTime = timeTable.getArchetypeTime();
                    if (archetypeTime != null) {
                        if (archetypeTime.getSlot() != null) {
                            attendanceDto.setSlotCode(archetypeTime.getSlot().getCode());
                            attendanceDto.setSlotName(archetypeTime.getSlot().getName());
                            Slot slot = archetypeTime.getSlot();
                            attendanceDto.setStartTime(slot.getStartTime());
                            attendanceDto.setEndTime(slot.getEndTime());
                        }
                        if (archetypeTime.getDayOfWeek() != null) {
                            attendanceDto.setDowCode(archetypeTime.getDayOfWeek().getCode());
                            attendanceDto.setDowName(archetypeTime.getDayOfWeek().getName());
                        }

                        Archetype archetype = archetypeTime.getArchetype();
                        if (archetype != null) {
                            attendanceDto.setArchetypeCode(archetype.getCode());
                            attendanceDto.setArchetypeName(archetype.getName());
                        }
                    }

                });
                attendanceDtoList.add(attendanceDto);
            });
            classAttendanceResponse.setAttendance(attendanceDtoList);
            classAttendanceResponseList.add(classAttendanceResponse);
        });

        return classAttendanceResponseList;


    }

    @Override
    public Long adminCreateTimeTableClass(Long classId, Long numberSlot, TimeTableRequest timeTableRequest) throws ParseException {
        Account currentUser = SecurityUtil.getCurrentUserThrowNotFoundException();

        Class aClass = classRepository.findById(classId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Lớp không tìm thấy" + classId));
        if (!aClass.getStatus().equals(EClassStatus.RECRUITING)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Lớp không thể cập nhật"));
        }

        if (aClass.getTimeTables().size() > 0) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("class đã có thời khoá biểu"));
        }
        if (timeTableRequest.getSlotDow().size() > 7|| timeTableRequest.getSlotDow().size() < 1) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Vui lòng kiểm tra lại số buổi trong tuần "));
        }

        if (archetypeRepository.existsByCode(aClass.getCode())) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("time table code da ton tai"));
        }
        if (timeTableRequest.getSlotDow().size() != numberSlot) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Thêm số buổi dạy trong tuần đang lớn / nhỏ hơn với số buổi bạn đã chọn "));
        }

        //Set Archetype
        Archetype archetype = new Archetype();
        archetype.setCode(aClass.getCode());
        archetype.setName(timeTableRequest.getArchetypeName());
        if (aClass.getAccount() != null) {
            archetype.setCreatedByTeacherId(aClass.getAccount().getId());
        }


        List<SlotDowDto> slotDows = timeTableRequest.getSlotDow();


        List<Slot> slotList = slotRepository.findAll();
        Map<Long, Slot> slotMap = HashMapUtil.convertSlotListToMap(slotList);

        List<fpt.project.bsmart.entity.DayOfWeek> dayOfWeekList = dayOfWeekRepository.findAll();
        Map<Long, fpt.project.bsmart.entity.DayOfWeek> dayOfWeekMap = HashMapUtil.convertDoWListToMap(dayOfWeekList);

        List<TimeTable> timeTableList = new ArrayList<>();

        Instant startDate = aClass.getStartDate();
        Instant endDate = aClass.getEndDate();

        // kiem tra slot va thu trùng nhau
        List<Integer> allSlotNumber = slotDows.stream().map(SlotDowDto::getSlotNumber).collect(Collectors.toList());
        Set<Integer> items = new HashSet<>();
        List<Integer> checkDuplicateSlotNumber = allSlotNumber.stream().filter(n -> !items.add(n)).collect(Collectors.toList());
        if (checkDuplicateSlotNumber.size() > 0) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Slot number không thể trùng"));
        }

        int slotNumber = 1;

//        List<Long> allSlot = slotDows.stream().map(SlotDowDto::getSlotId).collect(Collectors.toList());
//        Set<Long> itemSlot = new HashSet<>();
//        List<Long> checkDuplicateSlot = allSlot.stream().filter(n -> !itemSlot.add(n)).collect(Collectors.toList());
//
//        List<Long> allDayOfWeek = slotDows.stream().map(SlotDowDto::getDayOfWeekId).collect(Collectors.toList());
//        Set<Long> itemDayOfWeek = new HashSet<>();
//        List<Long> checkDuplicateDayOfWeek = allDayOfWeek.stream().filter(n -> !itemDayOfWeek.add(n))
//                .map(allSlot.g).collect(Collectors.toList());


        List<String> checkDuplicate = new ArrayList<>();
        slotDows.forEach(slotDowDto -> {
            Long dayOfWeekId = slotDowDto.getDayOfWeekId();
            Long slotId = slotDowDto.getSlotId();
            checkDuplicate.add(dayOfWeekId + "" + slotId);
        });
        Set<String> setCheckDuplicate = new HashSet<>();
        List<String> resultDuplicate = checkDuplicate.stream().filter(n -> !setCheckDuplicate.add(n)).collect(Collectors.toList());
        if (resultDuplicate.size() > 0) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage("Thời gian dạy trong ngày nhau"));
        }

        List<TimeTable> timeTableList1 = setDateOfWeek(timeTableRequest.getSlotDow(), slotNumber, startDate, endDate, archetype, aClass, timeTableList);


        aClass.getTimeTables().clear();
        aClass.getTimeTables().addAll(timeTableList1);
//        aClass.setStatus(EClassStatus.REQUESTING);

        classRepository.save(aClass);

        return aClass.getId();
    }

    @Override
    public Long adminUpdateTimeTableClass(Long classId, Long numberSlot, TimeTableRequest timeTableRequest) {
        return null;
    }


}
