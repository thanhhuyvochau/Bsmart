package fpt.project.bsmart.service;


import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.CourseDto;
import fpt.project.bsmart.entity.request.CreateCourseRequest;
import org.springframework.data.domain.Pageable;

public interface ICourseService {

    Long mentorCreateCourse(CreateCourseRequest createCourseRequest);

    ApiPage<CourseDto> mentorGetCourse(Pageable pageable);
}
