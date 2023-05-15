package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Attendance;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.TimeTable;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.response.AttendanceResponse;
import fpt.project.bsmart.repository.AttendanceRepository;
import fpt.project.bsmart.repository.ClassRepository;
import fpt.project.bsmart.service.AttendanceService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private  final AttendanceRepository attendanceRepository;

    private final ModelMapper modelMapper;

    private final ClassRepository classRepository;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository, ModelMapper modelMapper, ClassRepository classRepository) {
        this.attendanceRepository = attendanceRepository;
        this.modelMapper = modelMapper;
        this.classRepository = classRepository;
    }

    @Override
    public List<AttendanceResponse> getAllAttendance(long classId ) {
        Class aClass = classRepository.findById(classId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy lớp với id:" + classId));
        List<TimeTable> timeTables = aClass.getTimeTables();
        List<Attendance> attendanceList = new ArrayList<>( );
        for (TimeTable timeTable : timeTables) {
            attendanceList.addAll(timeTable.getAttendanceList());
        }
        Type listType = new TypeToken<List<AttendanceResponse>>() {}.getType();
        modelMapper.createTypeMap(Attendance.class, AttendanceResponse.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getTimeTable().getDate(), AttendanceResponse::setDate);
                    mapper.map(src -> src.getTimeTable().getClazz().getId(), AttendanceResponse::setClassId);
                    mapper.map(src -> src.getTimeTable().getSlot().getId(), AttendanceResponse::setSlotId);
                });

        return  modelMapper.map(attendanceList, listType);
    }
}