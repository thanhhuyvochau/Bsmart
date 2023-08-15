package fpt.project.bsmart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ValidationErrorsException;
import fpt.project.bsmart.entity.dto.MentorProfileDTO;
import fpt.project.bsmart.entity.dto.UserDto;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.entity.request.User.MentorSendAddSkill;
import fpt.project.bsmart.entity.request.mentorprofile.ManagerSearchEditProfileRequest;
import fpt.project.bsmart.entity.request.mentorprofile.UserDtoRequest;
import fpt.project.bsmart.entity.response.mentor.*;
import fpt.project.bsmart.entity.response.MentorProfileResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMentorProfileService {
    MentorProfileDTO getMentorProfile(Long id);
    ApiPage<MentorProfileDTO> getAllMentors(MentorSearchRequest mentorSearchRequest, Pageable pageable);
    List<MentorProfileResponse> getAllMentorProfiles();
    ApiPage<UserDto> getPendingMentorProfileList(MentorSearchRequest request  , Pageable pageable);
    Long approveMentorProfile(Long id,  ManagerApprovalAccountRequest managerApprovalAccountRequest);
    Long updateMentorProfile(UpdateMentorProfileRequest updateMentorProfileRequest);
    List<Long> updateCertificate(List<ImageRequest> imageRequests);

    CompletenessMentorProfileResponse getCompletenessMentorProfile();

    Boolean mentorRequestApprovalAccount(Long id) throws Exception, ValidationErrorsException;

    Boolean mentorCreateApprovalSkill( MentorSendAddSkill mentorSendAddSkill);

    Boolean managerHandleRequestApprovalSkill(Long id, ManagerApprovalSkillRequest managerApprovalSkillRequest);

    ApiPage<ManagerGetRequestApprovalSkillResponse> managerGetRequestApprovalSkill(Pageable pageable);

    Boolean mentorRequestApprovalSkill( MentorSendSkillRequest mentorSendAddSkill);

    List<MentorGetRequestApprovalSkillResponse> ManagerGetRequestApprovalSkillResponse();

    Long mentorCreateEditProfileRequest(UserDtoRequest request) throws JsonProcessingException;

    Boolean mentorSendEditProfileRequest(Long mentorProfileEditId);

    ApiPage<MentorEditProfileResponse> managerGetEditProfileRequest(ManagerSearchEditProfileRequest query  , Pageable pageable) ;
    MentorEditProfileDetailResponse managerGetEditProfileDetailRequest(Long mentorProfileEditId) throws JsonProcessingException;
}
