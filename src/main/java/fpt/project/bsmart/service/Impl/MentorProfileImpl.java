package fpt.project.bsmart.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fpt.project.bsmart.director.NotificationConstant;
import fpt.project.bsmart.director.NotificationDirector;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ValidationErrors;
import fpt.project.bsmart.entity.common.ValidationErrorsException;
import fpt.project.bsmart.entity.constant.*;
import fpt.project.bsmart.entity.dto.*;
import fpt.project.bsmart.entity.dto.mentor.MentorProfileRequestEditDTO;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.request.User.MentorSendAddSkill;
import fpt.project.bsmart.entity.request.mentorprofile.ImageEditDto;
import fpt.project.bsmart.entity.request.mentorprofile.ManagerApprovalEditProfileRequest;
import fpt.project.bsmart.entity.request.mentorprofile.ManagerSearchEditProfileRequest;
import fpt.project.bsmart.entity.request.mentorprofile.UserDtoRequest;
import fpt.project.bsmart.entity.response.MentorProfileResponse;
import fpt.project.bsmart.entity.response.mentor.*;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.IMentorProfileService;
import fpt.project.bsmart.util.*;
import fpt.project.bsmart.util.email.EmailUtil;
import fpt.project.bsmart.util.specification.MentorProfileSpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.Year;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static fpt.project.bsmart.util.Constants.ErrorMessage.*;
import static fpt.project.bsmart.util.Constants.ErrorMessage.Invalid.*;
import static fpt.project.bsmart.util.ConvertUtil.*;

@Service
public class MentorProfileImpl implements IMentorProfileService {
    private final MentorProfileRepository mentorProfileRepository;
    private final SubjectRepository subjectRepository;
    private final MessageUtil messageUtil;

    private final UserImageRepository userImageRepository;

    private final MentorSkillRepository mentorSkillRepository;

    private final ActivityHistoryRepository activityHistoryRepository;
    private final NotificationRepository notificationRepository;
    private final WebSocketUtil webSocketUtil;

    private final MentorProfileEditRepository mentorProfileEditRepository;
    private final EmailUtil emailUtil;

    public MentorProfileImpl(MentorProfileRepository mentorProfileRepository, MentorSkillRepository mentorSkillRepository, SubjectRepository subjectRepository, MessageUtil messageUtil, UserImageRepository userImageRepository, MentorSkillRepository mentorSkillRepository1, ActivityHistoryRepository activityHistoryRepository, NotificationRepository notificationRepository, WebSocketUtil webSocketUtil, MentorProfileEditRepository mentorProfileEditRepository, EmailUtil emailUtil) {
        this.mentorProfileRepository = mentorProfileRepository;
        this.subjectRepository = subjectRepository;
        this.messageUtil = messageUtil;
        this.userImageRepository = userImageRepository;
        this.mentorSkillRepository = mentorSkillRepository1;
        this.activityHistoryRepository = activityHistoryRepository;
        this.notificationRepository = notificationRepository;
        this.webSocketUtil = webSocketUtil;
        this.mentorProfileEditRepository = mentorProfileEditRepository;
        this.emailUtil = emailUtil;
    }

    private MentorProfile findById(Long id) {
        return mentorProfileRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(MENTOR_PROFILE_NOT_FOUND_BY_ID) + id));
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
        MentorProfileDTO mentorProfileDTO = convertMentorProfileToMentorProfileDto(mentorProfile);
        return mentorProfileDTO;
    }

    @Override
    public ApiPage<MentorProfileDTO> getAllMentors(MentorSearchRequest mentorSearchRequest, Pageable pageable) {
        MentorProfileSpecificationBuilder builder = MentorProfileSpecificationBuilder.specificationBuilder()
                .queryByStatus(mentorSearchRequest.getAccountStatus())
                .searchByUserName(mentorSearchRequest.getQ())
                .searchBySkill(mentorSearchRequest.getSkills());
        Page<MentorProfile> mentorProfilePage = mentorProfileRepository.findAll(builder.build(), pageable);
        List<MentorProfile> mentorProfiles = mentorProfilePage.stream().collect(Collectors.toList());
        List<MentorProfileDTO> mentorProfileDTOS = new ArrayList<>();
        for (MentorProfile mentorProfile : mentorProfiles) {
            mentorProfileDTOS.add(convertMentorProfileToMentorProfileDto(mentorProfile));
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
                .queryByStatus(request.getAccountStatus());

        List<MentorProfile> mentorProfiles = mentorProfileRepository.findAll(builder.build());
        List<UserDto> userDtoList = new ArrayList<>();
        for (MentorProfile mentorProfile : mentorProfiles) {
            User user = mentorProfile.getUser();
            UserDto userDto = ConvertUtil.convertUsertoUserDto(user);
            userDto.setWallet(null);
            userDtoList.add(userDto);
        }
        Page<UserDto> userDtos = PageUtil.toPage(userDtoList, pageable);
        return PageUtil.convert(userDtos);

    }

    @Transactional
    @Override
    public Long approveMentorProfile(Long id, ManagerApprovalAccountRequest managerApprovalAccountRequest) {

        MentorProfile mentorProfile = findById(id);

        if (mentorProfile.getUser() == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(ACCOUNT_IS_NOT_MENTOR));
        }
        validateApprovalAccountRequest(managerApprovalAccountRequest.getStatus());

        if (mentorProfile.getStatus() != EMentorProfileStatus.WAITING && mentorProfile.getStatus() != EMentorProfileStatus.STARTING) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(ACCOUNT_STATUS_NOT_ALLOW) + mentorProfile.getStatus());
        }
        if (mentorProfile.getStatus() == EMentorProfileStatus.STARTING) {
            List<MentorSkill> skills = mentorProfile.getSkills();
            List<MentorSkill> skillsActive = new ArrayList<>();
            for (MentorSkill skill : skills) {
                skill.setStatus(true);
                skillsActive.add(skill);
            }
            mentorProfile.setSkills(skillsActive);
        }
        mentorProfile.setStatus(managerApprovalAccountRequest.getStatus());

