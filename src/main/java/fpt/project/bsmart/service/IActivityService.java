package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.constant.ECourseActivityType;
import fpt.project.bsmart.entity.dto.ActivityDetailDto;
import fpt.project.bsmart.entity.dto.QuizDto;
import fpt.project.bsmart.entity.dto.QuizSubmittionDto;
import fpt.project.bsmart.entity.request.ActivityRequest;
import fpt.project.bsmart.entity.request.StudentAttemptQuizRequest;
import fpt.project.bsmart.entity.request.SubmitAssignmentRequest;
import fpt.project.bsmart.entity.request.SubmitQuizRequest;
import fpt.project.bsmart.entity.request.activity.MentorCreateSectionForCourse;
import fpt.project.bsmart.entity.response.Avtivity.MentorDeleteSectionForCourse;
import fpt.project.bsmart.entity.response.Avtivity.MentorGetSectionForCourse;
import fpt.project.bsmart.entity.response.Avtivity.MentorUpdateSectionForCourse;

import java.io.IOException;
import java.util.List;

public interface IActivityService {
    Boolean addActivity(ActivityRequest activityRequest, ECourseActivityType type) throws IOException;


    List<Long> mentorCreateSectionForCourse(Long id, MentorCreateSectionForCourse sessions);


    Boolean deleteActivity(Long id);

    Boolean changeActivityVisible(Long id);

    //    Boolean editActivity(Long id, ActivityRequest activityRequest) throws IOException;
    ActivityDetailDto getDetailActivity(Long id);

    Boolean submitAssignment(Long id, SubmitAssignmentRequest request) throws IOException;

    QuizDto studentAttemptQuiz(Long id, StudentAttemptQuizRequest request);
    Boolean studentSubmitQuiz(Long activityId, SubmitQuizRequest request);
    QuizSubmittionDto studentReviewQuiz(Long id);
    List<MentorGetSectionForCourse> mentorGetSectionOfCourse(Long id);

    Boolean mentorUpdateSectionForCourse(Long id, MentorUpdateSectionForCourse updateRequest);

    Boolean mentorDeleteSectionForCourse(Long id, List<MentorDeleteSectionForCourse> deleteRequest);

    Boolean editActivity(Long id, ActivityRequest activityRequest, ECourseActivityType type) throws IOException;
}
