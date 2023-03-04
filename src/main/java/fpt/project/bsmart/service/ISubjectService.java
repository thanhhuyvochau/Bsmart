package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.Subject;
import fpt.project.bsmart.entity.request.subject.SubjectRequest;

import java.util.List;

public interface ISubjectService {
    List<Subject> FindAll();
    Subject FindSubjectById(Long id);
    Subject FindSubjectByName(String name);
    Long AddSubject(SubjectRequest subjectRequest);
    Long UpdateSubject(Long id, SubjectRequest subjectRequest);
    void DeleteSubject(Long id);
}
