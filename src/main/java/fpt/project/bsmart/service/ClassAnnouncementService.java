package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.ClassAnnouncementDto;
import fpt.project.bsmart.entity.dto.SimpleClassAnnouncementResponse;
import fpt.project.bsmart.entity.request.ClassAnnouncementRequest;
import org.springframework.data.domain.Pageable;

public interface ClassAnnouncementService {
    ApiPage<ClassAnnouncementDto> getAllClassAnnouncements(Long classId, Pageable pageable);

    SimpleClassAnnouncementResponse getClassAnnouncementById(Long classId, Long id);

    ClassAnnouncementDto saveClassAnnouncement(Long classId, ClassAnnouncementRequest request);

    ClassAnnouncementDto updateClassAnnouncement(Long classId, Long id, ClassAnnouncementRequest request);

    Boolean deleteClassAnnouncement(Long classId, Long id);
}
