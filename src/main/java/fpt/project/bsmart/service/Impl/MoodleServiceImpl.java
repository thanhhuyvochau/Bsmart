package fpt.project.bsmart.service.Impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.Module;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.EAccountRole;
import fpt.project.bsmart.moodle.repository.MoodleCourseRepository;
import fpt.project.bsmart.moodle.repository.MoodleRoleRepository;
import fpt.project.bsmart.moodle.repository.MoodleUserRepository;
import fpt.project.bsmart.moodle.request.*;
import fpt.project.bsmart.moodle.response.*;
import fpt.project.bsmart.repository.ClassRepository;
import fpt.project.bsmart.repository.FileAttachmentRepository;
import fpt.project.bsmart.repository.RoleRepository;
import fpt.project.bsmart.repository.SectionRepository;
import fpt.project.bsmart.service.IMoodleService;
import fpt.project.bsmart.util.MoodleUtil;
import fpt.project.bsmart.util.ObjectUtil;
import fpt.project.bsmart.util.PageUtil;
import fpt.project.bsmart.util.SecurityUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Transactional
public class MoodleServiceImpl implements IMoodleService {

    private final MoodleCourseRepository moodleCourseRepository;
    private final ClassRepository classRepository;
    private final SectionRepository sectionRepository;
    private final FileAttachmentRepository fileAttachmentRepository;
    private final MoodleRoleRepository moodleRoleRepository;
    private final RoleRepository roleRepository;

    private final MoodleUserRepository moodleUserRepository;
    private final MoodleUtil moodleUtil;
    private final SecurityUtil securityUtil;

    public MoodleServiceImpl(MoodleCourseRepository moodleCourseRepository, ClassRepository classRepository, SectionRepository sectionRepository, FileAttachmentRepository fileAttachmentRepository, MoodleRoleRepository moodleRoleRepository, RoleRepository roleRepository, MoodleUserRepository moodleUserRepository, MoodleUtil moodleUtil, SecurityUtil securityUtil) {
        this.moodleCourseRepository = moodleCourseRepository;
        this.classRepository = classRepository;
        this.sectionRepository = sectionRepository;
        this.fileAttachmentRepository = fileAttachmentRepository;
        this.moodleRoleRepository = moodleRoleRepository;
        this.roleRepository = roleRepository;
        this.moodleUserRepository = moodleUserRepository;
        this.moodleUtil = moodleUtil;
        this.securityUtil = securityUtil;
    }

    @Override
    public List<MoodleCategoryResponse> getCategoryFromMoodle() throws JsonProcessingException {
        GetCategoryRequest request = new GetCategoryRequest();
        List<GetCategoryRequest.MoodleCategoryBody> moodleCategoryBodyList = new ArrayList<>();
        GetCategoryRequest.MoodleCategoryBody moodleCategoryBody = new GetCategoryRequest.MoodleCategoryBody();
        moodleCategoryBody.setKey("id");
        moodleCategoryBody.setValue("");
        moodleCategoryBodyList.add(moodleCategoryBody);
        request.setCriteria(moodleCategoryBodyList);
        return moodleCourseRepository.getCategories(request);
    }

    @Override
    public Boolean createCategoryToMoodle(CreateCategoryRequest.CreateCategoryBody createCategoryBody) throws JsonProcessingException {
        CreateCategoryRequest request = new CreateCategoryRequest();

        List<CreateCategoryRequest.CreateCategoryBody> createCategoryBodyList = new ArrayList<>();
        CreateCategoryRequest.CreateCategoryBody set = new CreateCategoryRequest.CreateCategoryBody();
        set.setName(createCategoryBody.getName());
        set.setParent(createCategoryBody.getParent());
        set.setIdnumber(createCategoryBody.getIdnumber());
        set.setDescription(createCategoryBody.getDescription());
        set.setDescriptionformat(createCategoryBody.getDescriptionformat());
        createCategoryBodyList.add(set);

        request.setCategories(createCategoryBodyList);

        moodleCourseRepository.createCategory(request);
        return true;
    }

    @Override
    public ApiPage<MoodleCourseResponse> synchronizedClassFromMoodle() throws JsonProcessingException {
        MoodleMasterDataRequest request = new MoodleMasterDataRequest();
        List<MoodleCourseResponse> course = moodleCourseRepository.getCourses(request);
        Page<MoodleCourseResponse> page = new PageImpl<>(course);
        return PageUtil.convert(page.map(moodleClassResponse -> ObjectUtil.copyProperties(moodleClassResponse, new MoodleCourseResponse(), MoodleCourseResponse.class)));
    }

    @Override
    public Boolean synchronizedAllClassDetailFromMoodle() throws JsonProcessingException {
        List<Class> classes = classRepository.findAll();
        /** Dữ liệu để test, do dữ liệu hiện tại bị lỗi*/
//        List<Class> classes = new ArrayList<>();
//        classes.add(classRepository.findByMoodleClassId(26L));

        for (Class clazz : classes) {
            Map<Long, Section> existedSectionMap = clazz.getSections().stream().collect(Collectors.toMap(Section::getMoodleId, Function.identity()));
            GetMoodleCourseRequest getMoodleCourseRequest = new GetMoodleCourseRequest();
            getMoodleCourseRequest.setCourseid(clazz.getMoodleClassId());
            List<MoodleSectionResponse> detailCourse = moodleCourseRepository.getResourceCourse(getMoodleCourseRequest);
            List<Section> sections = new ArrayList<>();
            for (MoodleSectionResponse moodleSectionResponse : detailCourse) {
                Section existedSection = existedSectionMap.get(moodleSectionResponse.getId());
                Section section = createSection(clazz, moodleSectionResponse);
                if (existedSection != null) {
                    existedSection.setName(section.getName());
                    existedSection.setVisible(section.getVisible());
                    section = existedSection;
                } else {
                    sections.add(section);
                }

                Map<Integer, Module> existedModuleMap = section.getModules().stream().collect(Collectors.toMap(Module::getMoodleId, Function.identity()));
                for (MoodleModuleResponse moodleModuleResponse : moodleSectionResponse.getModules()) {
                    Module existedModule = existedModuleMap.get(moodleModuleResponse.getId());
                    Module module = createModule(section, moodleModuleResponse);
                    if (existedModule != null) {
                        existedModule.setName(module.getName());
                        existedModule.setType(module.getType());
                        existedModule.setUrl(module.getUrl());
                    } else {
                        section.getModules().add(module);
                    }
                }

            }
            clazz.getSections().addAll(sections);
        }
        classRepository.saveAll(classes);
        return true;
    }

