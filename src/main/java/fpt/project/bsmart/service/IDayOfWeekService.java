package fpt.project.bsmart.service;


import fpt.project.bsmart.entity.dto.DayOfWeekDTO;
import fpt.project.bsmart.entity.request.CreateCourseRequest;

import java.util.List;

public interface IDayOfWeekService {
    List<DayOfWeekDTO> getAllDaysOfWeek();

    DayOfWeekDTO getDayOfWeekById(Long id);

    DayOfWeekDTO createDayOfWeek(DayOfWeekDTO request);

    DayOfWeekDTO updateDayOfWeek(Long id, DayOfWeekDTO request);

    void deleteDayOfWeek(Long id);
}