//        ActivityHistoryUtil.logHistoryForAccountSendRequestApprove(mentorProfile.getUser(), managerApprovalAccountRequest.getMessage());
        Notification notification = NotificationDirector.buildApprovalMentorProfile(mentorProfile);
        notificationRepository.save(notification);
        ResponseMessage responseMessage = convertNotificationToResponseMessage(notification, mentorProfile.getUser());
        webSocketUtil.sendPrivateNotification(mentorProfile.getUser().getEmail(), responseMessage);
        emailUtil.sendApprovalMentorProfile(mentorProfile, managerApprovalAccountRequest.getMessage());
        return mentorProfileRepository.save(mentorProfile).getId();
    }

    private void validateApprovalAccountRequest(EMentorProfileStatus accountStatus) {
        List<EMentorProfileStatus> ALLOWED_STATUSES = Arrays.asList(EMentorProfileStatus.STARTING, EMentorProfileStatus.EDITREQUEST, EMentorProfileStatus.REJECTED);

        if (!ALLOWED_STATUSES.contains(accountStatus)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(ACCOUNT_STATUS_NOT_ALLOW));
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
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Invalid.NEGATIVE_YEAR_OF_EXPERIENCES) + mentorUpdateSkill.getYearOfExperiences());
                }
                ZonedDateTime userBirthYear = mentorProfile.getUser().getBirthday().atZone(ZoneOffset.UTC);
                int userAge = Year.now().getValue() - userBirthYear.getYear();
                boolean validMaximumYearOfExperience = userAge - mentorUpdateSkill.getYearOfExperiences() > 1;
                if (!validMaximumYearOfExperience) {
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Invalid.INVALID_YEAR_OF_EXPERIENCES) + mentorUpdateSkill.getYearOfExperiences());
                }
                if (mentorUpdateSkill.getSkillId() == null) {
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Empty.EMPTY_SKILL));
                }
                if (!skillIds.add(mentorUpdateSkill.getSkillId())) {
                    // Duplicate skillId found, raise error
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(SUBJECT_ID_DUPLICATE) + mentorUpdateSkill.getSkillId());
                }
            }
            List<MentorSkill> mentorSkills = new ArrayList<>();
            for (UpdateSkillRequest mentorUpdateSkill : mentorUpdateSkills) {
                MentorSkill mentorSkill = new MentorSkill();
                Subject subject = subjectRepository.findById(mentorUpdateSkill.getSkillId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(SUBJECT_NOT_FOUND_BY_ID) + mentorUpdateSkill.getSkillId()));
                mentorSkill.setSkill(subject);
                mentorSkill.setYearOfExperiences(mentorUpdateSkill.getYearOfExperiences());
                mentorSkill.setMentorProfile(mentorProfile);
                mentorSkill.setStatus(mentorUpdateSkill.getStatus());
                mentorSkill.setVerified(true);
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
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(MENTOR_PROFILE_NOT_FOUND_BY_USER));
        }
        if (!mentorProfile.getStatus().equals(EMentorProfileStatus.REQUESTING) && !mentorProfile.getStatus().equals(EMentorProfileStatus.EDITREQUEST)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(ACCOUNT_STATUS_NOT_ALLOW));
        }
        List<Role> roles = currentUserAccountLogin.getRoles();
        List<Boolean> checkRoleTeacher = roles.stream().map(role -> role.getCode().equals(EUserRole.TEACHER)).collect(Collectors.toList());
        if (checkRoleTeacher.isEmpty()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(ACCOUNT_IS_NOT_MENTOR));
        }
        ValidationErrors vaErr = new ValidationErrors();
        CompletenessMentorProfileResponse response = MentorUtil.checkCompletenessMentorProfile();
        List<CompletenessMentorProfileResponse.MissingInformation.RequiredInfo> requiredInfoList =
                response.getMissingInformation().stream().map(CompletenessMentorProfileResponse.MissingInformation::getRequiredInfo).collect(Collectors.toList());
        ValidationErrors.ValidationError validationError = new ValidationErrors.ValidationError();

        ArrayList<String> invalidParams = new ArrayList<String>();

        requiredInfoList.forEach(requiredInfo -> {
            requiredInfo.getFields().stream().map(CompletenessMentorProfileResponse.MissingInformation.RequiredInfo.Field::getField).findFirst().ifPresent(invalidParams::add);
        });
        validationError.setMessage("Vui lòng cập nhật đây đủ thông tin trước khi yêu cầu phê duyệt tài khoản");
        validationError.setInvalidParams(invalidParams);
        vaErr.setError(validationError);

        if (!invalidParams.isEmpty()) {
            throw new ValidationErrorsException(vaErr.getError().getInvalidParams(), vaErr.getError().getMessage());
        }
        ActivityHistory activityHistory = activityHistoryRepository.findByTypeAndActivityId(EActivityType.USER, currentUserAccountLogin.getId());
        if (activityHistory != null) {
            activityHistory.setLastModified(Instant.now());
            activityHistory.setCount(activityHistory.getCount() + 1);
            activityHistoryRepository.save(activityHistory);
        } else {
            ActivityHistoryUtil.logHistoryForAccountSendRequestApprove(currentUserAccountLogin);
        }
        mentorProfile.setStatus(EMentorProfileStatus.WAITING);
        mentorProfileRepository.save(mentorProfile);
        return true;
    }


    @Override
    public Boolean mentorCreateApprovalSkill(MentorSendAddSkill mentorSendAddSkill) {
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();
        MentorProfile mentorProfile = currentUserAccountLogin.getMentorProfile();
        if (mentorProfile == null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(MENTOR_PROFILE_NOT_FOUND_BY_USER);
        }

        List<MentorSkill> skills = mentorProfile.getSkills();
        List<Long> skillIds = skills.stream().map(mentorSkill -> mentorSkill.getSkill().getId()).collect(Collectors.toList());
        List<UpdateSkillRequest> mentorSkills = mentorSendAddSkill.getMentorSkills();

        List<MentorSkill> mentorSkillList = new ArrayList<>();
        for (UpdateSkillRequest mentorSkill : mentorSkills) {
            if (skillIds.contains(mentorSkill.getSkillId())) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Bạn đã có quyền dạy môn học này ! Không thể yêu cầu mở thêm ");
            }
            MentorSkill mentorSkill1 = new MentorSkill();
            Subject subject = subjectRepository.findById(mentorSkill.getSkillId())
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                            .withMessage(messageUtil.getLocalMessage(SKILL_NOT_FOUND_BY_ID)));

            mentorSkill1.setSkill(subject);
            mentorSkill1.setStatus(false);
            mentorSkill1.setVerified(false);
            mentorSkill1.setMentorProfile(mentorProfile);
            mentorSkill1.setYearOfExperiences(mentorSkill.getYearOfExperiences());
            mentorSkillList.add(mentorSkill1);
        }
        mentorProfile.getSkills().addAll(mentorSkillList);

