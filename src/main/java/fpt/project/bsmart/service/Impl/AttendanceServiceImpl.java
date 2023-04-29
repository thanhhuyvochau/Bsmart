package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.response.AttendanceResponse;
import fpt.project.bsmart.repository.AttendanceRepository;
import fpt.project.bsmart.repository.ClassRepository;
import fpt.project.bsmart.service.AttendanceService;
import fpt.project.bsmart.util.SecurityUtil;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    private final ModelMapper modelMapper;

    private final ClassRepository classRepository;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository, ModelMapper modelMapper, ClassRepository classRepository) {
        this.attendanceRepository = attendanceRepository;
        this.modelMapper = modelMapper;
        this.classRepository = classRepository;
    }

    @Override
    public List<AttendanceResponse> studentGetAllAttendance(Long classId) {

        User currentUserAccountLogin = SecurityUtil.getCurrentUser();
        if (classId != null) {

            Class aClass = classRepository.findById(classId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy lớp với id:" + classId));
            if (currentUserAccountLogin != null) {
                List<Class> listClass = currentUserAccountLogin.getStudentClasses().stream().map(StudentClass::getClazz).collect(Collectors.toList());
                if (!listClass.contains(aClass)) {
                    throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Bạn không có trong danh sách học sinh lớp này! Không thể xem điểm danh");
                }
                List<TimeTable> timeTables = aClass.getTimeTables();
                return getAttendanceOfOneClass(timeTables);
            }

        } else {

            final List<AttendanceResponse> attendanceAllClassResponseList = new ArrayList<>();


            if (currentUserAccountLogin != null) {
                if (currentUserAccountLogin.getStudentClasses() != null) {
                    List<StudentClass> studentClasses = currentUserAccountLogin.getStudentClasses();
                    List<Long> classIds = studentClasses.stream().map(studentClass -> studentClass.getClazz().getId()).collect(Collectors.toList());
                    List<Class> classList = classRepository.findAllById(classIds);
                    return getAttendanceOfManyClass(classList);
                }
            }

            return attendanceAllClassResponseList;
        }

        return null;
    }

    @Override
    public List<AttendanceResponse> teacherGetAllAttendance(Long classId) {
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();
        if (classId != null) {
            Class aClass = classRepository.findById(classId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy lớp với id:" + classId));
            SubCourse subCourse = aClass.getSubCourse();
            User mentor = subCourse.getMentor();
            if (mentor != currentUserAccountLogin) {
                throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Bạn không có trong danh sách giáo viên của lớp này! Không thể xem điểm danh");
            }
            List<TimeTable> timeTables = aClass.getTimeTables();
            return getAttendanceOfOneClass(timeTables);
        } else {
            if (currentUserAccountLogin != null) {
                List<StudentClass> studentClasses = currentUserAccountLogin.getStudentClasses();
                List<Long> classIds = studentClasses.stream().map(studentClass -> studentClass.getClazz().getId()).collect(Collectors.toList());
                List<Class> classList = classRepository.findAllById(classIds);
                return getAttendanceOfManyClass(classList);
            }

        }
        return null;
    }

    private List<AttendanceResponse> getAttendanceOfOneClass(List<TimeTable> timeTables) {
        List<Attendance> attendanceList = new ArrayList<>();
        List<AttendanceResponse> attendanceResponseList = new ArrayList<>();
        for (TimeTable timeTable : timeTables) {
            attendanceList.addAll(timeTable.getAttendanceList());
        }
        Type listType = new TypeToken<List<AttendanceResponse>>() {
        }.getType();
        modelMapper.createTypeMap(Attendance.class, AttendanceResponse.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getTimeTable().getDate(), AttendanceResponse::setDate);
                    mapper.map(src -> src.getTimeTable().getClazz().getId(), AttendanceResponse::setClassId);
                    mapper.map(src -> src.getTimeTable().getSlot().getId(), AttendanceResponse::setSlotId);
                });

        attendanceResponseList = modelMapper.map(attendanceList, listType);
        return attendanceResponseList;
    }

    private List<AttendanceResponse> getAttendanceOfManyClass(List<Class> classList) {
        List<AttendanceResponse> attendanceAllClassResponseList = new ArrayList<>();
        classList.forEach(aClass -> {
            List<TimeTable> timeTables = aClass.getTimeTables();
            List<Attendance> attendanceList = new ArrayList<>();
            for (TimeTable timeTable : timeTables) {
                attendanceList.addAll(timeTable.getAttendanceList());
            }
            Type listType = new TypeToken<List<AttendanceResponse>>() {
            }.getType();
            modelMapper.createTypeMap(Attendance.class, AttendanceResponse.class)
                    .addMappings(mapper -> {
                        mapper.map(src -> src.getTimeTable().getDate(), AttendanceResponse::setDate);
                        mapper.map(src -> src.getTimeTable().getClazz().getId(), AttendanceResponse::setClassId);
                        mapper.map(src -> src.getTimeTable().getSlot().getId(), AttendanceResponse::setSlotId);
                    });

            attendanceAllClassResponseList.add(modelMapper.map(attendanceList, listType));

        });
        return attendanceAllClassResponseList;
    }
}