package fpt.project.bsmart.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.EForumType;
import fpt.project.bsmart.entity.request.SubjectRequest;
import fpt.project.bsmart.entity.request.SubjectSearchRequest;
import fpt.project.bsmart.entity.response.SubjectResponse;
import fpt.project.bsmart.moodle.repository.MoodleCourseRepository;
import fpt.project.bsmart.moodle.request.CreateCategoryRequest;
import fpt.project.bsmart.moodle.request.GetCategoryRequest;
import fpt.project.bsmart.moodle.response.MoodleCategoryResponse;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.ISubjectService;
import fpt.project.bsmart.util.*;
import fpt.project.bsmart.util.specification.SubjectSpecificationBuilder;
import fpt.project.bsmart.util.specification.SuggestSubjectSpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubjectServiceImpl implements ISubjectService {

    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;

    private final AccountRepository accountRepository;

    private final MessageUtil messageUtil;

    private final StudentAnswerRepository studentAnswerRepository;

    private final MoodleCourseRepository moodleCourseRepository;

    private final SecurityUtil securityUtil;

    private final InfoFindTutorRepository infoFindTutorRepository;
    private final ForumRepository forumRepository;

    public SubjectServiceImpl(CourseRepository courseRepository, SubjectRepository subjectRepository, AccountRepository accountRepository, MessageUtil messageUtil, StudentAnswerRepository studentAnswerRepository, MoodleCourseRepository moodleCourseRepository, SecurityUtil securityUtil, InfoFindTutorRepository infoFindTutorRepository, ForumRepository forumRepository) {
        this.courseRepository = courseRepository;
        this.subjectRepository = subjectRepository;
        this.accountRepository = accountRepository;
        this.messageUtil = messageUtil;
        this.studentAnswerRepository = studentAnswerRepository;
        this.moodleCourseRepository = moodleCourseRepository;
        this.securityUtil = securityUtil;
        this.infoFindTutorRepository = infoFindTutorRepository;
        this.forumRepository = forumRepository;
    }

    @Override
    public SubjectResponse createNewSubject(SubjectRequest subjectRequest) throws JsonProcessingException {
        Subject subject = new Subject();

        if (subjectRepository.existsByCode(subjectRequest.getCode())) throw ApiException.create(HttpStatus.BAD_REQUEST)
                .withMessage(messageUtil.getLocalMessage("code subject   đã tòn tạo"));
        subject.setCode(subjectRequest.getCode());
        subject.setName(subjectRequest.getName());


        GetCategoryRequest getCategoryRequest = new GetCategoryRequest();
        List<MoodleCategoryResponse> categoryList = moodleCourseRepository.getCategories(getCategoryRequest);
        List<CreateCategoryRequest.CreateCategoryBody> moodleCreateCategoryRequestList = new ArrayList<>();


        List<String> stringList = new ArrayList<>();

        for (MoodleCategoryResponse moodleCategoryResponse : categoryList) {
            if (moodleCategoryResponse.getName().equals(subjectRequest.getCode().name())) {
                stringList.add(subjectRequest.getCode().name());
            }
        }

        if (stringList.isEmpty()) {

            CreateCategoryRequest request = new CreateCategoryRequest();
            CreateCategoryRequest.CreateCategoryBody createCategoryBody = new CreateCategoryRequest.CreateCategoryBody();
            createCategoryBody.setName(subjectRequest.getCode().name());
            moodleCreateCategoryRequestList.add(createCategoryBody);
            request.setCategories(moodleCreateCategoryRequestList);
            try {
                moodleCourseRepository.createCategory(request);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }


        Subject subjectSaved = subjectRepository.save(subject);
        SubjectResponse response = new SubjectResponse();
        response.setId(subjectSaved.getId());
        response.setName(subjectSaved.getName());
        response.setCode(subjectSaved.getCode());
        createSubjectForum(subjectSaved);
        return response;
    }

    @Override
    public SubjectResponse updateSubject(Long subjectId, SubjectRequest subjectRequest) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay subject"));
        subject.setCode(subjectRequest.getCode());
        subject.setName(subjectRequest.getName());
        Subject subjectSaved = subjectRepository.save(subject);
        SubjectResponse response = new SubjectResponse();
        response.setId(subjectSaved.getId());
        response.setName(subjectSaved.getName());
        response.setCode(subjectSaved.getCode());

        return response;
    }

    @Override
    public Long deleteSubject(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay subject"));
        subjectRepository.delete(subject);
        return subjectId;
    }

    @Override
    public SubjectResponse getSubject(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay subject"));
        SubjectResponse response = new SubjectResponse();
        response.setId(subject.getId());
        response.setName(subject.getName());
        response.setCode(subject.getCode());
//        response.setCourseIds(subject.getCourses());
        return response;
    }

    @Override
    public ApiPage<SubjectResponse> suggestSubjectForStudent(Long studentId, Pageable pageable) {
        Account account = accountRepository.findById(studentId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage("Khong tim thay account") + studentId));

        List<StudentAnswer> studentAnswers = account.getStudentAnswers();

        List<Answer> answer = studentAnswers.stream().map(StudentAnswer::getAnswer).collect(Collectors.toList());

        List<String> stringAnswer = answer.stream().map(Answer::getAnswer).collect(Collectors.toList());
        String queryString = String.join(" ", stringAnswer);


//        SuggestSubjectSpecificationBuilder builder = SuggestSubjectSpecificationBuilder.specification()
//                .querySubjectCode(queryString) ;


        Optional<String> first = answer.stream().map(Answer::getAnswer).findFirst();
        String s = "";
        if (first.isPresent()) {
            s = first.get();
        }
        SuggestSubjectSpecificationBuilder builder = SuggestSubjectSpecificationBuilder.specification()
                .querySubjectCode(s);


        Page<Subject> subjectPage = subjectRepository.findAll(builder.build(), pageable);

        return PageUtil.convert(subjectPage.map(ConvertUtil::doConvertEntityToResponse));
    }

    @Override
    public List<SubjectResponse> getAllWithoutPaging() {

        List<Subject> subjects = subjectRepository.findAll();
        return subjects.stream()
                .map(ConvertUtil::doConvertEntityToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public ApiPage<SubjectResponse> getAllWithPaging(Pageable pageable) {
        Page<Subject> allSubjects = subjectRepository.findAll(pageable);
        return PageUtil.convert(allSubjects.map(ConvertUtil::doConvertEntityToResponse));
    }

    @Override
    public ApiPage<SubjectResponse> searchSubject(SubjectSearchRequest query, Pageable pageable) {
        SubjectSpecificationBuilder builder = SubjectSpecificationBuilder.specification()
                .queryLike(query.getQ());

        Page<Subject> coursePage = subjectRepository.findAll(builder.build(), pageable);

        return PageUtil.convert(coursePage.map(ConvertUtil::doConvertEntityToResponse));

    }

    @Override
    public List<SubjectResponse> getSubjectOfTeacher() {
        List<SubjectResponse> subjectResponseList = new ArrayList<>();
        Account teacher = securityUtil.getCurrentUserThrowNotFoundException();
        AccountDetail accountDetail = teacher.getAccountDetail();

        List<Subject> subjectList = null;
        if (accountDetail != null) {
            subjectList = accountDetail.getAccountDetailSubjects().stream().map(AccountDetailSubject::getSubject).collect(Collectors.toList());
        }
        if (subjectList != null) {
            subjectList.forEach(subject -> {
                subjectResponseList.add(ObjectUtil.copyProperties(subject, new SubjectResponse(), SubjectResponse.class));

            });
        }
        return subjectResponseList;
    }

    private Boolean createSubjectForum(Subject subject) {
        Forum forum = new Forum();
        forum.setName(subject.getName());
        forum.setCode(subject.getCode().name());
        forum.setSubject(subject);
        forum.setType(EForumType.SUBJECT);
        forumRepository.save(forum);
        return true;
    }
}
