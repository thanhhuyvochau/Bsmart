package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EAssignmentStatus;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.entity.constant.FileType;
import fpt.project.bsmart.entity.request.AddActivityRequest;
import fpt.project.bsmart.entity.request.AddAssignmentRequest;
import fpt.project.bsmart.repository.ActivityRepository;
import fpt.project.bsmart.repository.ActivityTypeRepository;
import fpt.project.bsmart.repository.ClassSectionRepository;
import fpt.project.bsmart.service.IActivityService;
import fpt.project.bsmart.util.SecurityUtil;
import fpt.project.bsmart.util.UrlUtil;
import fpt.project.bsmart.util.adapter.MinioAdapter;
import io.minio.ObjectWriteResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Instant;
import java.util.Objects;

@Service
@Transactional
public class ActivityServiceImpl implements IActivityService {
    @Value("${minio.endpoint}")
    String minioUrl;
    private final ActivityTypeRepository activityTypeRepository;
    private final ActivityRepository activityRepository;
    private final ClassSectionRepository classSectionRepository;
    private final MinioAdapter minioAdapter;

    public ActivityServiceImpl(ActivityTypeRepository activityTypeRepository, ActivityRepository activityRepository, ClassSectionRepository classSectionRepository, MinioAdapter minioAdapter) {
        this.activityTypeRepository = activityTypeRepository;
        this.activityRepository = activityRepository;
        this.classSectionRepository = classSectionRepository;
        this.minioAdapter = minioAdapter;
    }

    @Override
    public Boolean addActivity(AddActivityRequest addActivityRequest) throws IOException {
        User currentUser = SecurityUtil.getCurrentUser();
        ClassSection classSection = classSectionRepository.findById(addActivityRequest.getClassSectionId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                .withMessage("Không tìm thấy section của lớp cần thêm, vui lòng thử lại"));
        Class clazz = classSection.getClazz();
        User mentor = clazz.getSubCourse().getMentor();
        if (!SecurityUtil.isHasRole(currentUser, EUserRole.MANAGER) && !Objects.equals(currentUser.getId(), mentor.getId())) {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage("Bạn không có quyền thao tác với lớp học này!");
        }
        ActivityType activityType = activityTypeRepository.findById(addActivityRequest.getActivityTypeId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage("Không tìm thấy type của hoạt động, vui lòng thử lại"));

        Activity activity = new Activity();
        activity.setClassSection(classSection);
        activity.setName(addActivityRequest.getName());
        activity.setVisible(addActivityRequest.getIsVisible());
        activity.setType(activityType);
        String code = activityType.getCode();
        switch (code) {
            case "QUIZ":
                break;
            case "ASSIGNMENT":
                Assignment assignment = addAssignment((AddAssignmentRequest) addActivityRequest, activity);
                activity.setAssignment(assignment);
                activityRepository.save(activity);
                return true;
            default:
                throw ApiException.create(HttpStatus.NO_CONTENT).withMessage("Loại hoạt động không hợp lệ!");
        }
        return null;
    }

    private Assignment addAssignment(AddAssignmentRequest request, Activity activity) throws IOException {
        Instant now = Instant.now();
        if (request.getStartDate().isBefore(now) || request.getEndDate().isBefore(now)) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage("Ngày mở hoặc đóng không thể trong quá khứ");
        } else if (request.getStartDate().isAfter(request.getEndDate())) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage("Ngày bắt đầu không thể sau ngày kết thúc");
        }
        Assignment assignment = new Assignment();
        assignment.setDescription(request.getDescription());
        assignment.setStartDate(request.getStartDate());
        assignment.setEndDate(request.getEndDate());
        assignment.setEditBeForSubmitMin(request.getEditBeForSubmitMin());
        assignment.setMaxFileSubmit(request.getMaxFileSubmit());
        assignment.setMaxFileSize(request.getMaxFileSize());
        assignment.setStatus(now.equals(request.getStartDate()) ? EAssignmentStatus.OPENING : EAssignmentStatus.PENDING);
        assignment.setActivity(activity);
        MultipartFile[] attachFiles = request.getAttachFiles();
        for (MultipartFile attachFile : attachFiles) {
            String name = attachFile.getOriginalFilename() + "_" + Instant.now().toString();
            ObjectWriteResponse objectWriteResponse = minioAdapter.uploadFile(name, attachFile.getContentType(), attachFile.getInputStream(), attachFile.getSize());
            AssignmentFile assignmentFile = new AssignmentFile();
            assignmentFile.setFileType(FileType.ATTACH);
            assignmentFile.setUrl(UrlUtil.buildUrl(minioUrl, objectWriteResponse));
            assignmentFile.setUser(SecurityUtil.getCurrentUser());
            assignmentFile.setAssignment(assignment);
            assignment.getAssignmentFiles().add(assignmentFile);
        }
        return assignment;
    }
}
