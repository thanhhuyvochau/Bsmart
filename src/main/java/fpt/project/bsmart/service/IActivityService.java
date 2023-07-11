package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.constant.ECourseActivityType;
import fpt.project.bsmart.entity.dto.ActivityDetailDto;
import fpt.project.bsmart.entity.dto.ActivityDto;
import fpt.project.bsmart.entity.request.ActivityRequest;
import fpt.project.bsmart.entity.request.SubmitAssignmentRequest;
import fpt.project.bsmart.entity.request.activity.MentorCreateSectionForCourse;
import fpt.project.bsmart.entity.response.Avtivity.MentorDeleteSectionForCourse;
import fpt.project.bsmart.entity.response.Avtivity.MentorGetSectionForCourse;
import fpt.project.bsmart.entity.response.Avtivity.MentorUpdateSectionForCourse;

import java.io.IOException;
import java.util.List;

public interface IActivityService {
    Boolean addActivity(ActivityRequest activityRequest, ECourseActivityType type) throws IOException;


    List<Long> mentorCreateSectionForCourse(Long id, MentorCreateSectionForCourse sessions );


    Boolean deleteActivity(Long id);

    Boolean changeActivityVisible(Long id);

    //    Boolean editActivity(Long id, ActivityRequest activityRequest) throws IOException;
    ActivityDetailDto getDetailActivity(Long id);

    Boolean submitAssignment(Long id, SubmitAssignmentRequest request) throws IOException;


    List<MentorGetSectionForCourse> mentorGetSectionOfCourse(Long id);

    Boolean mentorUpdateSectionForCourse(Long id, MentorUpdateSectionForCourse updateRequest);

    Boolean mentorDeleteSectionForCourse(Long id, List<MentorDeleteSectionForCourse> deleteRequest);
}
