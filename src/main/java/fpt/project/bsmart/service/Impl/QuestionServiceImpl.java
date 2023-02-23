package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.EAccountRole;
import fpt.project.bsmart.entity.common.EForumType;
import fpt.project.bsmart.entity.dto.QuestionDto;
import fpt.project.bsmart.entity.dto.QuestionSimpleDto;
import fpt.project.bsmart.entity.request.CreateQuestionRequest;
import fpt.project.bsmart.entity.request.VoteRequest;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.IQuestionService;
import fpt.project.bsmart.util.*;
import fpt.project.bsmart.util.specification.QuestionSpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements IQuestionService {
    private final QuestionRepository questionRepository;
    private final SubjectRepository subjectRepository;
    private final SecurityUtil securityUtil;
    private final ForumRepository forumRepository;
    private final ForumLessonRepository forumLessonRepository;
    private final VoteRepository voteRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository, SubjectRepository subjectRepository, SecurityUtil securityUtil, ForumRepository forumRepository, ForumLessonRepository forumLessonRepository, VoteRepository voteRepository) {
        this.questionRepository = questionRepository;
        this.subjectRepository = subjectRepository;
        this.securityUtil = securityUtil;
        this.forumRepository = forumRepository;
        this.forumLessonRepository = forumLessonRepository;
        this.voteRepository = voteRepository;
    }

    @Override
    public QuestionDto getQuestion(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Question not found by id:" + id));
        return ConvertUtil.doConvertEntityToResponse(question, securityUtil.getCurrentUserThrowNotFoundException());
    }

    @Override
    public QuestionDto createQuestion(CreateQuestionRequest createQuestionRequest) {
        Question question = ObjectUtil.copyProperties(createQuestionRequest, new Question(), Question.class, true);
        Account account = securityUtil.getCurrentUserThrowNotFoundException();

        Forum forum = forumRepository.findById(createQuestionRequest.getForumId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage("Subject not found with id:" + createQuestionRequest.getForumId()));
        if (createQuestionRequest.getTitle() == null || createQuestionRequest.getTitle().trim().isEmpty()) {
            throw ApiException.create(HttpStatus.CONFLICT)
                    .withMessage("Title can not be empty!");
        }
        if (!ForumUtil.isValidForumMember(forum, account)) {
            throw ApiException.create(HttpStatus.CONFLICT)
                    .withMessage("User not a valid member of this forum");
        }
        if (forum.getType().name().equals(EForumType.CLASS.name())) {
            ForumLesson forumLesson = forumLessonRepository.findById(createQuestionRequest
                    .getForumLessonId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                    .withMessage("Lesson not found with id:" + createQuestionRequest.getForumLessonId()));
            question.setForumLesson(forumLesson);
        }
        question.setTitle(createQuestionRequest.getTitle());
        question.setForum(forum);
        question.setStudent(account);
        questionRepository.save(question);
        return ConvertUtil.doConvertEntityToResponse(question, account);
    }

    @Override
    public QuestionDto updateQuestion(Long questionId, CreateQuestionRequest createQuestionRequest) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage("Question not found with id:" + questionId));
        Question newQuestion = ObjectUtil.copyProperties(createQuestionRequest, question, Question.class, true);
        Account account = securityUtil.getCurrentUserThrowNotFoundException();
        EAccountRole roleCode = account.getRole().getCode();
        boolean isValidToUpdate = false;
        if (account.getId().equals(question.getStudent().getId())) {
            isValidToUpdate = true;
        } else if (roleCode.equals(EAccountRole.MANAGER)) {
            isValidToUpdate = true;
        } else if (roleCode.equals(EAccountRole.TEACHER)) {
            isValidToUpdate = true;
        }
        if (isValidToUpdate) {
            Forum forum = forumRepository.findById(createQuestionRequest.getForumId())
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                            .withMessage("Forum not found with id:" + createQuestionRequest.getForumId()));
            newQuestion.setForum(forum);
            questionRepository.save(newQuestion);
            return ConvertUtil.doConvertEntityToResponse(newQuestion, account);
        } else {
            throw ApiException.create(HttpStatus.NOT_FOUND)
                    .withMessage("You not have the right to modify question!");
        }
    }

    @Override
    public Boolean closeQuestion(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Question not found by id:" + id));
        if (question.getClosed()) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Question has already closed!");
        } else {
            question.setClosed(true);
        }
        return true;
    }

    @Override
    public Boolean openQuestion(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Question not found by id:" + id));
        if (!question.getClosed()) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Question has already opened!");
        } else {
            question.setClosed(false);
        }
        return true;
    }

    private Boolean isEnrolledToSubject(Account account, Subject subject) {
        List<Class> enrolledClass = account.getStudentClasses().stream().map(StudentClass::getaClass).collect(Collectors.toList());
        Class classMatchSubject = enrolledClass.stream()
                .filter(aClass -> aClass.getCourse().getSubject() != null)
                .filter(aClass -> aClass.getCourse().getSubject().getId().equals(subject.getId()))
                .findFirst().orElseThrow(() -> ApiException.create(HttpStatus.CONFLICT).withMessage("Student not enrolled to this subject!!"));
        return true;
    }

    @Override
    public Boolean voteQuestion(VoteRequest request) {
        Account account = securityUtil.getCurrentUserThrowNotFoundException();
        Long questionId = request.getQuestionId();
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Question not found by id:" + questionId));
        Vote vote = voteRepository.findByQuestionAndAccount(question, account).orElse(new Vote());
        if (Objects.equals(request.getVote(), vote.getVote())) {
            question.getVotes().remove(vote);
            voteRepository.delete(vote);
        } else {
            vote.setQuestion(question);
            vote.setAccount(account);
            vote.setVote(request.getVote());
            voteRepository.save(vote);
        }
        return true;
    }

    @Override
    public ApiPage<QuestionSimpleDto> searchQuestion(Long forumId, String q, Pageable pageable) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage("Forum not found with id:" + forumId));
        Account account = securityUtil.getCurrentUserThrowNotFoundException();
        Boolean isValidForumForMember = ForumUtil.isValidForumMember(forum, account);
        if (isValidForumForMember) {
            QuestionSpecificationBuilder questionSpecificationBuilder = new QuestionSpecificationBuilder();
            questionSpecificationBuilder
                    .queryByTitle(q)
                    .queryByContent(q);
            Page<Question> questionPage = questionRepository.findAll(questionSpecificationBuilder.build(), pageable);
            return PageUtil.convert(questionPage.map(ConvertUtil::doConvertEntityToSimpleResponse));
        } else {
            throw ApiException.create(HttpStatus.METHOD_NOT_ALLOWED).withMessage("Forum is not valid for you!");
        }
    }
}
