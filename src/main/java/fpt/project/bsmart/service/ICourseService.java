package fpt.project.bsmart.service;



import fpt.project.bsmart.entity.common.ApiPage;

import fpt.project.bsmart.entity.dto.CourseDto;
import fpt.project.bsmart.entity.request.CreateCourseRequest;
import fpt.project.bsmart.entity.response.CourseResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICourseService {

    List<CourseDto> getCoursesBySubject(Long id);
    Long mentorCreateCourse(CreateCourseRequest createCourseRequest);

    ApiPage<CourseDto> mentorGetCourse(Pageable pageable);

    ApiPage<CourseResponse> getCourseForCoursePage( Pageable pageable);

    CourseDto getDetailCourseForCoursePage(Long id);

//    Boolean mentorUploadImageForCourse(Long id, FileDto request);
}
