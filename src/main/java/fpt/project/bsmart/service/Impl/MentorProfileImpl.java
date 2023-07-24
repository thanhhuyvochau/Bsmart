package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ValidationErrors;
import fpt.project.bsmart.entity.common.ValidationErrorsException;
import fpt.project.bsmart.entity.constant.EMentorProfileStatus;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.entity.dto.MentorProfileDTO;
import fpt.project.bsmart.entity.dto.UserDto;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.request.User.MentorSendAddSkill;
import fpt.project.bsmart.entity.response.MentorProfileResponse;
import fpt.project.bsmart.entity.response.mentor.CompletenessMentorProfileResponse;
import fpt.project.bsmart.repository.MentorProfileRepository;
import fpt.project.bsmart.repository.MentorSkillRepository;
import fpt.project.bsmart.repository.SubjectRepository;
import fpt.project.bsmart.service.IMentorProfileService;
import fpt.project.bsmart.util.*;
import fpt.project.bsmart.util.specification.MentorProfileSpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Year;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.*;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Invalid.INVALID_MENTOR_PROFILE_STATUS;

@Service
public class MentorProfileImpl implements IMentorProfileService {
    private final MentorProfileRepository mentorProfileRepository;
    private final SubjectRepository subjectRepository;
    private final MessageUtil messageUtil;

    public MentorProfileImpl(MentorProfileRepository mentorProfileRepository, MentorSkillRepository mentorSkillRepository, SubjectRepository subjectRepository, MessageUtil messageUtil) {
        this.mentorProfileRepository = mentorProfileRepository;
        this.subjectRepository = subjectRepository;
        this.messageUtil = messageUtil;
    }

