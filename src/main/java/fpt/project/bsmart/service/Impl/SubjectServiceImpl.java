package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.dto.SubjectDto;
import fpt.project.bsmart.entity.request.SubjectRequest;
import fpt.project.bsmart.repository.CategoryRepository;
import fpt.project.bsmart.repository.MentorProfileRepository;
import fpt.project.bsmart.repository.SubjectRepository;
import fpt.project.bsmart.service.ISubjectService;
import fpt.project.bsmart.util.MessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.CATEGORY_NOT_FOUND_BY_ID;
import static fpt.project.bsmart.util.Constants.ErrorMessage.SUBJECT_NOT_FOUND_BY_ID;
import static fpt.project.bsmart.util.ConvertUtil.convertSubjectToSubjectDto;
import static fpt.project.bsmart.util.MentorUtil.checkIsMentor;


@Service
public class SubjectServiceImpl implements ISubjectService {
    private final SubjectRepository subjectRepository;
    private final CategoryRepository categoryRepository;

    private final MessageUtil messageUtil;

    private final MentorProfileRepository mentorProfileRepository;


    public SubjectServiceImpl(SubjectRepository subjectRepository, CategoryRepository categoryRepository, MessageUtil messageUtil, MentorProfileRepository mentorProfileRepository) {
        this.subjectRepository = subjectRepository;
        this.categoryRepository = categoryRepository;
        this.messageUtil = messageUtil;
        this.mentorProfileRepository = mentorProfileRepository;
    }


    private Subject findById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(SUBJECT_NOT_FOUND_BY_ID) + id));
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(CATEGORY_NOT_FOUND_BY_ID) + id));
    }

    private List<Category> findCategoryByIds(List<Long> ids) {
        return categoryRepository.findAllById(ids);
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
    public List<SubjectDto> getSubjectsByCategory(Long id) {
        Category category = findCategoryById(id);
        Set<Subject> subjectList = category.getSubjects();
        List<SubjectDto> subjectDtoList = new ArrayList<>();
        if (!subjectList.isEmpty()) {
            for (Subject subject : subjectList) {
                subjectDtoList.add(convertSubjectToSubjectDto(subject));
            }
        }
        return subjectDtoList;
    }

    @Override
    public SubjectDto getSubject(Long id) {
        Subject subject = findById(id);
        return convertSubjectToSubjectDto(subject);
    }

    @Override
    public Long createSubject(SubjectRequest subjectRequest) {
        List<Category> categories = findCategoryByIds(subjectRequest.getCategoryIds());
        Subject subject = new Subject();
        subject.setName(subjectRequest.getName());
        subject.setCode(subjectRequest.getCode());
        for (Category category : categories) {
            category.addSubject(subject);
        }
        return subjectRepository.save(subject).getId();
    }

    @Override
    public Long updateSubject(Long id, SubjectRequest subjectRequest) {
        List<Category> categories = findCategoryByIds(subjectRequest.getCategoryIds());
        Subject subject = findById(id);
        subject.setName(subjectRequest.getName());
        subject.setCode(subjectRequest.getCode());
        for (Category category : categories) {
            category.addSubject(subject);
        }
        return subjectRepository.save(subject).getId();
    }

    public Long deleteSubject(Long id) {
        Subject subject = findById(id);
        subjectRepository.delete(subject);
        return id;
    }

    @Override
    public List<SubjectDto> getSubjectsByMentorSkill() {
        User user = checkIsMentor();
        MentorProfile mentorProfile = user.getMentorProfile();
        List<SubjectDto> subjectDtoList = new ArrayList<>();
        List<MentorSkill> allSkills = mentorProfile.getSkills();
        List<MentorSkill> skills = allSkills.stream().filter(MentorSkill::getStatus).collect(Collectors.toList());
        List<Subject> skillList = skills.stream().map(MentorSkill::getSkill).collect(Collectors.toList());
        skillList.forEach(subject -> {
                subjectDtoList.add(convertSubjectToSubjectDto(subject));
        });
        return subjectDtoList;
    }
}