//        List<UserImage> SkillFilesInRequest = userImageRepository.findAllById(mentorSendAddSkill.getFileIds());
//
//        List<UserImage> updateDegrees = new ArrayList<>();
//        SkillFilesInRequest.forEach(userImage -> {
//            if (!userImage.getType().equals(EImageType.DEGREE)) {
//                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Vui lòng nộp bằng cấp liên quan môn học bạn muốn giảng dạy!");
//            }
//            if (userImage.getUser() != null) {
//                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Bằng cấp này đang được sử dụng! vui lòng kiểm tra lại");
//            }
//            userImage.setUser(currentUserAccountLogin);
//            updateDegrees.add(userImage);
//        });
//        userImageRepository.saveAll(updateDegrees);

        mentorProfileRepository.save(mentorProfile);
        return true;
    }

    @Transactional
    @Override
    public Boolean managerHandleRequestApprovalSkill(Long id, ManagerApprovalSkillRequest managerApprovalSkillRequest) {
        MentorProfile mentorProfile = mentorProfileRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.MENTOR_PROFILE_NOT_FOUND_BY_USER) + id));


        List<MentorSkill> byMentorProfileAndStatus = mentorSkillRepository.findByMentorProfileAndStatusAndVerified(mentorProfile, true, false);

        // check request & DB
        List<Long> skillIds = managerApprovalSkillRequest.getSkillIds();
        for (Long skillId : skillIds) {
            MentorSkill bySkillIdAndStatus = mentorSkillRepository.findByMentorProfileAndSkillIdAndStatusAndVerified(mentorProfile, skillId, true, false)
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                            .withMessage("Môn học với ID " + skillId + " không có sẵn trong yêu cầu phê duyệt của giáo viên "));

            if (!byMentorProfileAndStatus.contains(bySkillIdAndStatus)) {
                throw ApiException.create(HttpStatus.BAD_REQUEST)
                        .withMessage("Môn học với ID " + skillId + " không có sẵn trong yêu cầu phê duyệt của giáo viên ");
            }
        }


        if (byMentorProfileAndStatus.size() > managerApprovalSkillRequest.getSkillIds().size()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Có " + byMentorProfileAndStatus.size() + " môn học cần được phê duyệt ! Vui lòng kiểm tra lại số môn học bạn đang duyệt !  ");
        }

        List<UserImage> byUserAndStatus = userImageRepository.findByUserAndTypeAndStatus(mentorProfile.getUser(), EImageType.DEGREE, true);

        // check request & DB
        List<Long> degreeIds = managerApprovalSkillRequest.getDegreeIds();
        for (Long degreeId : degreeIds) {
            UserImage userImage = userImageRepository.findByIdAndUserAndTypeAndStatusAndVerified(degreeId, mentorProfile.getUser(), EImageType.DEGREE, true, false)
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                            .withMessage("Bằng cấp với ID " + degreeId + " không có sẵn trong yêu cầu phê duyệt của giáo viên "));

            if (!byUserAndStatus.contains(userImage)) {
                throw ApiException.create(HttpStatus.BAD_REQUEST)
                        .withMessage("Bằng cấp với ID " + degreeId + " không có sẵn trong yêu cầu phê duyệt của giáo viên ");
            }
        }

        if (byUserAndStatus.size() > managerApprovalSkillRequest.getDegreeIds().size()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Có " + byUserAndStatus.size() + " bằng cấp cần được phê duyệt ! Vui lòng kiểm tra lại số bằng cấp bạn đang duyệt !");
        }


        List<MentorSkill> mentorSkillToApproval = new ArrayList<>();
        byMentorProfileAndStatus.forEach(mentorSkill -> {
            mentorSkill.setVerified(managerApprovalSkillRequest.getStatus());
            mentorSkillToApproval.add(mentorSkill);
        });


        List<UserImage> userImageList = new ArrayList<>();
        byUserAndStatus.forEach(userImage -> {
            userImage.setVerified(managerApprovalSkillRequest.getStatus());
            userImageList.add(userImage);
        });
        userImageRepository.saveAll(userImageList);
        mentorSkillRepository.saveAll(mentorSkillToApproval);
        return true;
    }

    @Override
    public ApiPage<ManagerGetRequestApprovalSkillResponse> managerGetRequestApprovalSkill(Pageable pageable) {
        List<ManagerGetRequestApprovalSkillResponse> responseList = new ArrayList<>();

        List<MentorSkill> byStatus = mentorSkillRepository.findByStatusAndVerified(true, false);
        List<MentorProfile> mentorProfileSkillStatusIsFalse = byStatus.stream().map(MentorSkill::getMentorProfile).collect(Collectors.toList());
        List<MentorProfile> mentorProfiles = mentorProfileSkillStatusIsFalse.stream()
                .filter(mentorProfile -> mentorProfile.getStatus().equals(EMentorProfileStatus.STARTING)).collect(Collectors.toList());

        mentorProfiles.forEach(mentorProfile -> {

            User user = mentorProfile.getUser();
            ManagerGetRequestApprovalSkillResponse ManagerGetRequestApprovalSkillResponse = ObjectUtil.copyProperties(user, new ManagerGetRequestApprovalSkillResponse(), ManagerGetRequestApprovalSkillResponse.class);

            UserDto userDto = ConvertUtil.convertUsertoUserDto(user);
            ManagerGetRequestApprovalSkillResponse.setMentorProfileId(mentorProfile.getId());
            List<MentorSkill> byMentorProfileAndStatus = mentorSkillRepository.findByMentorProfileAndStatusAndVerified(mentorProfile, true, false);


            if (!byMentorProfileAndStatus.isEmpty()) {
                List<MentorSkillDto> skillList = new ArrayList<>();
                for (MentorSkill mentorSkill : byMentorProfileAndStatus) {
                    MentorSkillDto mentorSkillDto = convertMentorSkillToMentorSkillDto(mentorSkill);
                    skillList.add(mentorSkillDto);

                }
                ManagerGetRequestApprovalSkillResponse.setTotalSkillRequest(byMentorProfileAndStatus.size());
                ManagerGetRequestApprovalSkillResponse.setMentorSkillRequest(skillList);

            }
            List<UserImage> byUserAndStatus = userImageRepository.findByUserAndTypeAndStatusAndVerified(user, EImageType.DEGREE, true, false);
            if (!byUserAndStatus.isEmpty()) {
                List<ImageDto> imageDtoList = new ArrayList<>();
                for (UserImage image : byUserAndStatus) {
                    imageDtoList.add(convertUserImageToUserImageDto(image));
                    ManagerGetRequestApprovalSkillResponse.setCreated(image.getCreated());
                }
                ManagerGetRequestApprovalSkillResponse.setDegreeRequest(imageDtoList);
                ManagerGetRequestApprovalSkillResponse.setTotalDegreeRequest(byUserAndStatus.size());
            }


            responseList.add(ManagerGetRequestApprovalSkillResponse);
        });

        Page<ManagerGetRequestApprovalSkillResponse> pages = new PageImpl<>(responseList, pageable, responseList.size());

        return PageUtil.convert(pages);
    }

    @Override
    public Boolean mentorRequestApprovalSkill(MentorSendSkillRequest mentorSendAddSkill) {
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();
        MentorProfile mentorProfile = currentUserAccountLogin.getMentorProfile();

        List<MentorSkill> mentorSkillList = new ArrayList<>();
        for (Long skillId : mentorSendAddSkill.getSkillIds()) {
            MentorSkill bySkillIdAndStatus = mentorSkillRepository.findByMentorProfileAndSkillIdAndStatusAndVerified(mentorProfile, skillId, false, false)
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                            .withMessage("Môn học với ID " + skillId + " không tìm thấy "));

            bySkillIdAndStatus.setStatus(true);
            mentorSkillList.add(bySkillIdAndStatus);

        }
        List<UserImage> userImageList = new ArrayList<>();
        List<Long> degreeIds = mentorSendAddSkill.getDegreeIds();
        for (Long degreeId : degreeIds) {
            UserImage userImage = userImageRepository.findByIdAndUserAndTypeAndStatusAndVerified
                            (degreeId, mentorProfile.getUser(), EImageType.DEGREE, false, false)
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                            .withMessage("Bằng cấp với ID " + degreeId + " không tìm thấy"));
            userImage.setStatus(true);
            userImageList.add(userImage);
        }

        mentorSkillRepository.saveAll(mentorSkillList);
        userImageRepository.saveAll(userImageList);

        return true;
    }

    @Override
    public List<MentorGetRequestApprovalSkillResponse> ManagerGetRequestApprovalSkillResponse() {
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();
        MentorProfile mentorProfile = currentUserAccountLogin.getMentorProfile();
        List<MentorGetRequestApprovalSkillResponse> responseList = new ArrayList<>();


        User user = mentorProfile.getUser();

        MentorGetRequestApprovalSkillResponse response = new MentorGetRequestApprovalSkillResponse();


        List<MentorSkill> byMentorProfileAndStatus = mentorSkillRepository
                .findByMentorProfileAndStatusAndVerified(mentorProfile, false, false);


        if (!byMentorProfileAndStatus.isEmpty()) {
            List<MentorSkillDto> skillList = new ArrayList<>();
            for (MentorSkill mentorSkill : byMentorProfileAndStatus) {
                MentorSkillDto mentorSkillDto = convertMentorSkillToMentorSkillDto(mentorSkill);
                skillList.add(mentorSkillDto);
            }
            response.setTotalSkillRequest(byMentorProfileAndStatus.size());
            response.setMentorSkillRequest(skillList);

        }
        List<UserImage> byUserAndStatus = userImageRepository
                .findByUserAndTypeAndStatusAndVerified(user, EImageType.DEGREE, false, false);
        if (!byUserAndStatus.isEmpty()) {
            List<ImageDto> imageDtoList = new ArrayList<>();
            for (UserImage image : byUserAndStatus) {
                imageDtoList.add(convertUserImageToUserImageDto(image));
                response.setCreated(image.getCreated());
            }
            response.setDegreeRequest(imageDtoList);
            response.setTotalDegreeRequest(byUserAndStatus.size());
        }
        responseList.add(response);

        return responseList;
    }

    @Override
    public Long mentorCreateEditProfileRequest(UserDtoRequest request) throws JsonProcessingException {
        User user = SecurityUtil.getCurrentUser();
        MentorProfile mentorProfile = user.getMentorProfile();

        if (!mentorProfile.getStatus().equals(EMentorProfileStatus.STARTING)) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Trạng thái tài khoản không cho phép chỉnh sửa!");
        }

        MentorProfileEdit byStatusPending = mentorProfileEditRepository.findByMentorProfileAndStatus(mentorProfile, EMentorProfileEditStatus.PENDING);

        if (byStatusPending != null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Hồ sơ của bạn đang được xử lý! Không thể chỉnh sửa lúc này!");
        }

        UserDto userDto = new UserDto();
        // chỉnh sưa thông tin của table user
        UserDto userDtoEdit = mentorCreateEditUser(userDto, request, user);

        // chỉnh sưa thông tin của table mentor profile
        mentorCreateEditMentorProfile(userDtoEdit, request, mentorProfile);

        // chỉnh sưa thông tin về hinh ảnh (avt , bằng cấp , cmnd)
        mentorCreateEditImage(userDtoEdit, request);

        // kiểm tra xem user này đã gửi request edit trước đó chưa => nếu rồi thì không cho gửi nữa !!!
        MentorProfileEdit byStatus = mentorProfileEditRepository.findByMentorProfileAndStatus(mentorProfile, EMentorProfileEditStatus.PENDING);
        if (byStatus != null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).
                    withMessage("Hồ sơ này đã gửi yêu cầu chỉnh sửa trước đó! Vui lòng thử lại sau! ");
        }
        // set data into table mentor profile edit to save edit profile request

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(userDto);

        MentorProfileEdit byStatusCreate = mentorProfileEditRepository.findByStatus(EMentorProfileEditStatus.CREATING);
        if (byStatusCreate != null) {
            byStatusCreate.setMentorProfile(mentorProfile);
            byStatusCreate.setProfileData(json);
            byStatusCreate.setStatus(EMentorProfileEditStatus.CREATING);
            MentorProfileEdit save = mentorProfileEditRepository.save(byStatusCreate);
            return save.getId();
        } else {
            MentorProfileEdit mentorProfileEdit = new MentorProfileEdit();
            mentorProfileEdit.setMentorProfile(mentorProfile);
            mentorProfileEdit.setProfileData(json);
            mentorProfileEdit.setStatus(EMentorProfileEditStatus.CREATING);
            MentorProfileEdit save = mentorProfileEditRepository.save(mentorProfileEdit);
            return save.getId();
        }

    }

    public UserDto mentorCreateEditUser(UserDto userDto, UserDtoRequest request, User user) {
        if (user.getWallet() != null) {
            WalletDto walletDto = convertWalletToWalletDto(user.getWallet());
            userDto.setWallet(walletDto);
        }

        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());

        if (request.getBirthday() != null) {
            if (!TimeUtil.isValidBirthday(request.getBirthday(), EUserRole.TEACHER)) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_BIRTHDAY));
            }
            userDto.setBirthday(request.getBirthday());
        }

        if (request.getPhone() != null) {
            if (!StringUtil.isValidVietnameseMobilePhoneNumber(request.getPhone())) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_PHONE_NUMBER));
            }
            userDto.setPhone(request.getPhone());
        }
        if (request.getFullName() != null) {
            userDto.setFullName(request.getFullName());
        }
        if (request.getAddress() != null) {
            userDto.setAddress(request.getAddress());
        }
        if (request.getGender() != null) {
            userDto.setGender(request.getGender());
        }
        userDto.setStatus(user.getStatus());

        List<Role> roles = user.getRoles();
        List<RoleDto> roleDtos = new ArrayList<>();
        roles.forEach(role -> {
            roleDtos.add(ConvertUtil.convertRoleToRoleDto(role));
        });
        userDto.setRoles(roleDtos);

        // chỉnh sửa social
        if (request.getFacebookLink() != null) {
            if (request.getFacebookLink().isEmpty()) {
                userDto.setFacebookLink(null);
            } else if (!StringUtil.isValidFacebookLink(request.getFacebookLink())) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_FACEBOOK_LINK));
            }
            userDto.setFacebookLink(request.getFacebookLink());
        }
        if (request.getLinkedinLink() != null) {
            if (request.getLinkedinLink().isEmpty()) {
                userDto.setLinkedinLink(null);
            } else if (!StringUtil.isValidLinkedinLink(request.getLinkedinLink())) {
                throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_TWITTER_LINK));
            }
            userDto.setLinkedinLink(request.getLinkedinLink());
        }
        boolean isTeacher = SecurityUtil.isHasAnyRole(user, EUserRole.TEACHER);
        if (isTeacher) {
            if (request.getWebsite() != null) {
                if (request.getWebsite().isEmpty()) {
                    userDto.setWebsite(null);
                } else if (!StringUtil.isValidWebsite(request.getWebsite())) {
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(INVALID_WEBSITE));
                }
                userDto.setWebsite(request.getWebsite());
            }
        }

        return userDto;
    }

    public void mentorCreateEditMentorProfile(UserDto userDtoEdit, UserDtoRequest request, MentorProfile mentorProfile) {

        MentorProfileRequestEditDTO mentorProfileRequest = request.getMentorProfile();
        if (mentorProfileRequest != null) {
            MentorProfileDTO mentorProfileDto = new MentorProfileDTO();

            mentorProfileDto.setId(mentorProfile.getId());
            mentorProfileDto.setStatus(mentorProfile.getStatus());

            if (mentorProfileRequest.getIntroduce() != null) {
                mentorProfileDto.setIntroduce(mentorProfileRequest.getIntroduce());
            }
            if (mentorProfileRequest.getWorkingExperience() != null) {
                mentorProfileDto.setWorkingExperience(mentorProfileRequest.getWorkingExperience());
            }
            if (mentorProfileRequest.getMentorSkills() != null) {
                List<MentorSkillDto> mentorUpdateSkills = mentorProfileRequest.getMentorSkills();
                Set<Long> skillIds = new HashSet<>();
                List<MentorSkillDto> mentorUpdateSkillsSkillDtoList = new ArrayList<>();
                for (MentorSkillDto mentorUpdateSkill : mentorUpdateSkills) {
                    if (mentorUpdateSkill.getYearOfExperiences() <= 0) {
                        throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Invalid.NEGATIVE_YEAR_OF_EXPERIENCES) + mentorUpdateSkill.getYearOfExperiences());
                    }
                    ZonedDateTime userBirthYear = mentorProfile.getUser().getBirthday().atZone(ZoneOffset.UTC);
                    int userAge = Year.now().getValue() - userBirthYear.getYear();
                    boolean validMaximumYearOfExperience = userAge - mentorUpdateSkill.getYearOfExperiences() > 1;
                    if (!validMaximumYearOfExperience) {
                        throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Invalid.INVALID_YEAR_OF_EXPERIENCES) + mentorUpdateSkill.getYearOfExperiences());
                    }
                    if (mentorUpdateSkill.getSkillId() == null) {
                        throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Empty.EMPTY_SKILL));
                    }
                    if (!skillIds.add(mentorUpdateSkill.getSkillId())) {
                        throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage(messageUtil.getLocalMessage(SUBJECT_ID_DUPLICATE) + mentorUpdateSkill.getSkillId());
                    }
                    MentorSkillDto mentorSkillDto = ObjectUtil.copyProperties(mentorUpdateSkill, new MentorSkillDto(), MentorSkillDto.class);
                    mentorSkillDto.setStatus(mentorUpdateSkill.getStatus());
                    mentorUpdateSkillsSkillDtoList.add(mentorSkillDto);

                }
                List<MentorSkillDto> mentorSkills = new ArrayList<>();
                mentorSkills.addAll(mentorUpdateSkillsSkillDtoList);
