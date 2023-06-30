package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.request.AttendanceDetailRequest;
import fpt.project.bsmart.entity.request.AttendanceRequest;
import fpt.project.bsmart.entity.response.*;
import fpt.project.bsmart.repository.AttendanceRepository;
import fpt.project.bsmart.repository.ClassRepository;
import fpt.project.bsmart.repository.TimeTableRepository;
import fpt.project.bsmart.service.AttendanceService;

import fpt.project.bsmart.util.ConvertUtil;
import fpt.project.bsmart.util.PageUtil;
import fpt.project.bsmart.util.SecurityUtil;
import fpt.project.bsmart.validator.AttendanceValidator;
import fpt.project.bsmart.validator.ClassValidator;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    private final ModelMapper modelMapper;

    private final ClassRepository classRepository;
    private final TimeTableRepository timeTableRepository;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository, ModelMapper modelMapper, ClassRepository classRepository, TimeTableRepository timeTableRepository) {
        this.attendanceRepository = attendanceRepository;
        this.modelMapper = modelMapper;
        this.classRepository = classRepository;
        this.timeTableRepository = timeTableRepository;
    }

    @Override
    public Boolean doAttendance(AttendanceRequest request) {
        Optional<User> currentUserOptional = SecurityUtil.getCurrentUserOptional();
        User currentUser = SecurityUtil.getUserOrThrowException(currentUserOptional);
        TimeTable timeTable = timeTableRepository.findById(request.getTimeTableId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy thời khóa biểu với id:" + request.getTimeTableId()));
        Class clazz = timeTable.getClazz();
        if (!ClassValidator.isMentorOfClass(currentUser, clazz)) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Bạn không phải là giáo viên của lớp này" + request.getTimeTableId());
        }
        List<Attendance> attendanceList = timeTable.getAttendanceList();
        boolean attendanceListEmpty = attendanceList.isEmpty();
        if (attendanceListEmpty) {
            attendanceList.addAll(createNewAttendances(request, timeTable));
        } else {
            editAttendances(request, timeTable);
        }

        attendanceRepository.saveAll(attendanceList);
        return true;
    }

    private List<Attendance> createNewAttendances(AttendanceRequest request, TimeTable timeTable) {
        List<Attendance> attendances = new ArrayList<>();
        Class clazz = timeTable.getClazz();
        if (!AttendanceValidator.isEnableTimeForDoAttendance(timeTable.getDate())) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Ngày không hợp lệ không thể điểm danh");
        }
        List<AttendanceDetailRequest> details = request.getDetails();
        if (AttendanceValidator.isDuplicateAttendanceDetail(details)) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Điểm danh bị trùng lặp học sinh");
        }
        for (AttendanceDetailRequest detail : details) {
            StudentClass studentClass = ClassValidator.isExistedStudentClass(clazz, detail.getStudentClassId())
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                            .withMessage("Không tìm thấy học sinh cần điểm danh với id:" + detail.getStudentClassId()));
            Attendance attendance = new Attendance(timeTable, studentClass, detail.isAttendance(), detail.getNote());
            attendances.add(attendance);
        }
        return attendances;
    }

    private List<Attendance> editAttendances(AttendanceRequest request, TimeTable timeTable) {
        List<Attendance> attendances = timeTable.getAttendanceList();
        Map<Long, Attendance> existedAttendancesMap = attendances.stream()
                .collect(Collectors.toMap(attendance -> attendance.getStudentClass().getId(), Function.identity()));

        Class clazz = timeTable.getClazz();
        if (!AttendanceValidator.isEnableTimeForDoAttendance(timeTable.getDate())) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Ngày không hợp lệ không thể điểm danh");
        }
        List<AttendanceDetailRequest> details = request.getDetails();
        if (AttendanceValidator.isDuplicateAttendanceDetail(details)) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Điểm danh bị trùng lặp học sinh");
        }
        for (AttendanceDetailRequest detail : details) {
            StudentClass studentClass = ClassValidator.isExistedStudentClass(clazz, detail.getStudentClassId())
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                            .withMessage("Không tìm thấy học sinh cần điểm danh với id:" + detail.getStudentClassId()));

            Attendance attendance = existedAttendancesMap.get(detail.getStudentClassId());
            if (attendance == null) {
                attendance = new Attendance(timeTable, studentClass, detail.isAttendance(), detail.getNote());
                attendances.add(attendance);
            } else {
                attendance.setNote(detail.getNote());
                attendance.setAttendance(detail.isAttendance());
            }
        }
        return attendances;
    }

    @Override
    public AttendanceResponseWrapper getAttendanceByTimeTableForTeacher(Long timeTableId, Pageable pageable) {
        Optional<User> currentUserOptional = SecurityUtil.getCurrentUserOptional();
        User currentUser = SecurityUtil.getUserOrThrowException(currentUserOptional);
        TimeTable timeTable = timeTableRepository.findById(timeTableId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy thời khóa biểu với id:" + timeTableId));
        Class clazz = timeTable.getClazz();
        if (!ClassValidator.isMentorOfClass(currentUser, clazz)) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Bạn không phải là giáo viên của lớp này");
        }
        Map<Long, Attendance> attendanceMap = timeTable.getAttendanceList().stream()
                .collect(Collectors.toMap(attendance -> attendance.getStudentClass().getId(), Function.identity()));
        List<StudentClass> studentClasses = clazz.getStudentClasses();
        List<AttendanceResponse> attendanceResponses = new ArrayList<>();
        for (StudentClass studentClass : studentClasses) {
            StudentClassResponse studentClassResponse = ConvertUtil.convertStudentClassToResponse(studentClass);
            AttendanceResponse attendanceResponse = new AttendanceResponse();
            attendanceResponse.setStudent(studentClassResponse);
            Attendance attendance = attendanceMap.get(studentClass.getId());
            if (attendance != null) {
                attendanceResponse.setNote(attendance.getNote());
                attendanceResponse.setId(attendance.getId());
                attendanceResponse.setAttendance(attendance.getAttendance());
            }
            attendanceResponses.add(attendanceResponse);
        }
        Page<AttendanceResponse> attendanceResponsePage = new PageImpl<>(attendanceResponses, pageable, attendanceResponses.size());
        TimeTableResponse timeTableResponse = ConvertUtil.convertTimeTableToResponse(timeTable);
        ApiPage<AttendanceResponse> attendanceResponse = PageUtil.convert(attendanceResponsePage);
        AttendanceResponseWrapper responseWrapper = new AttendanceResponseWrapper(timeTableResponse, attendanceResponse);
        return responseWrapper;
    }

    @Override
    public AttendanceStudentResponse getAttendanceByClassForStudent(Long classId) {
        Class clazz = classRepository
                .findById(classId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage("Không tìm thấy lớp với id:" + classId));
        User currentUser = SecurityUtil.getCurrentUser();
        if (!ClassValidator.isStudentOfClass(clazz, currentUser)) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Bạn không phải là học sinh của lớp này");
        }

        List<TimeTable> timeTables = clazz.getTimeTables();
        List<AttendanceStudentDetailResponse> attendanceStudentDetailResponses = new ArrayList<>();
        int absentNum = 0;
        for (TimeTable timeTable : timeTables) {
            Optional<Attendance> attendanceByUser = getAttendanceByUser(currentUser, timeTable.getAttendanceList());

            AttendanceStudentDetailResponse detailResponse = new AttendanceStudentDetailResponse();
            detailResponse.setDate(timeTable.getDate());
            detailResponse.setSlotNum(timeTable.getCurrentSlotNums());
            if (attendanceByUser.isPresent()) {
                Attendance attendance = attendanceByUser.get();
                detailResponse.setNote(attendance.getNote());
                detailResponse.setAttendance(attendance.getAttendance());
                if (!attendance.getAttendance()) {
                    absentNum++;
                }
            }
            attendanceStudentDetailResponses.add(detailResponse);
        }
        AttendanceStudentResponse response = new AttendanceStudentResponse();
        clazz.getSubCourse().getNumberOfSlot();
        response.setAttendanceStudentDetails(attendanceStudentDetailResponses);
        response.setAbsentPercentage(calculateAbsentPercentage(clazz.getSubCourse().getNumberOfSlot(), absentNum));
        return response;
    }

    private static Optional<Attendance> getAttendanceByUser(User currentUser, List<Attendance> attendanceList) {
        return attendanceList.stream().filter(att -> Objects.equals(att.getStudentClass().getStudent().getId(), currentUser.getId())).findFirst();
    }

    private static double calculateAbsentPercentage(Integer totalSlot, Integer absentNumber) {
        return new BigDecimal((Double.valueOf(absentNumber) / Double.valueOf(totalSlot) * 100)).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

}