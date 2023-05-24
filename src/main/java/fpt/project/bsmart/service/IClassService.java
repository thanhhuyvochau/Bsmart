package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.ClassProgressTimeDto;
import fpt.project.bsmart.entity.request.ClassFeedbackRequest;
import fpt.project.bsmart.entity.request.category.CreateClassRequest;
import fpt.project.bsmart.entity.response.ClassResponse;
import org.springframework.data.domain.Pageable;

public interface IClassService {

    Boolean createClass(CreateClassRequest request);
    ApiPage<ClassResponse> getClassFeedbacks(ClassFeedbackRequest classFeedbackRequest,Pageable pageable);
    ClassProgressTimeDto getClassProgression(Long clazzId);

}
