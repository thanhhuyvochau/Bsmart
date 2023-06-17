package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.ClassProgressTimeDto;
import fpt.project.bsmart.entity.dto.ClassSectionDto;
import fpt.project.bsmart.entity.request.ClassFeedbackRequest;
import fpt.project.bsmart.entity.request.ClassFilterRequest;
import fpt.project.bsmart.entity.request.ClassSectionCreateRequest;
import fpt.project.bsmart.entity.request.ClassSectionUpdateRequest;
import fpt.project.bsmart.entity.request.category.CreateClassRequest;
import fpt.project.bsmart.entity.response.ClassResponse;
import fpt.project.bsmart.entity.response.SimpleClassResponse;
import org.springframework.data.domain.Pageable;

public interface IClassService {

    Boolean createClass(CreateClassRequest request);

    ApiPage<SimpleClassResponse> getClassFeedbacks(ClassFeedbackRequest classFeedbackRequest, Pageable pageable);

    ClassProgressTimeDto getClassProgression(Long clazzId);

    ClassResponse getDetailClass(Long id);

    ApiPage<SimpleClassResponse> getUserClasses(ClassFilterRequest request, Pageable pageable);

    ClassSectionDto createClassSection(ClassSectionCreateRequest request, Long classId);

    ClassSectionDto getClassSection(Long classSectionId,Long classId);

    ClassSectionDto updateClassSection(Long classId, Long classSectionId, ClassSectionUpdateRequest request);

    Boolean deleteClassSection(Long classSectionId,Long classId);
}
