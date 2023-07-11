package fpt.project.bsmart.service;


import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.ActivityDto;
import fpt.project.bsmart.entity.request.CourseSearchRequest;
import fpt.project.bsmart.entity.request.CreateCourseRequest;
import fpt.project.bsmart.entity.response.CourseResponse;
import fpt.project.bsmart.entity.response.course.ManagerGetCourse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICourseService {


    ApiPage<CourseResponse> getCourseForCoursePage(CourseSearchRequest courseSearchRequest , Pageable pageable);

    Long mentorCreateCourse(CreateCourseRequest mentorCreateClassRequest);

    Long  mentorUpdateCourse(Long id  ,CreateCourseRequest createCourseRequest);

    ApiPage<CourseResponse> getCourseOfMentor(CourseSearchRequest query, Pageable pageable);

    Boolean mentorDeleteCourse(Long id);

    ApiPage<ManagerGetCourse> coursePendingToApprove(Pageable pageable);

    List<ActivityDto> getAllActivityByCourseId(Long id);
//
//    List<CourseDto> getCoursesBySubject(Long subjectId);
//    List<Long> mentorCreateCoursePrivate(MentorCreateClassRequest mentorCreateClassRequest);
//

//    ApiPage<CourseSubCourseResponse> mentorGetAllCourse(ECourseStatus status  , Pageable pageable);



//    CourseSubCourseDetailResponse getDetailCourseForCoursePage(Long subCourseId);
//
//    Boolean mentorUploadImageCourse(ImageRequest imageRequest);
//
//    Boolean memberRegisterCourse(Long id);
//
//    ApiPage<SubCourseDetailResponse> getAllSubCourseOfCourse(Long id,  Pageable pageable);
//
//    ApiPage<CourseSubCourseResponse>  memberGetCourse(ECourseStatus status,Pageable pageable);
//
//    ApiPage<CourseSubCourseResponse> memberGetCourseSuggest( Pageable pageable);
//
//    Boolean mentorUpdateCourse(Long subCourseId,  UpdateSubCourseRequest updateCourseRequest );
//
//    Boolean mentorDeleteCourse(Long subCourseId);
//
//    ApiPage<CourseDto> getCoursePublic(Pageable pageable);
//
//    Boolean mentorRequestApprovalCourse(Long subCourseId);
//
//    ApiPage<CourseSubCourseResponse> coursePendingToApprove(ECourseStatus status, Pageable pageable);
//
//    Boolean managerApprovalCourseRequest(Long subCourseId, ManagerApprovalCourseRequest approvalCourseRequest);
//
//    Boolean managerCreateCourse(CreateCoursePublicRequest createCourseRequest);
//
//
//    CourseSubCourseResponse mentorGetCourse( Long subCourseId);
//
//    Boolean mentorCreateContentCourse( Long id ,List<CourseContentDto>  request) throws JsonProcessingException;
}
