package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ValidationErrorsException;
import fpt.project.bsmart.entity.constant.ECourseClassStatus;
import fpt.project.bsmart.entity.request.ClassFilterRequest;
import fpt.project.bsmart.entity.request.MentorCreateClassRequest;
import fpt.project.bsmart.entity.request.clazz.GetPointStudentClassRequest;
import fpt.project.bsmart.entity.request.clazz.MentorCreateClass;
import fpt.project.bsmart.entity.request.timetable.MentorCreateScheduleRequest;
import fpt.project.bsmart.entity.response.Class.BaseClassResponse;
import fpt.project.bsmart.entity.response.Class.ManagerGetClassDetailResponse;
import fpt.project.bsmart.entity.response.Class.ManagerGetCourseClassResponse;
import fpt.project.bsmart.entity.response.Class.MentorGetClassDetailResponse;
import fpt.project.bsmart.entity.response.*;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;

public interface IClassService {
    List<String> mentorCreateCoursePrivate(MentorCreateClassRequest mentorCreateClassRequest);

    MentorGetCourseClassResponse getAllClassOfCourse(Long id);

    Long mentorCreateClassForCourse(Long id, MentorCreateClass mentorCreateClassRequest) throws Exception;

    Boolean mentorUpdateClassForCourse(Long id, MentorCreateClass mentorCreateClassRequest);

    ApiPage<MentorGetClassDetailResponse> mentorGetClassOfCourse(Long id, Pageable pageable);

    Boolean mentorDeleteClassForCourse(Long id);


    ManagerGetCourseClassResponse getAllClassOfCourseForManager(Long id, ECourseClassStatus status);


    ApiPage<BaseClassResponse> getAllClassesForManager(Pageable pageable);

    ManagerGetClassDetailResponse managerGetClassDetail(Long id);


    //        Boolean createClass(CreateClassRequest request);
//
//    ApiPage<SimpleClassResponse> getClassFeedbacks(ClassFeedbackRequest classFeedbackRequest, Pageable pageable);
//
//    ClassProgressTimeDto getClassProgression(Long clazzId);
//
    ClassResponse getDetailClass(Long id);

    ApiPage<SimpleClassResponse> getUserClasses(ClassFilterRequest request, Pageable pageable);

    List<WorkTimeResponse> getWorkingTime();

    ApiPage<MentorGetClassDetailResponse> mentorGetClass(ECourseClassStatus status, Pageable pageable);

    Boolean mentorOpenClass(Long id, List<MentorCreateScheduleRequest> timeTableRequest) throws ValidationErrorsException;

    ApiPage<StudentClassResponse> getClassMembers(Long id, Pageable pageable);

    ApiPage<MentorGetClassDetailResponse> managerGetClass(ECourseClassStatus status, Pageable pageable);

    ApiPage<MentorGetClassDetailResponse> getAllClassForSetTemplateFeedback(Pageable pageable);

    List<BaseClassResponse> getDuplicateTimeClassOfStudent(Long id);

    HashMap<String, List<MentorGetClassDetailResponse>> getClassesNotUseTemplate(Long templateId);

    Boolean simulateCloseClassEvent(Long classId);

    List<GetPointStudentClassResponse> getStudentPoint(GetPointStudentClassRequest request);

    boolean setClassURL(Long id, String url);
}
