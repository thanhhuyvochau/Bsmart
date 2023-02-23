package fpt.project.bsmart.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.TimeTableDto;
import fpt.project.bsmart.entity.request.TimeTableRequest;
import fpt.project.bsmart.entity.request.TimeTableSearchRequest;
import fpt.project.bsmart.entity.response.ClassAttendanceResponse;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.util.List;

public interface ITimeTableService {


    String createTimeTableClass(Long classId,Long numberSlot ,TimeTableRequest timeTableRequest ) throws ParseException, JsonProcessingException;

    ApiPage<TimeTableDto> getTimeTableInDay(TimeTableSearchRequest timeTableSearchRequest, Pageable pageable) ;

    List<ClassAttendanceResponse> accountGetAllTimeTable();

    Long adminCreateTimeTableClass(Long classId, Long numberSlot, TimeTableRequest timeTableRequest) throws ParseException;

    Long adminUpdateTimeTableClass(Long classId, Long numberSlot, TimeTableRequest timeTableRequest);
}
