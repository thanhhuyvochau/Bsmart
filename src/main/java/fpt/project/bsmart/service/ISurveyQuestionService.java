package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.dto.SurveyQuestionAnswerDto;
import fpt.project.bsmart.entity.request.StudentSurveyRequest;
import fpt.project.bsmart.entity.request.SurveyQuestionRequest;

import java.util.List;

public interface ISurveyQuestionService {

    Boolean adminCreateSurveyQuestion(List<SurveyQuestionRequest> surveyQuestionRequest);

    Boolean studentSubmitSurvey( Long studentId,List<StudentSurveyRequest> studentSurveyRequests);

    List<SurveyQuestionAnswerDto> listSurveyQuestion();
}
