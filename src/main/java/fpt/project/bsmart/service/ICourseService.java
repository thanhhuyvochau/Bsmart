package fpt.project.bsmart.service;


import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.constant.ECourseClassStatus;
import fpt.project.bsmart.entity.dto.ActivityDto;
import fpt.project.bsmart.entity.request.CourseSearchRequest;
import fpt.project.bsmart.entity.request.CreateCourseRequest;
import fpt.project.bsmart.entity.request.ManagerApprovalCourseRequest;
import fpt.project.bsmart.entity.response.CourseResponse;
import fpt.project.bsmart.entity.response.course.CompletenessCourseResponse;
import fpt.project.bsmart.entity.response.course.ManagerGetCourse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICourseService {


    ApiPage<CourseResponse> getCourseForCoursePage(CourseSearchRequest courseSearchRequest, Pageable pageable);

    ApiPage<CourseResponse> studentGetCurrentCourse(CourseSearchRequest request, Pageable pageable);

    Long mentorCreateCourse(CreateCourseRequest mentorCreateClassRequest);

    Long mentorUpdateCourse(Long id, CreateCourseRequest createCourseRequest);

    ApiPage<CourseResponse> getCourseOfMentor(CourseSearchRequest query, Pageable pageable);

    Boolean mentorDeleteCourse(Long id);



    List<ActivityDto> getAllActivityByCourseId(Long id);

    Boolean mentorRequestApprovalCourse(Long id, List<Long> classIds);

    CompletenessCourseResponse getCompletenessCourse(Long id);

    //     ################################## START MANAGER ##########################################

    ApiPage<ManagerGetCourse> coursePendingToApprove(ECourseClassStatus status , Pageable pageable);
    Boolean managerApprovalCourseRequest(Long id, ManagerApprovalCourseRequest approvalCourseRequest);

    Boolean managerBlockCourse(Long id);

    //     ################################## END MANAGER ##########################################
}
