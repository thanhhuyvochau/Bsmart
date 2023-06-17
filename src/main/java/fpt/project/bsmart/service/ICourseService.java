package fpt.project.bsmart.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.SimpleResult;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.dto.CourseDto;
import fpt.project.bsmart.entity.dto.course.CourseContentDto;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.response.CourseResponse;
import fpt.project.bsmart.entity.response.CourseSubCourseDetailResponse;
import fpt.project.bsmart.entity.response.CourseSubCourseResponse;
import fpt.project.bsmart.entity.response.SubCourseDetailResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICourseService {

    List<CourseDto> getCoursesBySubject(Long subjectId);
    List<Long> mentorCreateCoursePrivate(CreateCourseRequest createCourseRequest);

    List<Long> mentorCreateCoursePublic(Long id  ,CreateCourseRequest createCourseRequest);
    ApiPage<CourseSubCourseResponse> mentorGetAllCourse(ECourseStatus status  , Pageable pageable);

    ApiPage<CourseResponse> getCourseForCoursePage(CourseSearchRequest courseSearchRequest , Pageable pageable);

    CourseSubCourseDetailResponse getDetailCourseForCoursePage(Long subCourseId);

    Boolean mentorUploadImageCourse(ImageRequest imageRequest);

    Boolean memberRegisterCourse(Long id);

    ApiPage<SubCourseDetailResponse> getAllSubCourseOfCourse(Long id,  Pageable pageable);

    ApiPage<CourseSubCourseResponse>  memberGetCourse(ECourseStatus status,Pageable pageable);

    ApiPage<CourseSubCourseResponse> memberGetCourseSuggest( Pageable pageable);

    Boolean mentorUpdateCourse(Long subCourseId,  UpdateSubCourseRequest updateCourseRequest );

    Boolean mentorDeleteCourse(Long subCourseId);

    ApiPage<CourseDto> getCoursePublic(Pageable pageable);

    Boolean mentorRequestApprovalCourse(Long subCourseId);

    ApiPage<CourseSubCourseResponse> coursePendingToApprove(ECourseStatus status, Pageable pageable);

    Boolean managerApprovalCourseRequest(Long subCourseId, ManagerApprovalCourseRequest approvalCourseRequest);

    Boolean managerCreateCourse(CreateCoursePublicRequest createCourseRequest);


    CourseSubCourseResponse mentorGetCourse( Long subCourseId);

    Boolean mentorCreateContentCourse( Long id ,List<CourseContentDto>  request) throws JsonProcessingException;
}
