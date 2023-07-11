package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.request.EditClassTimeTableRequest;
import fpt.project.bsmart.entity.request.timetable.GenerateScheduleRequest;
import fpt.project.bsmart.entity.request.timetable.GenerateScheduleResponse;
import fpt.project.bsmart.entity.response.TimeTableResponse;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

public interface ITimeTableService {
    List<GenerateScheduleResponse> generateScheduleForClass(GenerateScheduleRequest request);
//    Boolean editTimeTable(EditClassTimeTableRequest request);
//
//    ApiPage<TimeTableResponse> getTimeTableByClass(Long id, Pageable pageable);

}
