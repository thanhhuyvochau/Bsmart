package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.feedback.FeedbackTemplateDto;
import fpt.project.bsmart.entity.request.FeedbackTemplateRequest;
import fpt.project.bsmart.entity.request.StudentSubmitFeedbackRequest;
import fpt.project.bsmart.entity.response.FeedbackSubmissionResponse;
import org.springframework.data.domain.Pageable;

public interface IFeedbackService {
    Long createFeedbackTemplate(FeedbackTemplateDto request);
    Long updateFeedbackTemplate(FeedbackTemplateDto request);
    Boolean deleteFeedbackTemplate(Long id);
    ApiPage<FeedbackTemplateDto> getAll(FeedbackTemplateRequest request, Pageable pageable);
    FeedbackTemplateDto getTemplateById(Long id);
    Boolean assignFeedbackTemplateForClass(Long templateId, Long classId);
    Boolean changeDefaultTemplate(Long id);
    Long studentSubmitFeedback(Long classId, StudentSubmitFeedbackRequest request);
    ApiPage<FeedbackSubmissionResponse> teacherViewClassFeedback(Long clazzId, Pageable pageable);
}