    private MentorProfile findById(Long id) {
        return mentorProfileRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(MENTOR_PROFILE_NOT_FOUND_BY_ID) + id));
    }

    @Override
    public MentorProfileDTO getMentorProfile(Long id) {
        MentorProfile mentorProfile = findById(id);
        //Check if mentor profile is not active so only that mentor and admin can access
//        if (!mentorProfile.getStatus() || !mentorProfile.getUser().getStatus()) {
//            User user = Optional.ofNullable(SecurityUtil.getCurrentUserAccountLogin()).orElseThrow(() -> ApiException.create(HttpStatus.FORBIDDEN).withMessage("Bạn không có quyền xem hồ sơ của giáo viên này!"));
//            List<EUserRole> roleList = user.getRoles().stream()
//                    .map(Role::getCode)
//                    .collect(Collectors.toList());
//            if (!(roleList.contains(EUserRole.MANAGER) || Objects.equals(user.getId(), mentorProfile.getUser().getId()))) {
//                throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.FORBIDDEN));
//            }
//        }
        MentorProfileDTO mentorProfileDTO = ConvertUtil.convertMentorProfileToMentorProfileDto(mentorProfile);
        return mentorProfileDTO;
    }

    @Override
    public ApiPage<MentorProfileDTO> getAllMentors(MentorSearchRequest mentorSearchRequest, Pageable pageable) {
        MentorProfileSpecificationBuilder builder = MentorProfileSpecificationBuilder.specificationBuilder()
                .searchByUserName(mentorSearchRequest.getQ())
                .searchBySkill(mentorSearchRequest.getSkills());
        Page<MentorProfile> mentorProfilePage = mentorProfileRepository.findAll(builder.build(), pageable);
        List<MentorProfile> mentorProfiles = mentorProfilePage.stream().collect(Collectors.toList());
        List<MentorProfileDTO> mentorProfileDTOS = new ArrayList<>();
        for (MentorProfile mentorProfile : mentorProfiles) {
            mentorProfileDTOS.add(ConvertUtil.convertMentorProfileToMentorProfileDto(mentorProfile));
        }
        Page<MentorProfileDTO> page = new PageImpl<>(mentorProfileDTOS);
        return PageUtil.convert(page);
    }

    @Override
    public List<MentorProfileResponse> getAllMentorProfiles() {
        List<MentorProfileResponse> mentorProfileResponses = new ArrayList<>();
        for (MentorProfile mentorProfile : mentorProfileRepository.findAll()) {
            MentorProfileResponse mentorProfileResponse = new MentorProfileResponse();
            mentorProfileResponse.setId(mentorProfile.getId());
            if (mentorProfile.getUser() != null) {
                mentorProfileResponse.setName(mentorProfile.getUser().getFullName());
            }
            mentorProfileResponses.add(mentorProfileResponse);
        }
        return mentorProfileResponses;
    }

    @Override
    public ApiPage<UserDto> getPendingMentorProfileList(MentorSearchRequest request, Pageable pageable) {
        MentorProfileSpecificationBuilder builder = MentorProfileSpecificationBuilder.specificationBuilder()
                .queryLike(request.getQ())
                .queryByStatus(EMentorProfileStatus.WAITING);


        Page<MentorProfile> mentorProfilePage = mentorProfileRepository.findAll(builder.build(), pageable);
        List<MentorProfile> mentorProfiles = mentorProfilePage.stream().collect(Collectors.toList());

        List<UserDto> userDtoList = new ArrayList<>();
        for (MentorProfile mentorProfile : mentorProfiles) {
            User user = mentorProfile.getUser();
            UserDto userDto = ConvertUtil.convertUsertoUserDto(user);
            userDto.setWallet(null);
            userDtoList.add(userDto);
        }
        return PageUtil.convert(new PageImpl<>(userDtoList, pageable, userDtoList.size()));

    }

    @Transactional
    @Override
    public Long approveMentorProfile(Long id, ManagerApprovalAccountRequest managerApprovalAccountRequest) {
        MentorProfile mentorProfile = findById(id);

        if (mentorProfile.getUser() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(ACCOUNT_IS_NOT_MENTOR));
        }

        validateApprovalAccountRequest(managerApprovalAccountRequest.getStatus());

        if (mentorProfile.getStatus() != EMentorProfileStatus.WAITING) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(ACCOUNT_STATUS_NOT_ALLOW) + mentorProfile.getStatus());
        }
        List<MentorSkill> skills = mentorProfile.getSkills();
        List<MentorSkill> skillsActive = new ArrayList<>( );
        for (MentorSkill skill : skills) {
            skill.setStatus(true);
            skillsActive.add(skill);
        }
        mentorProfile.setSkills(skillsActive);
        mentorProfile.setStatus(managerApprovalAccountRequest.getStatus());
        ActivityHistoryUtil.logHistoryForAccountApprove(mentorProfile.getUser(), managerApprovalAccountRequest.getMessage());

        return mentorProfileRepository.save(mentorProfile).getId();

    }

    private void validateApprovalAccountRequest(EMentorProfileStatus accountStatus) {
        List<EMentorProfileStatus> ALLOWED_STATUSES = Arrays.asList(EMentorProfileStatus.STARTING, EMentorProfileStatus.EDITREQUEST, EMentorProfileStatus.REJECTED);

        if (!ALLOWED_STATUSES.contains(accountStatus)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(ACCOUNT_STATUS_NOT_ALLOW));
        }
    }


    @Override
    public Long updateMentorProfile(UpdateMentorProfileRequest updateMentorProfileRequest) {
        User user = SecurityUtil.getCurrentUser();
        MentorProfile mentorProfile = mentorProfileRepository.getMentorProfileByUser(user)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.MENTOR_PROFILE_NOT_FOUND_BY_USER) + user.getId()));

        MentorUtil.checkMentorStatusToUpdateInformation(mentorProfile);


        if (updateMentorProfileRequest.getIntroduce() != null) {
            mentorProfile.setIntroduce(updateMentorProfileRequest.getIntroduce());
        }

        if (updateMentorProfileRequest.getWorkingExperience() != null) {
            mentorProfile.setWorkingExperience(updateMentorProfileRequest.getWorkingExperience());
        }

        if (updateMentorProfileRequest.getMentorSkills() != null) {
            List<UpdateSkillRequest> mentorUpdateSkills = updateMentorProfileRequest.getMentorSkills();
            Set<Long> skillIds = new HashSet<>();
            for (UpdateSkillRequest mentorUpdateSkill : mentorUpdateSkills) {
                if (mentorUpdateSkill.getYearOfExperiences() <= 0) {
                    throw ApiException.create(HttpStatus.BAD_REQUEST)
                            .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Invalid.NEGATIVE_YEAR_OF_EXPERIENCES) + mentorUpdateSkill.getYearOfExperiences());
                }
                ZonedDateTime userBirthYear = mentorProfile.getUser().getBirthday().atZone(ZoneOffset.UTC);
                int userAge = Year.now().getValue() - userBirthYear.getYear();
                boolean validMaximumYearOfExperience = userAge - mentorUpdateSkill.getYearOfExperiences() > 1;
                if (!validMaximumYearOfExperience) {
                    throw ApiException.create(HttpStatus.BAD_REQUEST)
                            .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Invalid.INVALID_YEAR_OF_EXPERIENCES) + mentorUpdateSkill.getYearOfExperiences());
                }
                if (mentorUpdateSkill.getSkillId() == null) {
                    throw ApiException.create(HttpStatus.BAD_REQUEST)
                            .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Empty.EMPTY_SKILL));
                }
                if (!skillIds.add(mentorUpdateSkill.getSkillId())) {
                    // Duplicate skillId found, raise error
                    throw ApiException.create(HttpStatus.BAD_REQUEST)
                            .withMessage(messageUtil.getLocalMessage(SUBJECT_ID_DUPLICATE) + mentorUpdateSkill.getSkillId());
                }
            }
            List<MentorSkill> mentorSkills = new ArrayList<>();
            for (UpdateSkillRequest mentorUpdateSkill : mentorUpdateSkills) {
                MentorSkill mentorSkill = new MentorSkill();
                Subject subject = subjectRepository.findById(mentorUpdateSkill.getSkillId())
                        .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(SUBJECT_NOT_FOUND_BY_ID) + mentorUpdateSkill.getSkillId()));
                mentorSkill.setSkill(subject);
                mentorSkill.setYearOfExperiences(mentorUpdateSkill.getYearOfExperiences());
                mentorSkill.setMentorProfile(mentorProfile);
                mentorSkill.setStatus(false);
                mentorSkills.add(mentorSkill);
            }
            mentorProfile.setSkills(mentorSkills);
        }


        return mentorProfileRepository.save(mentorProfile).getId();
    }

    @Override
    public List<Long> updateCertificate(List<ImageRequest> imageRequests) {
        return null;
    }

    @Override
    public CompletenessMentorProfileResponse getCompletenessMentorProfile() {

        return MentorUtil.checkCompletenessMentorProfile();
    }

    @Override
    public Boolean mentorRequestApprovalAccount(Long id) throws Exception {
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();

        MentorProfile mentorProfile = mentorProfileRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.MENTOR_PROFILE_NOT_FOUND_BY_USER) + id));

        if (!currentUserAccountLogin.getMentorProfile().equals(mentorProfile)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(MENTOR_PROFILE_NOT_FOUND_BY_USER));

        }

        if (!mentorProfile.getStatus().equals(EMentorProfileStatus.REQUESTING) &&
                !mentorProfile.getStatus().equals(EMentorProfileStatus.EDITREQUEST) ) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(ACCOUNT_STATUS_NOT_ALLOW));
        }

        List<Role> roles = currentUserAccountLogin.getRoles();
        List<Boolean> checkRoleTeacher = roles.stream().map(role -> role.getCode().equals(EUserRole.TEACHER)).collect(Collectors.toList());

        if (checkRoleTeacher.isEmpty()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(messageUtil.getLocalMessage(ACCOUNT_IS_NOT_MENTOR));
        }


        ValidationErrors vaErr = new ValidationErrors();

        CompletenessMentorProfileResponse response = MentorUtil.checkCompletenessMentorProfile();
        List<CompletenessMentorProfileResponse.MissingInformation.RequiredInfo> requiredInfoList
                = response.getMissingInformation().stream().map(CompletenessMentorProfileResponse.MissingInformation::getRequiredInfo).collect(Collectors.toList());
        ValidationErrors.ValidationError validationError = new ValidationErrors.ValidationError();

        ArrayList<String> invalidParams = new ArrayList<String>();

        requiredInfoList.forEach(requiredInfo -> {
            requiredInfo.getFields().stream()
                    .map(CompletenessMentorProfileResponse.MissingInformation.RequiredInfo.Field::getField)
                    .findFirst()
                    .ifPresent(invalidParams::add);

        });
        validationError.setMessage("Vui lòng cập nhật đây đủ thông tin trước khi yêu cầu phê duyệt tài khoản");
        validationError.setInvalidParams(invalidParams);
        vaErr.setError(validationError);

        if (!invalidParams.isEmpty()) {
            throw new ValidationErrorsException(vaErr.getError().getInvalidParams(), vaErr.getError().getMessage());
        }

        mentorProfile.setStatus(EMentorProfileStatus.WAITING);
        mentorProfileRepository.save(mentorProfile);
        return true;
    }

    @Override
    public Boolean mentorRequestApprovalSkill( MentorSendAddSkill mentorSendAddSkill) {
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();
        MentorProfile mentorProfile = currentUserAccountLogin.getMentorProfile();
        if (mentorProfile == null){
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage(MENTOR_PROFILE_NOT_FOUND_BY_USER);
        }

        List<MentorSkill> skills = mentorProfile.getSkills();
        List<Long> skillIds = skills.stream().map(mentorSkill -> mentorSkill.getSkill().getId()).collect(Collectors.toList());
        List<UpdateSkillRequest> mentorSkills = mentorSendAddSkill.getMentorSkills();

        List<MentorSkill> mentorSkillList = new ArrayList<>();
        for (UpdateSkillRequest mentorSkill : mentorSkills) {
            if (skillIds.contains(mentorSkill.getSkillId())) {
                throw ApiException.create(HttpStatus.BAD_REQUEST)
                        .withMessage("Bạn đã có quyền dạy môn học này ! Không thể yêu cầu mở thêm ");
            }
            MentorSkill mentorSkill1 = new MentorSkill();
            Subject subject = subjectRepository.findById(mentorSkill.getSkillId())
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                            .withMessage(messageUtil.getLocalMessage(SKILL_NOT_FOUND_BY_ID)));
            mentorSkill1.setSkill(subject);
            mentorSkill1.setStatus(false);
            mentorSkill1.setMentorProfile(mentorProfile);
            mentorSkill1.setYearOfExperiences(mentorSkill.getYearOfExperiences());
            mentorSkillList.add(mentorSkill1);
        }
        mentorProfile.setSkills(mentorSkillList);
        mentorProfileRepository.save(mentorProfile);
        return true;
    }


}
