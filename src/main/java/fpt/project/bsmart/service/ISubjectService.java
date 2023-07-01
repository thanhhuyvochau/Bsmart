package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.dto.SubjectDto;
import fpt.project.bsmart.entity.request.SubjectRequest;

import java.util.List;

public interface ISubjectService {
    List<SubjectDto> getAllSubject();

    List<SubjectDto> getSubjectsByCategory(Long id);
    SubjectDto getSubject(Long id);

    Long createSubject(SubjectRequest subjectRequest);

    Long updateSubject(Long id, SubjectRequest subjectRequest);

    Long deleteSubject(Long id);

    List<SubjectDto> getSubjectsByMentorSkill();
}
