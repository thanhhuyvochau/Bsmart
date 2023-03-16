package fpt.project.bsmart.service;



import fpt.project.bsmart.entity.common.ApiPage;

import fpt.project.bsmart.entity.dto.CourseDto;
import fpt.project.bsmart.entity.request.CourseSearchRequest;
import fpt.project.bsmart.entity.request.CreateSubCourseRequest;
import fpt.project.bsmart.entity.request.ImageRequest;
import fpt.project.bsmart.entity.response.CourseDetailResponse;
import fpt.project.bsmart.entity.response.CourseSubCourseResponse;
import fpt.project.bsmart.entity.response.SubCourseDetailResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICourseService {

    List<CourseDto> getCoursesBySubject(Long id);
    Long mentorCreateCourse(CreateSubCourseRequest createSubCourseRequest);

    ApiPage<CourseDto> mentorGetCourse(Pageable pageable);

    ApiPage<CourseSubCourseResponse> getCourseForCoursePage(CourseSearchRequest courseSearchRequest , Pageable pageable);

    SubCourseDetailResponse getDetailCourseForCoursePage(Long subCourseId);

    Boolean mentorUploadImageCourse(ImageRequest imageRequest);

    Boolean memberRegisterCourse(Long id);

//    Boolean mentorUploadImageForCourse(Long id, FileDto request);
}
