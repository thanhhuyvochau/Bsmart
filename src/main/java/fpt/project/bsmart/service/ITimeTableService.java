package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.request.timetable.GenerateScheduleRequest;
import fpt.project.bsmart.entity.request.timetable.GenerateScheduleResponse;

import java.util.List;

public interface ITimeTableService {
    List<GenerateScheduleResponse> generateScheduleForClass(GenerateScheduleRequest request);
//    Boolean editTimeTable(EditClassTimeTableRequest request);
//
//    ApiPage<TimeTableResponse> getTimeTableByClass(Long id, Pageable pageable);

}
