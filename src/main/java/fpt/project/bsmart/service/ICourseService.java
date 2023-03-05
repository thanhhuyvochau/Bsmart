package fpt.project.bsmart.service;


import fpt.project.bsmart.entity.request.CreateCourseRequest;
import fpt.project.bsmart.entity.request.SubjectRequest;

public interface ICourseService {

    Long mentorCreateCourse(CreateCourseRequest createCourseRequest);
}
