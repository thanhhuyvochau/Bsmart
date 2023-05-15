package fpt.project.bsmart.service;


import fpt.project.bsmart.entity.request.SubCourseTimeRequest;

public interface ITimeInWeekService {

//    List<TimeInWeekDTO> getAllTimeInWeeks(Long clazzId);
    Boolean createTimeInWeek(SubCourseTimeRequest request);
//    TimeInWeekDTO updateTimeInWeek(Long id, TimeInWeekRequest request);
//    boolean deleteTimeInWeek(Long id);
}
