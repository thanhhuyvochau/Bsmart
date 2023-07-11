package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.request.MentorCreateClassRequest;
import fpt.project.bsmart.entity.request.clazz.MentorCreateClass;
import fpt.project.bsmart.entity.response.Class.MentorGetClassDetailResponse;
import fpt.project.bsmart.entity.response.ClassResponse;
import fpt.project.bsmart.entity.response.CourseClassResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClassService {
    List<String> mentorCreateCoursePrivate(MentorCreateClassRequest mentorCreateClassRequest);

    CourseClassResponse getAllClassOfCourse(Long id);

    Long mentorCreateClassForCourse( Long id  ,MentorCreateClass mentorCreateClassRequest);

    Boolean mentorUpdateClassForCourse(Long id,  MentorCreateClass mentorCreateClassRequest);

    ApiPage<MentorGetClassDetailResponse> mentorGetClassOfCourse(Long id, Pageable pageable);

    Boolean mentorDeleteClassForCourse(Long id);

    CourseClassResponse getAllClassOfCourseForManager(Long id);

//    Boolean createClass(CreateClassRequest request);
//
//    ApiPage<SimpleClassResponse> getClassFeedbacks(ClassFeedbackRequest classFeedbackRequest, Pageable pageable);
//
//    ClassProgressTimeDto getClassProgression(Long clazzId);
//
    ClassResponse getDetailClass(Long id);
//
//    ApiPage<SimpleClassResponse> getUserClasses(ClassFilterRequest request, Pageable pageable);
//
//    ClassSectionDto createClassSection(ClassSectionCreateRequest request, Long classId);
//
//    ClassSectionDto getClassSection(Long classSectionId,Long classId);
//
//    ClassSectionDto updateClassSection(Long classId, Long classSectionId, ClassSectionUpdateRequest request);
//
//    Boolean deleteClassSection(Long classSectionId,Long classId);


}
