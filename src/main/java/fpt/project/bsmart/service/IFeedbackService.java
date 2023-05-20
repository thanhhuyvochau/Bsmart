package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.dto.FeedbackQuestionDto;
import fpt.project.bsmart.entity.dto.FeedbackTemplateDto;
import fpt.project.bsmart.entity.request.UpdateSubCourseFeedbackTemplateRequest;
import fpt.project.bsmart.entity.request.feedback.FeedbackTemplateRequest;
import fpt.project.bsmart.entity.request.feedback.AddFeedbackQuestionRequest;
import fpt.project.bsmart.entity.request.feedback.SubCourseFeedbackRequest;
import fpt.project.bsmart.entity.response.UserFeedbackResponse;

import java.util.List;

public interface IFeedbackService {
    List<FeedbackQuestionDto> getAllFeedbackQuestions();
    Long addNewQuestion(AddFeedbackQuestionRequest addFeedbackQuestionRequest);
    Long addNewFeedbackTemplate(FeedbackTemplateRequest feedbackTemplateRequest);
    FeedbackTemplateDto getFeedbackTemplateById(Long id);
    Long updateFeedbackTemplateToSubCourse(UpdateSubCourseFeedbackTemplateRequest updateSubCourseFeedbackTemplateRequest);
    Long addNewSubCourseFeedback(SubCourseFeedbackRequest subCourseFeedbackRequest);
    List<UserFeedbackResponse> getFeedbackByClass(Long id);


}


