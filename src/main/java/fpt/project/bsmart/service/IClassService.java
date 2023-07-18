package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ValidationErrorsException;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.request.clazz.MentorCreateClass;
import fpt.project.bsmart.entity.response.*;
import fpt.project.bsmart.entity.response.Class.ManagerGetCourseClassResponse;

import fpt.project.bsmart.entity.request.ClassFilterRequest;
import fpt.project.bsmart.entity.request.MentorCreateClassRequest;
import fpt.project.bsmart.entity.request.clazz.MentorCreateClass;
import fpt.project.bsmart.entity.response.Class.BaseClassResponse;
import fpt.project.bsmart.entity.response.Class.ManagerGetClassDetailResponse;

import fpt.project.bsmart.entity.response.Class.MentorGetClassDetailResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClassService {
    List<String> mentorCreateCoursePrivate(MentorCreateClassRequest mentorCreateClassRequest);

    MentorGetCourseClassResponse getAllClassOfCourse(Long id);

    Long mentorCreateClassForCourse( Long id  ,MentorCreateClass mentorCreateClassRequest) throws ValidationErrorsException;

    Boolean mentorUpdateClassForCourse(Long id,  MentorCreateClass mentorCreateClassRequest);

    ApiPage<MentorGetClassDetailResponse> mentorGetClassOfCourse(Long id, Pageable pageable);

    Boolean mentorDeleteClassForCourse(Long id);


    ManagerGetCourseClassResponse getAllClassOfCourseForManager(Long id);


    ApiPage<BaseClassResponse> getAllClassesForManager(Pageable pageable);
    ManagerGetClassDetailResponse managerGetClassDetail(Long id);


//    Boolean createClass(CreateClassRequest request);
//
//    ApiPage<SimpleClassResponse> getClassFeedbacks(ClassFeedbackRequest classFeedbackRequest, Pageable pageable);
//
//    ClassProgressTimeDto getClassProgression(Long clazzId);
//
    ClassResponse getDetailClass(Long id);

    ApiPage<SimpleClassResponse> getUserClasses(ClassFilterRequest request, Pageable pageable);

    List<WorkTimeResponse> getWorkingTime();
}
