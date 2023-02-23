package fpt.project.bsmart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.request.SubjectRequest;
import fpt.project.bsmart.entity.request.SubjectSearchRequest;
import fpt.project.bsmart.entity.response.SubjectResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ISubjectService {

    SubjectResponse createNewSubject(SubjectRequest subjectRequest) throws JsonProcessingException;

    SubjectResponse updateSubject(Long subjectId, SubjectRequest subjectRequest);

    Long deleteSubject(Long subjectId);

    SubjectResponse getSubject(Long subjectId);

    ApiPage<SubjectResponse> suggestSubjectForStudent(Long studentId, Pageable pageable);

    List<SubjectResponse> getAllWithoutPaging();

    ApiPage<SubjectResponse> getAllWithPaging(Pageable pageable);

    ApiPage<SubjectResponse> searchSubject(SubjectSearchRequest query, Pageable pageable);

    List<SubjectResponse> getSubjectOfTeacher();
}
