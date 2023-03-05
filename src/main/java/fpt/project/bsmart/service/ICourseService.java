package fpt.project.bsmart.service;


import fpt.project.bsmart.entity.request.CreateCourseRequest;

public interface ICourseService {

    Long mentorCreateCourse(CreateCourseRequest createCourseRequest);
}
