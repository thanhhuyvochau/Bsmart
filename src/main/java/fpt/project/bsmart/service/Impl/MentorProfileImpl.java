package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.MentorProfileDTO;
import fpt.project.bsmart.entity.request.ImageRequest;
import fpt.project.bsmart.entity.request.MentorSearchRequest;
import fpt.project.bsmart.entity.request.UpdateMentorProfileRequest;
import fpt.project.bsmart.entity.request.UpdateSkillRequest;
import fpt.project.bsmart.entity.response.MentorProfileResponse;
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

import java.time.Year;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

import static fpt.project.bsmart.util.Constants.ErrorMessage.SUBJECT_ID_DUPLICATE;
import static fpt.project.bsmart.util.Constants.ErrorMessage.SUBJECT_NOT_FOUND_BY_ID;

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
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(""));
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
        for (MentorProfile mentorProfile : mentorProfiles){
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
    public List<MentorProfileDTO> getPendingMentorProfileList() {
        List<MentorProfileDTO> mentorProfileDTOList = new ArrayList<>();
        for (MentorProfile mentorProfile : mentorProfileRepository.getPendingMentorProfileList()) {
            mentorProfileDTOList.add(ConvertUtil.convertMentorProfileToMentorProfileDto(mentorProfile));
        }
        return mentorProfileDTOList;
    }

    @Override
    public Long approveMentorProfile(Long id) {
        MentorProfile mentorProfile = findById(id);
        mentorProfile.setStatus(true);
        return mentorProfileRepository.save(mentorProfile).getId();
    }

    @Override
    public Long updateMentorProfile(UpdateMentorProfileRequest updateMentorProfileRequest) {
        User user = SecurityUtil.getCurrentUser();
        MentorProfile mentorProfile = mentorProfileRepository.getMentorProfileByUser(user)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.MENTOR_PROFILE_NOT_FOUND_BY_USER) + user.getId()));

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
                if(mentorUpdateSkill.getYearOfExperiences() <= 0){
                    throw ApiException.create(HttpStatus.BAD_REQUEST)
                            .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Invalid.NEGATIVE_YEAR_OF_EXPERIENCES) + mentorUpdateSkill.getYearOfExperiences());
                }
                ZonedDateTime userBirthYear = mentorProfile.getUser().getBirthday().atZone(ZoneOffset.UTC);
                int userAge = Year.now().getValue() - userBirthYear.getYear();
                boolean validMaximumYearOfExperience = userAge - mentorUpdateSkill.getYearOfExperiences() > 1;
                if(!validMaximumYearOfExperience){
                    throw ApiException.create(HttpStatus.BAD_REQUEST)
                            .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Invalid.INVALID_YEAR_OF_EXPERIENCES) + mentorUpdateSkill.getYearOfExperiences());
                }
                if(mentorUpdateSkill.getSkillId() == null){
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
                mentorSkills.add(mentorSkill);
            }
            mentorProfile.setSkills(mentorSkills);
        }
//        if (!mentorProfile.getStatus() && !user.getStatus()) {
//            mentorProfile.setStatus(true);
//        }


        return mentorProfileRepository.save(mentorProfile).getId();
    }

    @Override
    public List<Long> updateCertificate(List<ImageRequest> imageRequests) {
        return null;
    }
}
