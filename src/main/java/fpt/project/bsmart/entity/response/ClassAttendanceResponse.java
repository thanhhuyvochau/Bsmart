package fpt.project.bsmart.entity.response;


import fpt.project.bsmart.entity.dto.AttendanceDto;

import java.util.List;

public class ClassAttendanceResponse {


    private Long accountId;
    private Long classId ;
    private List<AttendanceDto> attendance;


    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public List<AttendanceDto> getAttendance() {
        return attendance;
    }

    public void setAttendance(List<AttendanceDto> attendance) {
        this.attendance = attendance;
    }
}
