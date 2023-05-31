package fpt.project.bsmart.validator;

import fpt.project.bsmart.entity.request.AttendanceDetailRequest;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class AttendanceValidator {


    public static boolean isEnableTimeForDoAttendance(Instant attendanceDate) {
        Instant now = Instant.now();
        return now.equals(attendanceDate);
    }

    public static boolean isDuplicateAttendanceDetail(List<AttendanceDetailRequest> attendanceDetailRequestList) {
        List<Long> distinctList = attendanceDetailRequestList.stream().map(AttendanceDetailRequest::getStudentClassId).distinct().collect(Collectors.toList());
        return distinctList.size() < attendanceDetailRequestList.size();
    }
}
