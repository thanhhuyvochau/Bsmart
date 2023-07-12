package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ValidationErrorsException;
import fpt.project.bsmart.entity.request.timetable.GenerateScheduleRequest;
import fpt.project.bsmart.entity.request.timetable.MentorCreateScheduleRequest;
import fpt.project.bsmart.entity.response.timetable.GenerateScheduleResponse;

import java.util.List;

public interface ITimeTableService {
    List<GenerateScheduleResponse> generateScheduleForClass(GenerateScheduleRequest request);

    Boolean mentorCreateScheduleForClass(Long classId , List<MentorCreateScheduleRequest> request) throws ValidationErrorsException;
//    Boolean editTimeTable(EditClassTimeTableRequest request);
//
//    ApiPage<TimeTableResponse> getTimeTableByClass(Long id, Pageable pageable);

}
