package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.constant.EAccountStatus;
import fpt.project.bsmart.entity.dto.MentorProfileDTO;
import fpt.project.bsmart.entity.dto.UserDto;
import fpt.project.bsmart.entity.request.ImageRequest;
import fpt.project.bsmart.entity.request.MentorSearchRequest;
import fpt.project.bsmart.entity.request.UpdateMentorProfileRequest;
import fpt.project.bsmart.entity.response.MentorProfileResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMentorProfileService {
    MentorProfileDTO getMentorProfile(Long id);
    ApiPage<MentorProfileDTO> getAllMentors(MentorSearchRequest mentorSearchRequest, Pageable pageable);
    List<MentorProfileResponse> getAllMentorProfiles();
    ApiPage<UserDto> getPendingMentorProfileList(EAccountStatus accountStatus  , Pageable pageable);
    Long approveMentorProfile(Long id);
    Long updateMentorProfile(UpdateMentorProfileRequest updateMentorProfileRequest);
    List<Long> updateCertificate(List<ImageRequest> imageRequests);
}
