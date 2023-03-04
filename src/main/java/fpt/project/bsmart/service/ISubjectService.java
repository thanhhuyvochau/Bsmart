package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.Subject;
import fpt.project.bsmart.entity.dto.SubjectDTO;
import fpt.project.bsmart.entity.request.subject.SubjectRequest;

import java.util.List;

public interface ISubjectService {
    List<SubjectDTO> FindAll();
    SubjectDTO FindSubjectById(Long id);
    Long AddSubject(SubjectRequest subjectRequest);
    Long UpdateSubject(Long id, SubjectRequest subjectRequest);
    void DeleteSubject(Long id);
}
