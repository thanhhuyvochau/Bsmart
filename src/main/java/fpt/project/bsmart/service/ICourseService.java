package fpt.project.bsmart.service;


import fpt.project.bsmart.entity.dto.CourseDto;
import fpt.project.bsmart.entity.request.CreateCourseRequest;

import java.util.List;

public interface ICourseService {

    List<CourseDto> getCoursesBySubject(Long id);
    Long mentorCreateCourse(CreateCourseRequest createCourseRequest);
}
