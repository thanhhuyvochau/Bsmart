package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.EForumType;
import fpt.project.bsmart.entity.dto.ForumDto;
import fpt.project.bsmart.entity.dto.SimpleForumDto;
import fpt.project.bsmart.entity.request.UpdateForumRequest;
import fpt.project.bsmart.moodle.response.MoodleSectionResponse;
import fpt.project.bsmart.repository.ClassRepository;
import fpt.project.bsmart.repository.ForumRepository;
import fpt.project.bsmart.repository.SubjectRepository;
import fpt.project.bsmart.service.IForumService;
import fpt.project.bsmart.util.ConvertUtil;
import fpt.project.bsmart.util.ForumUtil;
import fpt.project.bsmart.util.PageUtil;
import fpt.project.bsmart.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ForumServiceImpl implements IForumService {


    private final ForumRepository forumRepository;
    private final ClassRepository classRepository;
    private final SubjectRepository subjectRepository;
    private final SecurityUtil securityUtil;

    public ForumServiceImpl(ForumRepository forumRepository, ClassRepository classRepository, SubjectRepository subjectRepository, SecurityUtil securityUtil) {
        this.forumRepository = forumRepository;
        this.classRepository = classRepository;
        this.subjectRepository = subjectRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    public ForumDto createForumForClass(Long classId, MoodleSectionResponse moodleSectionResponse) {
        Class clazz = classRepository.findById(classId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Class not found with id:" + classId));
        Forum existedForum = forumRepository.findForumByClazz(clazz).orElse(null);
        if (existedForum != null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("A class at most one forum");
        }

        Forum forum = new Forum();
        forum.setName(clazz.getName());
        forum.setCode(clazz.getCode());
        forum.setType(EForumType.CLASS);

        List<ForumLesson> forumLessons = moodleSectionResponse.getModules().stream().filter(resourceMoodleResponse -> resourceMoodleResponse.getModname().equals("lesson")).map(resourceMoodleResponse -> {
            ForumLesson forumLesson = new ForumLesson();
            forumLesson.setForum(forum);
            forumLesson.setLessonName(resourceMoodleResponse.getName());
            return forumLesson;
        }).collect(Collectors.toList());

        forum.setForumLessons(forumLessons);
        Forum savedForum = forumRepository.save(forum);
        return ConvertUtil.doConvertEntityToResponse(savedForum);
    }

    @Override
    public ForumDto createForumForSubject(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Subject not found with id:" + subjectId));

        Forum existedForum = forumRepository.findForumBySubject(subject).orElse(null);
        if (existedForum != null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("A subject at most one forum");
        }

        Forum forum = new Forum();
        forum.setName(subject.getName());
        forum.setCode(subject.getCode().name());
        forum.setType(EForumType.SUBJECT);
        forum.setSubject(subject);
        forumRepository.save(forum);
        return ConvertUtil.doConvertEntityToResponse(forum);
    }

    @Override
    public ForumDto updateForum(Long id, UpdateForumRequest request) {
        Forum forum = forumRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Forum not found with id:" + id));
        String forumName = request.getName();
        if (forumName != null && !forumName.trim().isEmpty()) {
            forum.setName(forumName);
        }
        String forumCode = request.getCode();
        if (forumCode != null && !forumCode.trim().isEmpty()) {
            forum.setCode(forumCode);
        }
        return ConvertUtil.doConvertEntityToResponse(forum);
    }

    @Override
    public ForumDto getForumByClass(Long classId) {
        Account account = securityUtil.getCurrentUserThrowNotFoundException();
        Class clazz = classRepository.findById(classId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Class not found with id:" + classId));
        Forum forum = clazz.getForum();
        if (forum == null) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Forum of this class not found, contact admin for help!");
        }
        String roleCode = account.getRole().getCode().name();
        switch (roleCode) {
            case "ADMIN":
                return ConvertUtil.doConvertEntityToResponse(forum);
            case "TEACHER":
                Account teacher = clazz.getAccount();
                if (!teacher.getId().equals(account.getId())) {
                    throw ApiException.create(HttpStatus.CONFLICT).withMessage("This forum subject invalid for you!");
                }
                return ConvertUtil.doConvertEntityToResponse(forum);
            case "STUDENT":
                if (!ForumUtil.isValidClassForStudent(account, clazz)) {
                    throw ApiException.create(HttpStatus.CONFLICT).withMessage("This forum subject invalid for you!");
                }
                return ConvertUtil.doConvertEntityToResponse(forum);
        }
        return null;
    }

    @Override
    public ForumDto getForumBySubject(Long subjectId,String q) {
        Account account = securityUtil.getCurrentUserThrowNotFoundException();
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Subject not found with id:" + subjectId));
        Forum forum = subject.getForum();
        if (forum == null) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Forum of this subject not found, contact admin for help!");
        }

        String roleCode = account.getRole().getCode().name();
        switch (roleCode) {
            case "ADMIN":
                return ConvertUtil.doConvertEntityToResponse(forum);
            case "TEACHER":
                if (!ForumUtil.isValidSubjectForTeacher(account, subject)) {
                    throw ApiException.create(HttpStatus.CONFLICT).withMessage("This forum subject invalid for you!");
                }
                return ConvertUtil.doConvertEntityToResponse(forum);
            case "STUDENT":
                if (!ForumUtil.isValidSubjectForStudent(account, subject)) {
                    throw ApiException.create(HttpStatus.CONFLICT).withMessage("This forum subject invalid for you!");
                }
                return ConvertUtil.doConvertEntityToResponse(forum);
        }
        return null;
    }


    @Override
    public ApiPage<SimpleForumDto> getAllForumByTypes(Pageable pageable, EForumType forumType) {
        if (forumType.name().equals(EForumType.CLASS.name())) {
            return getAllClassForums(pageable);
        } else if (forumType.name().equals(EForumType.SUBJECT.name())) {
            return getAllSubjectForums(pageable);
        }
        return null;
    }


    private ApiPage<SimpleForumDto> getAllSubjectForums(Pageable pageable) {
        Account account = securityUtil.getCurrentUserThrowNotFoundException();
        String roleCode = account.getRole().getCode().name();
        switch (roleCode) {
            case "STUDENT":
                return getSubjectForumsOfStudent(account, pageable);
            case "TEACHER":
                return getSubjectForumsOfTeacher(account, pageable);
            case "ADMIN":
                return getForumsForAdmin(EForumType.SUBJECT, pageable);
            default:
                throw ApiException.create(HttpStatus.METHOD_NOT_ALLOWED).withMessage("Invalid user to call this method!");
        }
    }


    private ApiPage<SimpleForumDto> getSubjectForumsOfStudent(Account account, Pageable pageable) {
        List<Subject> enrolledSubjects = account.getStudentClasses().stream()
                .map(StudentClass::getaClass)
                .map(Class::getCourse)
                .map(Course::getSubject)
                .distinct()
                .collect(Collectors.toList());
        Page<Forum> forums = forumRepository.findAllBySubjectIn(enrolledSubjects, pageable);
        return PageUtil.convert(forums.map(ConvertUtil::doConvertEntityToSimpleResponse));
    }

    private ApiPage<SimpleForumDto> getSubjectForumsOfTeacher(Account account, Pageable pageable) {
        AccountDetail accountDetail = account.getAccountDetail();
        if (accountDetail != null) {
            List<Subject> taughtSubject = accountDetail.getAccountDetailSubjects().stream()
                    .map(AccountDetailSubject::getSubject)
                    .collect(Collectors.toList());

            Page<Forum> forums = forumRepository.findAllBySubjectIn(taughtSubject, pageable);
            return PageUtil.convert(forums.map(ConvertUtil::doConvertEntityToSimpleResponse));
        }
        return PageUtil.convert(Page.empty());
    }

    private ApiPage<SimpleForumDto> getAllClassForums(Pageable pageable) {
        Account account = securityUtil.getCurrentUserThrowNotFoundException();
        String roleCode = account.getRole().getCode().name();
        switch (roleCode) {
            case "STUDENT":
                return getClassesForumsOfStudent(account, pageable);
            case "TEACHER":
                return getClassesForumsOfTeacher(account, pageable);
            case "ADMIN":
                return getForumsForAdmin(EForumType.CLASS, pageable);
            default:
                throw ApiException.create(HttpStatus.METHOD_NOT_ALLOWED).withMessage("Invalid user to call this method!");
        }
    }

    private ApiPage<SimpleForumDto> getClassesForumsOfStudent(Account student, Pageable pageable) {
        List<Class> enrolledClasses = student.getStudentClasses().stream()
                .map(StudentClass::getaClass).collect(Collectors.toList());
        Page<Forum> classForums = forumRepository.findAllByClazzIn(enrolledClasses, pageable);
        return PageUtil.convert(classForums.map(ConvertUtil::doConvertEntityToSimpleResponse));
    }

    private ApiPage<SimpleForumDto> getClassesForumsOfTeacher(Account account, Pageable pageable) {
        List<Class> teachedClass = account.getTeacherClass();
        Page<Forum> classForums = forumRepository.findAllByClazzIn(teachedClass, pageable);
        return PageUtil.convert(classForums.map(ConvertUtil::doConvertEntityToSimpleResponse));
    }

    private ApiPage<SimpleForumDto> getForumsForAdmin(EForumType forumType, Pageable pageable) {
        Page<Forum> forums = forumRepository.findAllByType(forumType, pageable);
        return PageUtil.convert(forums.map(ConvertUtil::doConvertEntityToSimpleResponse));
    }

    @Override
    public Boolean synchronizeLessonForum(Long classId) {
        Class clazz = classRepository.findById(classId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Class not found with id:" + classId));
        List<Section> sections = clazz.getSections();
        Forum classForum = clazz.getForum();
        for (Section section : sections) {
            ForumLesson forumLesson = Optional.ofNullable(section.getForumLesson()).orElse(new ForumLesson());
            forumLesson.setForum(classForum);
            forumLesson.setLessonName(section.getName());
            forumLesson.setSection(section);
            classForum.getForumLessons().add(forumLesson);
        }
        forumRepository.save(classForum);
        return true;
    }
}
