package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.dto.FeedbackTemplateDto;
import fpt.project.bsmart.entity.request.feedback.AddFeedbackTemplateRequest;
import fpt.project.bsmart.entity.request.feedback.AddQuestionRequest;
import fpt.project.bsmart.entity.request.feedback.FeedbackAnswerRequest;
import fpt.project.bsmart.entity.request.feedback.SubCourseFeedbackRequest;

public interface IFeedbackService {
    Long addNewQuestion(AddQuestionRequest addQuestionRequest);
    Long addNewFeedbackTemplate(AddFeedbackTemplateRequest addFeedbackTemplateRequest);
    FeedbackTemplateDto getFeedbackTemplateById(Long id);
    Long addNewSubCourseFeedback(SubCourseFeedbackRequest subCourseFeedbackRequest);
}
