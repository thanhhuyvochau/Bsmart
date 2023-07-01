package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ValidationErrorsException;
import fpt.project.bsmart.entity.constant.EMentorProfileStatus;
import fpt.project.bsmart.entity.dto.MentorProfileDTO;
import fpt.project.bsmart.entity.dto.UserDto;
import fpt.project.bsmart.entity.request.ImageRequest;
import fpt.project.bsmart.entity.request.ManagerApprovalAccountRequest;
import fpt.project.bsmart.entity.request.MentorSearchRequest;
import fpt.project.bsmart.entity.request.UpdateMentorProfileRequest;
import fpt.project.bsmart.entity.response.Mentor.CompletenessMentorProfileResponse;
import fpt.project.bsmart.entity.response.MentorProfileResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMentorProfileService {
    MentorProfileDTO getMentorProfile(Long id);
    ApiPage<MentorProfileDTO> getAllMentors(MentorSearchRequest mentorSearchRequest, Pageable pageable);
    List<MentorProfileResponse> getAllMentorProfiles();
    ApiPage<UserDto> getPendingMentorProfileList(EMentorProfileStatus accountStatus  , Pageable pageable);
    Long approveMentorProfile(Long id,  ManagerApprovalAccountRequest managerApprovalAccountRequest);
    Long updateMentorProfile(UpdateMentorProfileRequest updateMentorProfileRequest);
    List<Long> updateCertificate(List<ImageRequest> imageRequests);

    CompletenessMentorProfileResponse getCompletenessMentorProfile();

    Boolean mentorRequestApprovalAccount(Long id) throws Exception, ValidationErrorsException;
}