    @Override
    public String enrolUserToCourseMoodle(Class clazz,Account account) throws JsonProcessingException {
        Role role = account.getRole();

        MoodleUserResponse moodleAccountOfUser = moodleUtil.getMoodleUserIfExist(account);

        CreateEnrolCourseRequest request = new CreateEnrolCourseRequest();

        CreateEnrolCourseRequest.Enrolment enrolment = new CreateEnrolCourseRequest.Enrolment();
        enrolment.setUserid(moodleAccountOfUser.getId());
        enrolment.setCourseid(new Integer(clazz.getMoodleClassId().toString()));
        enrolment.setRoleid(new Integer(role.getMoodleRoleId().toString()));
        request.getEnrolments().add(enrolment);
        return moodleCourseRepository.enrolUser(request);
    }

    @Override
    public String unenrolUserToCourseMoodle(Class clazz) throws JsonProcessingException {

        Account account = securityUtil.getCurrentUserThrowNotFoundException();
        Role role = account.getRole();

        MoodleUserResponse moodleAccountOfUser = moodleUtil.getMoodleUserIfExist(account);

        CreateEnrolCourseRequest request = new CreateEnrolCourseRequest();

        CreateEnrolCourseRequest.Enrolment enrolment = new CreateEnrolCourseRequest.Enrolment();
        enrolment.setUserid(moodleAccountOfUser.getId());
        enrolment.setCourseid(new Integer(clazz.getMoodleClassId().toString()));

        request.getEnrolments().add(enrolment);
        return moodleCourseRepository.unenrolUser(request);
    }

    @Override
    public Boolean synchronizedClassDetailFromMoodle(Class clazz) throws JsonProcessingException {
        if (clazz.getMoodleClassId() == null) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage("Class is not appear in moodle!");
        }

        GetMoodleCourseRequest getMoodleCourseRequest = new GetMoodleCourseRequest();
        getMoodleCourseRequest.setCourseid(clazz.getMoodleClassId());
        List<MoodleSectionResponse> detailCourse = moodleCourseRepository.getResourceCourse(getMoodleCourseRequest);
        List<Section> sections = new ArrayList<>();
        for (MoodleSectionResponse moodleSectionResponse : detailCourse) {
            Section section = createSection(clazz, moodleSectionResponse);
            for (MoodleModuleResponse moodleModuleResponse : moodleSectionResponse.getModules()) {
                Module module = createModule(section, moodleModuleResponse);
                section.getModules().add(module);

            }
            sections.add(section);
        }
        clazz.getSections().clear();
        clazz.getSections().addAll(sections);

        classRepository.save(clazz);
        return true;
    }

    @Override
    public Boolean synchronizedRoleFromMoodle() throws JsonProcessingException {
        List<MoodleRoleResponse> moodleRoles = moodleRoleRepository.getRoles();
        Map<String, MoodleRoleResponse> moodleRolesMap = moodleRoles.stream()
                .collect(Collectors.toMap(MoodleRoleResponse::getShortname, Function.identity()));

        List<Role> roles = roleRepository.findAll();
        for (Role role : roles) {
            EAccountRole roleCode = role.getCode();
            String moodleRoleName = roleCode.getMoodleName();
            if (!moodleRoleName.isEmpty()) {
                MoodleRoleResponse moodleRoleResponse = moodleRolesMap.get(moodleRoleName);
                if (moodleRoleResponse != null) {
                    role.setMoodleRoleId((long) moodleRoleResponse.getId());
                }
            }
        }
        return true;
    }


    @NotNull
    private Section createSection(Class clazz, MoodleSectionResponse moodleSectionResponse) {
        Section section = new Section();
        section.setClazz(clazz);
        section.setName(moodleSectionResponse.getName());
        section.setMoodleId(moodleSectionResponse.getId());
        if (Objects.equals(moodleSectionResponse.getName(), "General")) {
            section.setVisible(false);
        } else {
            section.setVisible(moodleSectionResponse.isUservisible());
        }
        return section;
    }

    @NotNull
    private Module createModule(Section section, MoodleModuleResponse moodleModuleResponse) {
        Module module = new Module();
        module.setName(moodleModuleResponse.getName());
        module.setType(getModuleType(moodleModuleResponse.getModname()));
        module.setSection(section);
        module.setUrl(moodleModuleResponse.getUrl());
        module.setMoodleId(moodleModuleResponse.getId().intValue());
        return module;
    }

    private EModuleType getModuleType(String modname) {
        if (modname.equals(EModuleType.QUIZ.getLabel())) {
            return EModuleType.QUIZ;
        } else if (modname.equals(EModuleType.LESSON.getLabel())) {
            return EModuleType.LESSON;
        } else if (modname.equals(EModuleType.ASSIGN.getLabel())) {
            return EModuleType.ASSIGN;
        }
        return null;
    }


}
