package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.dto.ActivityDto;
import fpt.project.bsmart.entity.request.ActivityRequest;
import fpt.project.bsmart.entity.request.SubmitAssignmentRequest;
import fpt.project.bsmart.entity.request.activity.MentorCreateSectionForCourse;

import java.io.IOException;
import java.util.List;

public interface IActivityService {
    Boolean addActivity(ActivityRequest activityRequest) throws IOException;

    List<Long> mentorCreateSectionForCourse(Long id, List<MentorCreateSectionForCourse> sessions);

    Boolean deleteActivity(Long id);

    Boolean changeActivityVisible(Long id);

    //    Boolean editActivity(Long id, ActivityRequest activityRequest) throws IOException;
    ActivityDto getDetailActivity(Long id);

    Boolean submitAssignment(Long id, SubmitAssignmentRequest request);


}
