package fpt.project.bsmart.service;


import fpt.project.bsmart.entity.dto.TimeInWeekDTO;
import fpt.project.bsmart.entity.request.CreateCourseRequest;
import fpt.project.bsmart.entity.request.TimeInWeekRequest;

import java.util.List;

public interface ITimeInWeekService {

    List<TimeInWeekDTO> getAllTimeInWeeks(Long clazzId);
    TimeInWeekDTO createTimeInWeek(TimeInWeekRequest request);
    TimeInWeekDTO updateTimeInWeek(Long id, TimeInWeekRequest request);
    boolean deleteTimeInWeek(Long id);
}
