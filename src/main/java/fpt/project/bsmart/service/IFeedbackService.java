package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.dto.FeedbackTemplateDto;
import fpt.project.bsmart.entity.request.feedback.AddFeedbackTemplateRequest;
import fpt.project.bsmart.entity.request.feedback.AddQuestionRequest;
import fpt.project.bsmart.entity.request.feedback.SubCourseFeedbackRequest;
import fpt.project.bsmart.entity.response.UserFeedbackResponse;

import java.util.List;

public interface IFeedbackService {
    Long addNewQuestion(AddQuestionRequest addQuestionRequest);
    Long addNewFeedbackTemplate(AddFeedbackTemplateRequest addFeedbackTemplateRequest);
    FeedbackTemplateDto getFeedbackTemplateById(Long id);
    Long addNewSubCourseFeedback(SubCourseFeedbackRequest subCourseFeedbackRequest);
    List<UserFeedbackResponse> getFeedbackByClass(Long id);
}
