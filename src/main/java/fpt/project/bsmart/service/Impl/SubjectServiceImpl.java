package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Category;
import fpt.project.bsmart.entity.Subject;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.dto.SubjectDto;
import fpt.project.bsmart.entity.request.SubjectRequest;
import fpt.project.bsmart.repository.CategoryRepository;
import fpt.project.bsmart.repository.SubjectRepository;
import fpt.project.bsmart.service.ISubjectService;
import fpt.project.bsmart.util.MessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static fpt.project.bsmart.util.Constants.ErrorMessage.CATEGORY_NOT_FOUND_BY_ID;
import static fpt.project.bsmart.util.Constants.ErrorMessage.SUBJECT_NOT_FOUND_BY_ID;
import static fpt.project.bsmart.util.ConvertUtil.convertSubjectToSubjectDto;


@Service
public class SubjectServiceImpl implements ISubjectService {
    private final SubjectRepository subjectRepository;
    private final CategoryRepository categoryRepository;

    private final MessageUtil messageUtil;

    public SubjectServiceImpl(SubjectRepository subjectRepository, CategoryRepository categoryRepository, MessageUtil messageUtil) {
        this.subjectRepository = subjectRepository;
        this.categoryRepository = categoryRepository;
        this.messageUtil = messageUtil;
    }


    private Subject findById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(SUBJECT_NOT_FOUND_BY_ID) + id));
    }

    @Override
    public List<SubjectDto> getAllSubject() {
        List<Subject> subjects = subjectRepository.findAll();
        List<SubjectDto> listSubjectDto = new ArrayList<>();
        for (Subject subject : subjects) {
            listSubjectDto.add(convertSubjectToSubjectDto(subject));
        }
        return listSubjectDto;
    }

    @Override
    public SubjectDto getSubject(Long id) {
        Subject subject = findById(id);
        return convertSubjectToSubjectDto(subject);
    }

    @Override
    public Long createSubject(SubjectRequest subjectRequest) {
        Category category = categoryRepository.findById(subjectRequest.getCategoryId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CATEGORY_NOT_FOUND_BY_ID) + subjectRequest.getCategoryId()));
        Subject subject = new Subject();
        subject.setName(subjectRequest.getName());
        subject.setCode(subjectRequest.getCode());
        subject.setCategory(category);
        return subjectRepository.save(subject).getId();
    }

    @Override
    public Long updateSubject(Long id, SubjectRequest subjectRequest) {
        Category category = categoryRepository.findById(subjectRequest.getCategoryId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CATEGORY_NOT_FOUND_BY_ID) + subjectRequest.getCategoryId()));
        Subject subject = findById(id);
        subject.setName(subjectRequest.getName());
        subject.setCode(subjectRequest.getCode());
        subject.setCategory(category);
        return subjectRepository.save(subject).getId();
    }

    public Long deleteSubject(Long id) {
        Subject subject = findById(id);

        subjectRepository.delete(subject);
        return id;
    }


}
