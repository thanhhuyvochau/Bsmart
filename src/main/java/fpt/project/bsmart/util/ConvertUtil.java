package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.Module;
import fpt.project.bsmart.entity.VoteNumberReponse;
import fpt.project.bsmart.entity.common.EForumType;
import fpt.project.bsmart.entity.common.EGenderType;
import fpt.project.bsmart.entity.dto.*;
import fpt.project.bsmart.entity.response.*;
import fpt.project.bsmart.moodle.repository.MoodleCourseRepository;
import fpt.project.bsmart.moodle.request.GetMoodleCourseRequest;
import fpt.project.bsmart.moodle.response.MoodleModuleResponse;
import fpt.project.bsmart.moodle.response.MoodleRecourseDtoResponse;
import fpt.project.bsmart.moodle.response.MoodleResourceResponse;
import fpt.project.bsmart.moodle.response.MoodleSectionResponse;
import fpt.project.bsmart.repository.ClassLevelRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class ConvertUtil {


    private static ClassLevelRepository staticClassLevelRepository;

    private static MoodleCourseRepository staticMoodleCourseRepository;

    public ConvertUtil(ClassLevelRepository classLevelRepository, MoodleCourseRepository moodleCourseRepository) {

        staticClassLevelRepository = classLevelRepository;
        staticMoodleCourseRepository = moodleCourseRepository;
    }


    public static QuestionDto doConvertEntityToResponse(Question question, Account account) {
        QuestionDto questionDto = ObjectUtil.copyProperties(question, new QuestionDto(), QuestionDto.class, true);
        AccountSimpleResponse accountResponse = doConvertEntityToSimpleResponse(question.getStudent());
        // filter(comment -> comment.getParentComment() == null)
        // -> Để lấy tất cả các comment trực tiếp của câu hỏi (Không hỏi comment con) mục đích để get được ra dạng cây của các comment
        // -> Nếu không filter thì sẽ trả ra tất cả comment của câu hỏi và không handle ra dạng cây được.
        List<CommentDto> comments = question.getComments().stream().filter(comment -> comment.getParentComment() == null)
                .map(comment -> doConvertEntityToResponse(comment, account))
                .collect(Collectors.toList());
        questionDto.setVoteNumberReponse(VoteUtil.getVoteResponse(question));
        questionDto.setUser(accountResponse);
        questionDto.setComments(comments);
        Integer userState = VoteUtil.getUserState(question, account);
        questionDto.setUserState(userState);
        return questionDto;
    }

    public static QuestionSimpleDto doConvertEntityToSimpleResponse(Question question) {
        QuestionSimpleDto questionSimpleDto = ObjectUtil.copyProperties(question, new QuestionSimpleDto(), QuestionSimpleDto.class, true);
        AccountSimpleResponse accountResponse = doConvertEntityToSimpleResponse(question.getStudent());
        VoteNumberReponse voteNumberResponse = VoteUtil.getVoteResponse(question);
        questionSimpleDto.setVoteNumber(voteNumberResponse);
        questionSimpleDto.setUser(accountResponse);

        return questionSimpleDto;
    }

    public static CommentDto doConvertEntityToResponse(Comment comment, Account currentUser) {
        return doConvertEntityToResponseAsTree(comment, comment.getParentComment(), currentUser);
    }

    private static CommentDto doConvertEntityToResponseAsTree(Comment comment, Comment parentComment, Account currentUser) {
        CommentDto commentDto = ObjectUtil.copyProperties(comment, new CommentDto(), CommentDto.class, true);
        VoteNumberReponse voteResponse = VoteUtil.getVoteResponse(comment);
        commentDto.setVoteNumber(voteResponse);
        commentDto.setUserState(VoteUtil.getUserState(comment, currentUser));
        AccountSimpleResponse accountResponse = doConvertEntityToSimpleResponse(comment.getAccount());
        commentDto.setUser(accountResponse);
        if (parentComment != null) {
            CommentDto parentCommentDto = ObjectUtil.copyProperties(parentComment, new CommentDto(), CommentDto.class, true);
            commentDto.setParentComment(parentCommentDto);
        }
        if (!comment.getSubComments().isEmpty()) {
            List<CommentDto> subCommentDtos = comment.getSubComments().stream()
                    .map(subComment -> doConvertEntityToResponseAsTree(subComment, comment, currentUser))
                    .collect(Collectors.toList());
            commentDto.setSubComments(subCommentDtos);
        }
        return commentDto;
    }

    public static AccountResponse doConvertEntityToResponse(Account account) {

        AccountResponse accountResponse = new AccountResponse();
        if (account != null) {
            accountResponse.setUsername(account.getUsername());
            accountResponse.setId(account.getId());

            RoleDto roleDto = doConvertEntityToResponse(account.getRole());
            accountResponse.setRole(roleDto);
            if (account.getResource() != null) {
                accountResponse.setAvatar(account.getResource().getUrl());
            }

            AccountDetail accountDetail = account.getAccountDetail();
            if (accountDetail != null) {
                EGenderType gender = accountDetail.getGender();
                if (gender != null) {
                    GenderResponse genderResponse = doConvertEntityToResponse(gender);
                    accountResponse.setGender(genderResponse);
                }

                accountResponse.setPhoneNumber(accountDetail.getPhone());
                accountResponse.setBirthday(accountDetail.getBirthDay());
                accountResponse.setLastName(accountDetail.getLastName());
                accountResponse.setFirstName(accountDetail.getFirstName());
                accountResponse.setCurrentAddress(accountDetail.getCurrentAddress());
                accountResponse.setIdCard(accountDetail.getIdCard());
                accountResponse.setLevel(accountDetail.getLevel());
                accountResponse.setSchoolName(accountDetail.getTrainingSchoolName());
                accountResponse.setMajors(accountDetail.getMajors());
                accountResponse.setActive(accountDetail.getActive());
                accountResponse.setVoice(doConvertVoiceToResponse(account.getAccountDetail().getVoice()));
                accountResponse.setStatus(accountDetail.getStatus());
                accountResponse.setEmail(accountDetail.getEmail());


            }
        }


        return accountResponse;
    }

    public static GenderResponse doConvertEntityToResponse(EGenderType gender) {
        GenderResponse genderResponse = new GenderResponse();
        genderResponse.setCode(gender.name());
        genderResponse.setName(gender.getLabel());

        return genderResponse;
    }

    public static AccountSimpleResponse doConvertEntityToSimpleResponse(Account account) {

        AccountSimpleResponse response = new AccountSimpleResponse();
        response.setId(account.getId());
        RoleDto roleDto = doConvertEntityToResponse(account.getRole());
        response.setRole(roleDto);
        if (account.getResource() != null) {
            response.setAvatar(account.getResource().getUrl());
        }

        AccountDetail accountDetail = account.getAccountDetail();
        if (accountDetail != null) {
            EGenderType gender = accountDetail.getGender();
            if (gender != null) {
                GenderResponse genderResponse = new GenderResponse();
                genderResponse.setCode(gender.name());
                genderResponse.setName(gender.getLabel());
                response.setGender(genderResponse);
            }
            response.setLastName(accountDetail.getLastName());
            response.setFirstName(accountDetail.getFirstName());
            response.setLevel(accountDetail.getLevel());
            response.setStatus(accountDetail.getStatus());

        }

        return response;
    }

    public static RoleDto doConvertEntityToResponse(Role role) {
        return ObjectUtil.copyProperties(role, new RoleDto(), RoleDto.class, true);
    }

    public static SubjectResponse doConvertEntityToResponse(Subject subject) {
        SubjectResponse subjectResponse = new SubjectResponse();
        subjectResponse.setId(subject.getId());
        subjectResponse.setCode(subject.getCode());
        subjectResponse.setName(subject.getName());
        List<Long> idCourse = subject.getCourses().stream().map(Course::getId).collect(Collectors.toList());
        subjectResponse.setCourseIds(idCourse);
        return subjectResponse;
    }

    public static SubjectSimpleResponse doConvertEntityToSimpleResponse(Subject subject) {
        SubjectSimpleResponse response = new SubjectSimpleResponse();
        response.setId(subject.getId());
        response.setCode(subject.getCode());
        response.setName(subject.getName());
        return response;
    }

    public static RequestFormResponese doConvertEntityToResponse(Request request) {
        RequestFormResponese requestFormResponese = ObjectUtil.copyProperties(request, new RequestFormResponese(), RequestFormResponese.class);
        requestFormResponese.setRequestType(ObjectUtil.copyProperties(request.getRequestType(), new RequestTypeDto(), RequestTypeDto.class));
        return requestFormResponese;
    }

    public static FeedbackAccountLogResponse doConvertEntityToResponse(FeedbackAccountLog feedbackAccountLog) {
        FeedbackAccountLogResponse response = new FeedbackAccountLogResponse();
        response.setId(feedbackAccountLog.getId());
        if (feedbackAccountLog.getAccount() != null) {
            response.setAccount(feedbackAccountLog.getAccount().getId());
        }
        if (feedbackAccountLog.getAccountDetail() != null) {
            response.setAccountDetail(feedbackAccountLog.getAccountDetail().getId());
        }
        response.setStatus(feedbackAccountLog.getStatus());
        response.setContent(feedbackAccountLog.getContent());
        response.setCreateDate(feedbackAccountLog.getLastModified());
        return response;
    }

    public static CourseDetailResponse doConvertEntityToResponse(Course course) {

        return ObjectUtil.copyProperties(course, new CourseDetailResponse(), CourseDetailResponse.class);
    }

    public static CourseResponse doConvertCourseToCourseResponse(Course course) {
        if (course == null) {
            return null;
        }
        CourseResponse courseResponse = ObjectUtil.copyProperties(course, new CourseResponse(), CourseResponse.class);
        courseResponse.setCourseName(course.getName());
        if (course.getResource() != null) {
            courseResponse.setImage(course.getResource().getUrl());
        }
        if (course.getSubject() != null) {
            courseResponse.setSubject(ConvertUtil.doConvertEntityToSimpleResponse(course.getSubject()));
        }
        courseResponse.setCourseTitle(course.getTitle());

//        courseDetailResponse.setGrade(course.getGrade());
        return courseResponse;
    }


    public static ForumDto doConvertEntityToResponse(Forum forum) {
        ForumDto forumDto = ObjectUtil.copyProperties(forum, new ForumDto(), ForumDto.class, true);
        if (forum.getType().name().equals(EForumType.CLASS.name())) {
            ClassDto classDto = doConvertEntityToResponse(forum.getaClazz());
            forumDto.setaClass(classDto);
            List<ForumLessonDto> lessonDtos = forum.getForumLessons().stream().map(ConvertUtil::doConvertEntityToResponse)
                    .collect(Collectors.toList());
            forumDto.setForumLessonDtos(lessonDtos);
        } else {
            List<QuestionSimpleDto> questionDtos = forum.getQuestions().stream().map(ConvertUtil::doConvertEntityToSimpleResponse).collect(Collectors.toList());
            forumDto.setQuestions(questionDtos);
            SubjectSimpleResponse subject = doConvertEntityToSimpleResponse(forum.getSubject());
            forumDto.setSubject(subject);
        }
        return forumDto;
    }

    public static SimpleForumDto doConvertEntityToSimpleResponse(Forum forum) {
        SimpleForumDto forumDto = ObjectUtil.copyProperties(forum, new SimpleForumDto(), SimpleForumDto.class, true);
        if (forum.getType().name().equals(EForumType.CLASS.name())) {
            Class clazz = forum.getaClazz();
            forumDto.setClassName(clazz.getName());
            forumDto.setClassCode(clazz.getCode());
            forumDto.setClassId(clazz.getId());
        } else {
            Subject subject = forum.getSubject();
            forumDto.setSubjectCode(subject.getCode().name());
            forumDto.setSubjectName(subject.getName());
            forumDto.setSubjectId(subject.getId());
        }
        return forumDto;
    }

    public static ClassDto doConvertEntityToResponse(Class clazz) {
        ClassDto classDto = ObjectUtil.copyProperties(clazz, new ClassDto(), ClassDto.class, true);

        CourseResponse courseResponse = ConvertUtil.doConvertCourseToCourseResponse(clazz.getCourse());
        classDto.setCourse(courseResponse);

        ClassLevel classLevel = clazz.getClassLevel();
        if (classLevel != null) {
            classDto.setClassLevel(classLevel.getCode());
        }

        if (clazz.getAccount() != null) {
            Account teacher = clazz.getAccount();
            AccountSimpleResponse accountResponse1 = doConvertEntityToSimpleResponse(teacher);
            classDto.setTeacher(accountResponse1);
        }
        List<SectionDto> resources = clazz.getSections().stream().map(ConvertUtil::doConvertEntityToResponse).collect(Collectors.toList());
        classDto.setResources(resources);

        TimeTable timeTable = clazz.getTimeTables().stream().findFirst().orElse(null);
        if (timeTable != null) {
            Archetype archetype = timeTable.getArchetypeTime().getArchetype();
            ArchetypeDto archetypeDto = doConvertEntityToResponse(archetype);
            classDto.setArchetype(archetypeDto);
        }

        return classDto;
    }

    public static ForumLessonDto doConvertEntityToResponse(ForumLesson forumLesson) {
        ForumLessonDto forumLessonDto = ObjectUtil.copyProperties(forumLesson, new ForumLessonDto(), ForumLessonDto.class, true);
        List<QuestionSimpleDto> questionDtos = forumLesson.getQuestions().stream().map(ConvertUtil::doConvertEntityToSimpleResponse).collect(Collectors.toList());
        forumLessonDto.setQuestions(questionDtos);
        return forumLessonDto;
    }

    public static CandicateResponse doConvertEntityToResponse(ClassTeacherCandicate classTeacherCandicate) {
        CandicateResponse response = new CandicateResponse();
        AccountResponse teacher = doConvertEntityToResponse(classTeacherCandicate.getTeacher());
        response.setTeacher(teacher);
        response.setStatus(classTeacherCandicate.getStatus());
        return response;
    }


    public static List<MoodleRecourseDtoResponse> doConvertExercise(Class aClass) {
        List<MoodleRecourseDtoResponse> exercise = new ArrayList<>();

        if (aClass.getMoodleClassId() != null) {
            GetMoodleCourseRequest getMoodleCourseRequest = new GetMoodleCourseRequest();

            getMoodleCourseRequest.setCourseid(aClass.getMoodleClassId());
            try {
                List<MoodleSectionResponse> resourceCourse = staticMoodleCourseRepository.getResourceCourse(getMoodleCourseRequest);
                resourceCourse.stream().skip(1).forEach(moodleRecourseClassResponse -> {

                    List<MoodleModuleResponse> modules = moodleRecourseClassResponse.getModules();
                    List<Boolean> isAssignList = modules.stream().map(moodleModuleResponse -> moodleModuleResponse.getModname().equals("quiz")
                            || moodleModuleResponse.getModname().equals("assign")
                    ).collect(Collectors.toList());
                    isAssignList.forEach(isAssign -> {
                        if (isAssign) {
                            MoodleRecourseDtoResponse recourseDtoResponse = new MoodleRecourseDtoResponse();
                            recourseDtoResponse.setId(moodleRecourseClassResponse.getId());
                            recourseDtoResponse.setName(moodleRecourseClassResponse.getName());
                            List<MoodleResourceResponse> moodleResourceResponseList = new ArrayList<>();
                            modules.forEach(moodleResponse -> {

                                if (moodleResponse.getModname().equals("quiz") || moodleResponse.getModname().equals("assign")) {
                                    MoodleResourceResponse moodleResourceResponse = new MoodleResourceResponse();
                                    moodleResourceResponse.setId(moodleResponse.getId());
                                    moodleResourceResponse.setUrl(moodleResponse.getUrl());
                                    moodleResourceResponse.setInstance(moodleResponse.getInstance());
                                    moodleResourceResponse.setName(moodleResponse.getName());
                                    moodleResourceResponse.setType(moodleResponse.getModname());
                                    moodleResourceResponseList.add(moodleResourceResponse);
                                }
                            });
                            recourseDtoResponse.setModules(moodleResourceResponseList);
                            exercise.add(recourseDtoResponse);
                        }
                    });

                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return exercise;
    }

    public static ModuleDto doConvertEntityToResponse(Module module) {
        return ObjectUtil.copyProperties(module, new ModuleDto(), ModuleDto.class, true);
    }

    public static SectionDto doConvertEntityToResponse(Section section) {
        SectionDto sectionDto = ObjectUtil.copyProperties(section, new SectionDto(), SectionDto.class, true);
        List<ModuleDto> moduleDtos = section.getModules().stream().map(ConvertUtil::doConvertEntityToResponse).collect(Collectors.toList());
        sectionDto.setModules(moduleDtos);
        return sectionDto;
    }

    public static ArchetypeDto doConvertEntityToResponse(Archetype archetype) {
        ArchetypeDto archetypeDto = ObjectUtil.copyProperties(archetype, new ArchetypeDto(), ArchetypeDto.class, true);
        List<ArchetypeTimeDto> archetypeTimeDtos = archetype.getArchetypeTimes().stream().map(ConvertUtil::doConvertEntityToResponse).collect(Collectors.toList());
        archetypeDto.setArchetypeTimes(archetypeTimeDtos);
        return archetypeDto;
    }

    public static ArchetypeTimeDto doConvertEntityToResponse(ArchetypeTime archetypeTime) {
        ArchetypeTimeDto archetypeTimeDto = ObjectUtil.copyProperties(archetypeTime, new ArchetypeTimeDto(), ArchetypeTimeDto.class, true);
        archetypeTimeDto.setDayOfWeek(doConvertEntityToResponse(archetypeTime.getDayOfWeek()));
        archetypeTimeDto.setSlot(doConvertEntityToResponse(archetypeTime.getSlot()));
        return archetypeTimeDto;
    }

    public static SlotDto doConvertEntityToResponse(Slot slot) {
        return ObjectUtil.copyProperties(slot, new SlotDto(), SlotDto.class, true);
    }

    public static DayOfWeekDto doConvertEntityToResponse(DayOfWeek dayOfWeek) {
        return ObjectUtil.copyProperties(dayOfWeek, new DayOfWeekDto(), DayOfWeekDto.class, true);
    }

    public static MarkDto doConvertEntityToResponse(Mark mark) {
        MarkDto markDto = ObjectUtil.copyProperties(mark, new MarkDto(), MarkDto.class, true);
        AccountSimpleResponse student = doConvertEntityToSimpleResponse(mark.getStudent().getAccount());
        markDto.setStudent(student);
        return markDto;
    }

    public static List<MarkResponse> doConvertEntityToListResponse(List<Mark> marks) {
        List<MarkResponse> responses = new ArrayList<>();
        for (Mark mark : marks) {
            MarkDto markDto = doConvertEntityToResponse(mark);
            ModuleDto moduleDto = doConvertEntityToResponse(mark.getModule());
            MarkResponse markResponse = new MarkResponse();
            markResponse.setMarkDto(markDto);
            markResponse.setModule(moduleDto);
            responses.add(markResponse);
        }
        return responses;
    }

    public static RequestFormResponse convertRequestToRequestResponse(Request request) {
        RequestFormResponse response = new RequestFormResponse();
        if (request.getAccount() != null) {
            AccountResponse accountResponse = ConvertUtil.doConvertEntityToResponse(request.getAccount());
            response.setStudent(accountResponse);
        }
        response.setId(request.getId());
        response.setReason(request.getReason());
        response.setUrl(request.getUrl());
        response.setTitle(request.getTitle());
        response.setStatus(request.getStatus());
        response.setCreated(request.getLastModified());

        response.setRequestType(ObjectUtil.copyProperties(request.getRequestType(), new RequestTypeDto(), RequestTypeDto.class));
        return response;
    }

    public static VoiceResponse doConvertVoiceToResponse(Voice voice) {
        return ObjectUtil.copyProperties(voice, new VoiceResponse(), VoiceResponse.class, true);
    }


    public static AccountDetailResponse doConvertEntityToResponse(AccountDetail accountDetail) {
        Account account = accountDetail.getAccount();
        AccountDetailResponse accountDetailResponse = ObjectUtil.copyProperties(accountDetail, new AccountDetailResponse(), AccountDetailResponse.class);
        accountDetailResponse.setUserName(account.getUsername());
        List<AccountDetailSubject> accountDetailSubjects = accountDetail.getAccountDetailSubjects();
        List<SubjectDto> subjects = new ArrayList<>();
        accountDetailSubjects.forEach(accountDetailSubject -> {
            subjects.add(ObjectUtil.copyProperties(accountDetailSubject.getSubject(), new SubjectDto(), SubjectDto.class));
        });
        accountDetailResponse.setSubjects(subjects);
        accountDetailResponse.setStatus(accountDetail.getStatus());
        List<Resource> resources = accountDetail.getResources();

        List<ResourceDto> resourceDtoList = new ArrayList<>();
        resources.forEach(resource -> {
            resourceDtoList.add(ObjectUtil.copyProperties(resource, new ResourceDto(), ResourceDto.class));
        });

        accountDetailResponse.setResources(resourceDtoList);

        EGenderType gender = accountDetail.getGender();
        if (gender != null) {
            GenderResponse genderResponse = ConvertUtil.doConvertEntityToResponse(gender);
            accountDetailResponse.setGender(genderResponse);

        }
        Voice voice = accountDetail.getVoice();
        if (voice != null) {
            VoiceResponse voiceResponse = ConvertUtil.doConvertVoiceToResponse(voice);
            accountDetailResponse.setVoice(voiceResponse);
        }

        List<AccountDetailClassLevel> accountDetailClassLevels = accountDetail.getAccountDetailClassLevels();
        if (!accountDetailClassLevels.isEmpty()) {
            List<ClassLevelResponse> classLevelResponses = accountDetailClassLevels.stream()
                    .map(accountDetailClassLevel -> doConvertEntityToResponse(accountDetailClassLevel.getClassLevel()))
                    .collect(Collectors.toList());

            accountDetailResponse.getClassLevel().addAll((classLevelResponses));
        }
        accountDetailResponse.setAccountId(account.getId());

        return accountDetailResponse;
    }

    public static ClassLevelResponse doConvertEntityToResponse(ClassLevel classLevel) {
        return ObjectUtil.copyProperties(classLevel, new ClassLevelResponse(), ClassLevelResponse.class, true);
    }

}