package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.ClassAnnouncement;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.entity.dto.ClassAnnouncementDto;
import fpt.project.bsmart.entity.dto.SimpleClassAnnouncementResponse;
import fpt.project.bsmart.entity.request.ClassAnnouncementRequest;
import fpt.project.bsmart.repository.ClassAnnouncementRepository;
import fpt.project.bsmart.repository.ClassRepository;
import fpt.project.bsmart.service.ClassAnnouncementService;
import fpt.project.bsmart.util.*;
import fpt.project.bsmart.validator.ClassValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ClassAnnouncementServiceImpl implements ClassAnnouncementService {
    private final ClassAnnouncementRepository classAnnouncementRepository;
    private final ClassRepository classRepository;

    public ClassAnnouncementServiceImpl(ClassAnnouncementRepository classAnnouncementRepository, ClassRepository classRepository) {
        this.classAnnouncementRepository = classAnnouncementRepository;
        this.classRepository = classRepository;
    }

    @Override
    public ApiPage<SimpleClassAnnouncementResponse> getAllClassAnnouncements(Long classId, Pageable pageable) {
        Class clazz = classRepository.findById(classId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Class not found with id:" + classId));
        User user = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        EUserRole memberOfClassAsRole = ClassValidator.isMemberOfClassAsRole(clazz, user);
        if (memberOfClassAsRole == null) {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage("You are not member of this class");
        } else {
            ResponseUtil.responseForRole(memberOfClassAsRole);
        }
        List<ClassAnnouncement> classAnnouncements = clazz.getClassAnnouncements();
        Page<ClassAnnouncement> classAnnouncementPage = new PageImpl<>(classAnnouncements, pageable, classAnnouncements.size());
        return PageUtil.convert(classAnnouncementPage.map(ConvertUtil::convertClassAnnouncementToSimpleResponse));
    }

    @Override
    public ClassAnnouncementDto getClassAnnouncementById(Long classId, Long id) {
        ClassAnnouncement classAnnouncement = classAnnouncementRepository
                .findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage("Announcement not found with id:" + id));
        Class clazz = classAnnouncement.getClazz();
        User user = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        if (!ClassValidator.isMemberOfClass(clazz, user)) {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage("You are not member of this class");
        } else if (!Objects.equals(classId, clazz.getId())) {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage("Invalid classId parameter and class id of class announcement");
        }
        return ConvertUtil.convertClassAnnouncementToDto(classAnnouncement);
    }

    @Override
    public ClassAnnouncementDto saveClassAnnouncement(Long classId, ClassAnnouncementRequest request) {
        Class clazz = classRepository.findById(classId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Class not found with id:" + classId));
        User user = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        if (!ClassValidator.isMentorOfClass(user, clazz)) {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage("You are not member of this class");
        }
        ClassAnnouncement classAnnouncement = ObjectUtil.copyProperties(request, new ClassAnnouncement(), ClassAnnouncement.class, true);
        classAnnouncement.setClazz(clazz);
        ClassAnnouncement save = classAnnouncementRepository.save(classAnnouncement);
        return ConvertUtil.convertClassAnnouncementToDto(save);
    }

    @Override
    public ClassAnnouncementDto updateClassAnnouncement(Long classId, Long id, ClassAnnouncementRequest request) {
        Class clazz = classRepository.findById(classId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Class not found with id:" + classId));
        ClassAnnouncement classAnnouncement = classAnnouncementRepository
                .findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage("Announcement not found with id:" + classId));

        User user = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        if (!ClassValidator.isMentorOfClass(user, clazz)) {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage("You are not mentor of this class");
        }
        classAnnouncement.setContent(request.getContent());
        classAnnouncement.setTitle(request.getTitle());
        classAnnouncement.setVisible(request.getVisible());
        ClassAnnouncement save = classAnnouncementRepository.save(classAnnouncement);
        return ConvertUtil.convertClassAnnouncementToDto(save);
    }

    @Override
    public Boolean deleteClassAnnouncement(Long classId, Long id) {
        ClassAnnouncement classAnnouncement = classAnnouncementRepository
                .findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage("Announcement not found with id:" + id));
        Class clazz = classAnnouncement.getClazz();
        User user = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        if (!ClassValidator.isMemberOfClass(clazz, user)) {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage("You are not mentor of this class");
        }
        clazz.getClassAnnouncements().remove(classAnnouncement);
        classAnnouncementRepository.delete(classAnnouncement);
        return true;
    }
}
