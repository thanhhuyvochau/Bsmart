package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.request.StudentFeedbackRequest;
import fpt.project.bsmart.entity.response.StudentFeedbackResponse;

import java.util.List;

public interface IFeedbackService {


    List<StudentFeedbackResponse> studentFeedbackTeacher(List<StudentFeedbackRequest> studentFeedbackRequest);

    List<Long> studentGetTeacherNeededFeedback();
}