//                for (MentorSkillDto mentorUpdateSkill : mentorUpdateSkills) {
//                    MentorSkillDto mentorSkillDto = ObjectUtil.copyProperties(mentorUpdateSkill, new MentorSkillDto(), MentorSkillDto.class);
//                    mentorSkills.add(mentorSkillDto);
//                }
//                mentorProfileDto.setMentorSkills(mentorSkills);
                mentorProfileDto.setMentorSkills(mentorSkills);
            }
            userDtoEdit.setMentorProfile(mentorProfileDto);
        }

    }

    public void mentorCreateEditImage(UserDto userDtoEdit, UserDtoRequest request) {
        List<ImageDto> imageDtoList = new ArrayList<>();
        List<ImageEditDto> imageDtoEditRequest = request.getUserImages();
        if (imageDtoEditRequest != null) {
            for (ImageEditDto imageEditDto : imageDtoEditRequest) {
                if (imageEditDto.getId() != null) {
                    ImageDto imageDto = ObjectUtil.copyProperties(imageEditDto, new ImageDto(), ImageDto.class);
                    imageDtoList.add(imageDto);
                }
            }
        }
        userDtoEdit.setUserImages(imageDtoList);
    }

    @Override
    public Boolean mentorSendEditProfileRequest(Long mentorProfileEditId) {
        User user = SecurityUtil.getCurrentUser();
        MentorProfile mentorProfile = user.getMentorProfile();

        MentorProfileEdit mentorProfileEdit = mentorProfileEditRepository.findById(mentorProfileEditId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage("Không tìm thấy hồ sơ chỉnh sửa !"));

        MentorProfileEdit byStatusPending = mentorProfileEditRepository.findByMentorProfileAndStatus(mentorProfile, EMentorProfileEditStatus.PENDING);

        if (byStatusPending != null) {
            throw ApiException.create(HttpStatus.BAD_REQUEST)
                    .withMessage("Hồ sơ của bạn đang được xử lý! Không thể gửi thêm yêu cầu!");
        }

        mentorProfileEdit.setStatus(EMentorProfileEditStatus.PENDING);
        mentorProfileEditRepository.save(mentorProfileEdit);
        return true;
    }

    @Override
    public ApiPage<MentorEditProfileResponse> managerGetEditProfileRequest(ManagerSearchEditProfileRequest query,
                                                                           Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        List<MentorProfileEdit> allByStatus = mentorProfileEditRepository.findAllByStatus(query.getStatus());

        List<MentorEditProfileResponse> mentorEditProfileResponses = new ArrayList<>();
        allByStatus.forEach(mentorProfileEdit -> {
            try {
                MentorEditProfileResponse mentorEditProfileResponse = new MentorEditProfileResponse();
                String profileData = mentorProfileEdit.getProfileData();
                UserDto userDto;
                userDto = objectMapper.readValue(profileData, UserDto.class);
                mentorEditProfileResponse.setId(mentorProfileEdit.getId());
                mentorEditProfileResponse.setUserDto(userDto);
                mentorEditProfileResponses.add(mentorEditProfileResponse);

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        Page<MentorEditProfileResponse> page = PageUtil.toPage(mentorEditProfileResponses, pageable);
        return PageUtil.convert(page);
    }

    @Override
    public MentorEditProfileDetailResponse managerGetEditProfileDetailRequest(Long mentorProfileEditId) throws JsonProcessingException {
        MentorProfileEdit mentorProfileEdit = mentorProfileEditRepository.findById(mentorProfileEditId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage("Không tìm thấy hồ sơ chỉnh sửa !"));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());


        MentorEditProfileDetailResponse mentorEditProfileDetailResponse = new MentorEditProfileDetailResponse();
        mentorEditProfileDetailResponse.setId(mentorProfileEdit.getId());
        //set value profile eidt
        String profileData = mentorProfileEdit.getProfileData();
        UserDto userDtoOrigin = objectMapper.readValue(profileData, UserDto.class);
        mentorEditProfileDetailResponse.setUserDtoEdit(userDtoOrigin);

        //set value profile origin
        MentorProfile mentorProfile = mentorProfileEdit.getMentorProfile();
        User user = mentorProfile.getUser();
        UserDto userDtoEdit = convertUsertoUserDto(user);
        mentorEditProfileDetailResponse.setUserDtoOrigin(userDtoEdit);


        List<String> compare = compare(userDtoOrigin, userDtoEdit);
        mentorEditProfileDetailResponse.setDifferentFields(compare);

        return mentorEditProfileDetailResponse;
    }

    public List<String> compare(UserDto userDtoOrigin, UserDto userDtoEdit) {
        List<String> differentFields = new ArrayList<>();

        if (!Objects.equals(userDtoOrigin.getId(), userDtoEdit.getId())) {
            differentFields.add("id");
        }
        if (!Objects.equals(userDtoOrigin.getFullName(), userDtoEdit.getFullName())) {
            differentFields.add("Họ tên");
        }
        if (!Objects.equals(userDtoOrigin.getBirthday(), userDtoEdit.getBirthday())) {
            differentFields.add("Ngày sinh");
        }
        if (!Objects.equals(userDtoOrigin.getAddress(), userDtoEdit.getAddress())) {
            differentFields.add("Địa chỉ");
        }
        if (!Objects.equals(userDtoOrigin.getPhone(), userDtoEdit.getPhone())) {
            differentFields.add("Số điện thoại");
        }
        if (!Objects.equals(userDtoOrigin.getGender(), userDtoEdit.getGender())) {
            differentFields.add("Giới tính");
        }
        if (!Objects.equals(userDtoOrigin.getUserImages().size(), userDtoEdit.getUserImages().size())) {
            differentFields.add("Hình ảnh");
        }
        if (!Objects.equals(userDtoOrigin.getFacebookLink(), userDtoEdit.getFacebookLink())) {
            differentFields.add("Facebook");
        }
        if (!Objects.equals(userDtoOrigin.getLinkedinLink(), userDtoEdit.getLinkedinLink())) {
            differentFields.add("LinkedIn");
        }
        if (!Objects.equals(userDtoOrigin.getWebsite(), userDtoEdit.getWebsite())) {
            differentFields.add("Website riêng");
        }
        if (userDtoOrigin.getMentorProfile() != null && userDtoEdit.getMentorProfile() != null) {

            if (!Objects.equals(userDtoOrigin.getMentorProfile().getIntroduce(), userDtoEdit.getMentorProfile().getIntroduce())) {
                differentFields.add("Giới thiệu");
            }

            if (!Objects.equals(userDtoOrigin.getMentorProfile().getWorkingExperience(),
                    userDtoEdit.getMentorProfile().getWorkingExperience())) {
                differentFields.add("Kinh nghiệm");
            }
            if (userDtoOrigin.getMentorProfile().getMentorSkills() != null) {

                if (!Objects.equals(userDtoOrigin.getMentorProfile().getMentorSkills().size(),
                        userDtoEdit.getMentorProfile().getMentorSkills().size())) {
                    differentFields.add("Chuyên môn");
                }


            }
        }

        return differentFields;
    }

    @Override
    public Boolean approveMentorProfileEdit(Long id, ManagerApprovalEditProfileRequest request) throws JsonProcessingException {
        MentorProfileEdit mentorProfileEdit = mentorProfileEditRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage("Không tìm thấy hồ sơ chỉnh sửa !"));
        mentorProfileEdit.setStatus(request.getStatus());
        Long mentorProfileId = mentorProfileEdit.getMentorProfile().getId();
        MentorProfile mentorProfile = mentorProfileRepository.findById(mentorProfileId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage("Không tìm thấy hồ sơ giáo viên cần chỉnh sửa!"));
        User user = mentorProfile.getUser();
        // handle update profile edit to profile origin
        if (request.getStatus().equals(EMentorProfileEditStatus.APPROVED)) {
            MentorEditProfileDetailResponse mentorEditProfileDetailResponse = managerGetEditProfileDetailRequest(id);
            UserDto userDtoEdit = mentorEditProfileDetailResponse.getUserDtoEdit();
            MentorProfileDTO mentorProfile2 = userDtoEdit.getMentorProfile();
            user.setFullName(userDtoEdit.getFullName());
            user.setAddress(userDtoEdit.getAddress());
            user.setBirthday(userDtoEdit.getBirthday());
            user.setPhone(userDtoEdit.getPhone());
            user.setGender(userDtoEdit.getGender());
            user.setFacebookLink(userDtoEdit.getFacebookLink());
            user.setLinkedinLink(userDtoEdit.getLinkedinLink());
            user.setWebsite(userDtoEdit.getWebsite());

            // handle avt , cccd
            List<ImageDto> userImages = userDtoEdit.getUserImages();
            List<UserImage> userImageList = new ArrayList<>();
            for (ImageDto userImage : userImages) {
                if (userImage.getType().equals(EImageType.AVATAR)){
                    if (!userImage.isStatus()) {
                        UserImage userImage1 = userImageRepository.findById(userImage.getId())
                                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                                        .withMessage("Không tìm thấy hình ảnh cần chỉnh sửa!"));
                        userImage1.setStatus(true);
                        userImageList.add(userImage1);
                    }
                }

            }
            user.getUserImages().addAll(userImageList);

            MentorProfile mentorProfile1 = user.getMentorProfile();
            mentorProfile1.setIntroduce(mentorProfile2.getIntroduce());
            mentorProfile1.setIntroduce(mentorProfile2.getWorkingExperience());

            List<MentorSkillDto> mentorSkills = mentorProfile2.getMentorSkills();

            List<MentorSkill> mentorSkills1 = new ArrayList<>();
            for (MentorSkillDto mentorSkill : mentorSkills) {
                if (!mentorSkill.getStatus()) {
                    MentorSkill mentorSkill1 = mentorSkillRepository.findById(mentorSkill.getId())
                            .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                                    .withMessage("Không tìm thấy môn học cần chỉnh sửa!"));
                    mentorSkill1.setStatus(true);
                    mentorSkills1.add(mentorSkill1);
                }
            }
            mentorProfile.getSkills().addAll(mentorSkills1);
            mentorProfileRepository.save(mentorProfile);
        }
        /**Implement thông báo*/
        if (request.getStatus() != null) {
            Notification notification = NotificationDirector.buildEditApprovalMentorProfile(request.getStatus(), user, mentorProfileId);
            notificationRepository.save(notification);
            webSocketUtil.sendPrivateNotification(notification);
            emailUtil.sendApprovalMentorProfile(mentorProfile, request.getMessage());
        } else {
            System.out.println("Null Reqquest Status or Reqquest Message!!");
        }
        mentorProfileEditRepository.save(mentorProfileEdit);
        return true;
    }

    @Override
    public Boolean getchangEditRequestMentorProfile(Long id) {
        MentorProfile mentorProfile = mentorProfileRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                .withMessage("Không tìm thấy hồ sơ giáo viên!"));
        mentorProfile.setStatus(EMentorProfileStatus.EDITREQUEST);
        mentorProfileRepository.save(mentorProfile);


        User user = mentorProfile.getUser();
        /**Implement thông báo*/

            Notification notification = NotificationDirector.buildManagerToAllowEditMentorProfile(EMentorProfileStatus.EDITREQUEST, user, id);
            notificationRepository.save(notification);
            webSocketUtil.sendPrivateNotification(notification);
            emailUtil.sendApprovalMentorProfile(mentorProfile, NotificationConstant.COURSE_APPROVAL_PROFILE_TITLE);



        return true;
    }
}
