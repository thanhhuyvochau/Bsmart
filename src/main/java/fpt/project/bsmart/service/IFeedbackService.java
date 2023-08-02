package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.feedback.FeedbackTemplateDto;
import fpt.project.bsmart.entity.request.FeedbackTemplateRequest;
import fpt.project.bsmart.entity.request.FeedbackTemplateSearchRequest;
import fpt.project.bsmart.entity.request.StudentSubmitFeedbackRequest;
import fpt.project.bsmart.entity.response.FeedbackSubmissionResponse;
import fpt.project.bsmart.entity.response.FeedbackResponse;
import org.springframework.data.domain.Pageable;

public interface IFeedbackService {
    Long createFeedbackTemplate(FeedbackTemplateRequest request);
    Long updateFeedbackTemplate(Long id,FeedbackTemplateRequest request);
    Boolean deleteFeedbackTemplate(Long id);
    ApiPage<FeedbackTemplateDto> getAll(FeedbackTemplateSearchRequest request, Pageable pageable);
    FeedbackTemplateDto getTemplateById(Long id);
    Boolean assignFeedbackTemplateForClass(Long templateId, Long classId);
    Boolean changeDefaultTemplate(Long id);
    Long studentSubmitFeedback(Long classId, StudentSubmitFeedbackRequest request);
    Long studentUpdateFeedback(Long submissionId, StudentSubmitFeedbackRequest request);
    ApiPage<FeedbackSubmissionResponse> getClassFeedback(Long clazzId, Pageable pageable);
    FeedbackResponse getCourseFeedback(Long courseId);
    FeedbackResponse getMentorFeedback(Long mentorId);
}
