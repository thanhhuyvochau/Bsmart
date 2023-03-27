package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.dto.MentorProfileDTO;
import fpt.project.bsmart.entity.request.ImageRequest;
import fpt.project.bsmart.entity.request.UpdateMentorProfileRequest;

import java.util.List;

public interface IMentorProfileService {
    MentorProfileDTO getMentorProfile(Long id);
    List<MentorProfileDTO> getAllMentors();
    List<MentorProfileDTO> getPendingMentorProfileList();
    Long approveMentorProfile(Long id);
    Long updateMentorProfile(UpdateMentorProfileRequest updateMentorProfileRequest);
    List<Long> updateCertificate(List<ImageRequest> imageRequests);
}
