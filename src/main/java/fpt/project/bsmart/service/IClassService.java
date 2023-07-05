package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.ClassProgressTimeDto;
import fpt.project.bsmart.entity.dto.ClassSectionDto;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.request.category.CreateClassRequest;
import fpt.project.bsmart.entity.request.clazz.MentorCreateClass;
import fpt.project.bsmart.entity.response.ClassDetailResponse;
import fpt.project.bsmart.entity.response.ClassResponse;
import fpt.project.bsmart.entity.response.SimpleClassResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClassService {
    List<String> mentorCreateCoursePrivate(MentorCreateClassRequest mentorCreateClassRequest);

   ApiPage<ClassDetailResponse> getAllSubCourseOfCourse(Long id, Pageable pageable);

    List<Long> mentorCreateClassForCourse( Long id  ,List<MentorCreateClass> mentorCreateClassRequest);

//    Boolean createClass(CreateClassRequest request);
//
//    ApiPage<SimpleClassResponse> getClassFeedbacks(ClassFeedbackRequest classFeedbackRequest, Pageable pageable);
//
//    ClassProgressTimeDto getClassProgression(Long clazzId);
//
//    ClassResponse getDetailClass(Long id);
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
