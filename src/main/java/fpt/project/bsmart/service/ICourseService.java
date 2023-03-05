package fpt.project.bsmart.service;


import fpt.project.bsmart.entity.request.subject.SubjectRequest;

public interface ICourseService {

    Long mentorCreateCourse(SubjectRequest subjectRequest);
}
