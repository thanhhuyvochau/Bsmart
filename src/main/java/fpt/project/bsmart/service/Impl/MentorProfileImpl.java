package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.MentorProfile;
import fpt.project.bsmart.entity.Role;
import fpt.project.bsmart.entity.Subject;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.entity.dto.MentorProfileDTO;
import fpt.project.bsmart.entity.request.ImageRequest;
import fpt.project.bsmart.entity.request.UpdateMentorProfileRequest;
import fpt.project.bsmart.repository.MentorProfileRepository;
import fpt.project.bsmart.repository.SubjectRepository;
import fpt.project.bsmart.service.IMentorProfileService;
import fpt.project.bsmart.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MentorProfileImpl implements IMentorProfileService {
    private final MentorProfileRepository mentorProfileRepository;
    private final SubjectRepository subjectRepository;
    private final MessageUtil messageUtil;

    public MentorProfileImpl(MentorProfileRepository mentorProfileRepository, SubjectRepository subjectRepository, MessageUtil messageUtil) {
        this.mentorProfileRepository = mentorProfileRepository;
        this.subjectRepository = subjectRepository;
        this.messageUtil = messageUtil;
    }

    private MentorProfile findById(Long id){
        return mentorProfileRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(""));
    }

    @Override
    public MentorProfileDTO getMentorProfile(Long id) {
        MentorProfile mentorProfile = findById(id);
        //Check if mentor profile is not active so only that mentor and admin can access
        if(!mentorProfile.getStatus() || !mentorProfile.getUser().getStatus()){
            User user = SecurityUtil.getCurrentUserAccountLogin();
            List<EUserRole> roleList = user.getRoles().stream()
                    .map(Role::getCode)
                    .collect(Collectors.toList());
            if(!roleList.contains(EUserRole.MANAGER) || !Objects.equals(user.getId(), mentorProfile.getId())){
                throw ApiException.create(HttpStatus.FORBIDDEN).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.FORBIDDEN));
            }
        }
        MentorProfileDTO mentorProfileDTO = ConvertUtil.convertMentorProfileToMentorProfileDto(mentorProfile);
        return mentorProfileDTO;
    }

    @Override
    public List<MentorProfileDTO> getPendingMentorProfileList(){
        List<MentorProfileDTO> mentorProfileDTOList = new ArrayList<>();
        for (MentorProfile mentorProfile : mentorProfileRepository.getPendingMentorProfileList()){
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
        User user = SecurityUtil.getCurrentUserAccountLogin();
        MentorProfile mentorProfile = mentorProfileRepository.getMentorProfileByUser(user)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.MENTOR_PROFILE_NOT_FOUND_BY_USER + user.getId())));
        List<String> errorMessages = new ArrayList<>();
        if(StringUtil.isNullOrEmpty(updateMentorProfileRequest.getIntroduce())){
            errorMessages.add(messageUtil.getLocalMessage(Constants.ErrorMessage.Empty.EMPTY_INTRODUCE));
        }
        if(StringUtil.isNullOrEmpty(updateMentorProfileRequest.getYearOfExperiences())){
            errorMessages.add(messageUtil.getLocalMessage(Constants.ErrorMessage.Empty.EMPTY_YEAR_OF_EXPERIENCE));
        }
        if(updateMentorProfileRequest.getSubjectIds().isEmpty()){
            errorMessages.add(messageUtil.getLocalMessage(Constants.ErrorMessage.Empty.EMPTY_SKILL));
        }
        List<Subject> skillList = subjectRepository.getSubjectsByIdList(updateMentorProfileRequest.getSubjectIds());
        List<Long> differentIdList = getTheDifferentIdList(updateMentorProfileRequest.getSubjectIds(),skillList);
        if(!differentIdList.isEmpty()){
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.SKILL_NOT_FOUND_BY_ID) + differentIdList);
        }
        mentorProfile.setIntroduce(updateMentorProfileRequest.getIntroduce());
        mentorProfile.setYearsOfExperience(updateMentorProfileRequest.getYearOfExperiences());
        mentorProfile.setSkills(skillList);
        if(!mentorProfile.getStatus() && !user.getStatus()){
            mentorProfile.setStatus(true);
        }
        return mentorProfileRepository.save(mentorProfile).getId();
    }

    private List<Long> getTheDifferentIdList(List<Long> requestIdList, List<Subject> skillList){
        List<Long> skillIdList = skillList.stream()
                .map(Subject::getId)
                .collect(Collectors.toList());
        skillIdList.removeAll(requestIdList);
        return skillIdList;
    }

    @Override
    public List<Long> updateCertificate(List<ImageRequest> imageRequests) {
        return null;
    }
}
