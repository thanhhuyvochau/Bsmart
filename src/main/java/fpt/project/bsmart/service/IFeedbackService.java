package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.dto.FeedbackQuestionDto;
import fpt.project.bsmart.entity.dto.FeedbackTemplateDto;
import fpt.project.bsmart.entity.request.feedback.FeedbackTemplateRequest;
import fpt.project.bsmart.entity.request.feedback.FeedbackQuestionRequest;
import fpt.project.bsmart.entity.request.feedback.SubCourseFeedbackRequest;
import fpt.project.bsmart.entity.response.UserFeedbackResponse;

import java.util.List;

public interface IFeedbackService {
    List<FeedbackQuestionDto> getAllFeedbackQuestions();
    FeedbackQuestionDto getFeedbackQuestionById(Long id);
    Long addNewQuestion(FeedbackQuestionRequest feedbackQuestionRequest);
    Long updateFeedbackQuestion(Long id, FeedbackQuestionRequest request);
    Long deleteFeedbackQuestion(Long id);
    Long addNewFeedbackTemplate(FeedbackTemplateRequest feedbackTemplateRequest);
    List<FeedbackTemplateDto> getAllFeedbackTemplate();
    FeedbackTemplateDto getFeedbackTemplateById(Long id);
    Long updateFeedbackTemplate(Long id, FeedbackTemplateRequest request);
    Long updateFeedbackTemplateToSubCourse(Long templateId, Long subCourseId);
    Long addNewSubCourseFeedback(SubCourseFeedbackRequest subCourseFeedbackRequest);
    List<UserFeedbackResponse> getFeedbackByClass(Long id);


}


