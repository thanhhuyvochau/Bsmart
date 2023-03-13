package fpt.project.bsmart.service;



import fpt.project.bsmart.entity.common.ApiPage;

import fpt.project.bsmart.entity.dto.CourseDto;
import fpt.project.bsmart.entity.request.CourseSearchRequest;
import fpt.project.bsmart.entity.request.CreateCourseRequest;
import fpt.project.bsmart.entity.request.ImageRequest;
import fpt.project.bsmart.entity.response.CourseDetailResponse;
import fpt.project.bsmart.entity.response.CourseResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICourseService {

    List<CourseDto> getCoursesBySubject(Long id);
    Long mentorCreateCourse(CreateCourseRequest createCourseRequest);

    ApiPage<CourseDto> mentorGetCourse(Pageable pageable);

    ApiPage<CourseResponse> getCourseForCoursePage(CourseSearchRequest courseSearchRequest , Pageable pageable);

    CourseDetailResponse getDetailCourseForCoursePage(Long id);

    Boolean mentorUploadImageCourse(ImageRequest imageRequest);

//    Boolean mentorUploadImageForCourse(Long id, FileDto request);
}
