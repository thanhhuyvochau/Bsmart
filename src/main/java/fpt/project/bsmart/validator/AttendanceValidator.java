package fpt.project.bsmart.validator;

import fpt.project.bsmart.entity.request.AttendanceDetailRequest;
import fpt.project.bsmart.util.TimeUtil;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class AttendanceValidator {


    public static boolean isEnableTimeForDoAttendance(Instant attendanceDate) {
        Instant now = Instant.now().truncatedTo(ChronoUnit.HOURS);
        Instant truncatedAttendanceDate = attendanceDate.truncatedTo(ChronoUnit.HOURS);
        return !now.isBefore(truncatedAttendanceDate);
    }

    public static boolean isDuplicateAttendanceDetail(List<AttendanceDetailRequest> attendanceDetailRequestList) {
        List<Long> distinctList = attendanceDetailRequestList.stream().map(AttendanceDetailRequest::getStudentClassId).distinct().collect(Collectors.toList());
        return distinctList.size() < attendanceDetailRequestList.size();
    }
}
