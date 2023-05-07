package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EAssignmentStatus;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.entity.constant.FileType;
import fpt.project.bsmart.entity.dto.ActivityDto;
import fpt.project.bsmart.entity.request.ActivityRequest;
import fpt.project.bsmart.entity.request.AssignmentRequest;
import fpt.project.bsmart.repository.ActivityRepository;
import fpt.project.bsmart.repository.ActivityTypeRepository;
import fpt.project.bsmart.repository.ClassSectionRepository;
import fpt.project.bsmart.service.IActivityService;
import fpt.project.bsmart.util.ConvertUtil;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public Boolean editActivity(ActivityRequest activityRequest) throws IOException {
        User currentUser = SecurityUtil.getCurrentUser();
        ClassSection classSection = classSectionRepository.findById(activityRequest.getClassSectionId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                .withMessage("Không tìm thấy section của lớp cần thêm, vui lòng thử lại"));
        Class clazz = classSection.getClazz();
        User mentor = clazz.getSubCourse().getMentor();
        if (!SecurityUtil.isHasAnyRole(currentUser, EUserRole.MANAGER) && !Objects.equals(currentUser.getId(), mentor.getId())) {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage("Bạn không có quyền thao tác với lớp học này!");
        }
        ActivityType activityType = activityTypeRepository.findById(activityRequest.getActivityTypeId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage("Không tìm thấy type của hoạt động, vui lòng thử lại"));

        Activity activity = new Activity(activityRequest.getName(), activityType, activityRequest.getIsVisible(), classSection);
        String code = activityType.getCode();
        switch (code) {
            case "QUIZ":
                break; // Xử lý tương tự cho quiz activity ở đây
            case "ASSIGNMENT":
                Assignment assignment = addAssignment((AssignmentRequest) activityRequest, activity);
                activity.setAssignment(assignment);
                activityRepository.save(activity);
                return true;
            default:
                throw ApiException.create(HttpStatus.NO_CONTENT).withMessage("Loại hoạt động không hợp lệ!");
        }
        return false;
    }

    private Assignment addAssignment(AssignmentRequest request, Activity activity) throws IOException {
        Instant now = Instant.now();
        Instant startDate = request.getStartDate();
        Instant endDate = request.getEndDate();

        if (startDate.isBefore(now) || endDate.isBefore(now) || startDate.isAfter(endDate)) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage("Ngày không hợp lệ");
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
        // Lấy file đính kèm của assignment
        MultipartFile[] attachFiles = request.getAttachFiles();
        for (MultipartFile attachFile : attachFiles) {
            assignment.getAssignmentFiles().add(createAssignmentFile(attachFile, assignment));
        }
        return assignment;
    }

    private AssignmentFile createAssignmentFile(MultipartFile attachFile, Assignment assignment) throws IOException {
        String originalFilename = attachFile.getOriginalFilename();
        String name = originalFilename + "_" + Instant.now().toString();
        ObjectWriteResponse objectWriteResponse = minioAdapter.uploadFile(name, attachFile.getContentType(), attachFile.getInputStream(), attachFile.getSize());

        AssignmentFile assignmentFile = new AssignmentFile();
        assignmentFile.setName(originalFilename);
        assignmentFile.setFileType(FileType.ATTACH);
        assignmentFile.setUrl(UrlUtil.buildUrl(minioUrl, objectWriteResponse));
        assignmentFile.setUser(SecurityUtil.getCurrentUser());
        assignmentFile.setAssignment(assignment);
        return assignmentFile;
    }

    @Override
    public Boolean deleteActivity(Long id) {
        Activity activity = activityRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy hoạt động với id:" + id));
        User subCourseMentor = activity.getClassSection().getClazz().getSubCourse().getMentor();
        User currentUser = SecurityUtil.getCurrentUser();
        if (!Objects.equals(subCourseMentor.getId(), currentUser.getId()) || !SecurityUtil.isHasAnyRole(currentUser, EUserRole.MANAGER, EUserRole.ADMIN)) {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage("Bạn không có quyền chỉnh sửa  hoạt động của lớp học này!");
        }
        activityRepository.delete(activity);
        return true;
    }

    @Override
    public Boolean changeActivityVisible(Long id) {
        Activity activity = activityRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy hoạt động với id:" + id));
        User subCourseMentor = activity.getClassSection().getClazz().getSubCourse().getMentor();
        User currentUser = SecurityUtil.getCurrentUser();
        if (!Objects.equals(subCourseMentor.getId(), currentUser.getId()) || !SecurityUtil.isHasAnyRole(currentUser, EUserRole.MANAGER, EUserRole.ADMIN)) {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage("Bạn không có quyền chỉnh sửa  hoạt động của lớp học này!");
        }
        activity.setIsVisible(!activity.getIsVisible());
        return true;
    }

    @Override
    public Boolean editActivity(Long id, ActivityRequest activityRequest) throws IOException {
        User currentUser = SecurityUtil.getCurrentUser();
        ClassSection classSection = classSectionRepository.findById(activityRequest.getClassSectionId()).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                .withMessage("Không tìm thấy section của lớp cần thêm, vui lòng thử lại"));
        Class clazz = classSection.getClazz();
        User mentor = clazz.getSubCourse().getMentor();
        if (!SecurityUtil.isHasAnyRole(currentUser, EUserRole.MANAGER) && !Objects.equals(currentUser.getId(), mentor.getId())) {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage("Bạn không có quyền thao tác với lớp học này!");
        }
        ActivityType activityType = activityTypeRepository.findById(activityRequest.getActivityTypeId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage("Không tìm thấy type của hoạt động, vui lòng thử lại"));

        Activity activity = activityRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy hoạt động với id:" + id));
        String code = activityType.getCode();
        switch (code) {
            case "QUIZ":
                break; // Xử lý tương tự cho quiz activity ở đây
            case "ASSIGNMENT":
                Assignment assignment = editAssignment((AssignmentRequest) activityRequest, activity);
                activity.setAssignment(assignment);
                activityRepository.save(activity);
                return true;
            default:
                throw ApiException.create(HttpStatus.NO_CONTENT).withMessage("Loại hoạt động không hợp lệ!");
        }
        return false;
    }

    private Assignment editAssignment(AssignmentRequest request, Activity activity) throws IOException {
        Instant now = Instant.now();
        Instant startDate = request.getStartDate();
        Instant endDate = request.getEndDate();

        if (startDate.isBefore(now) || endDate.isBefore(now) || startDate.isAfter(endDate)) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage("Ngày không hợp lệ");
        }
        Assignment assignment = activity.getAssignment();
        assignment.setDescription(request.getDescription());
        assignment.setStartDate(request.getStartDate());
        assignment.setEndDate(request.getEndDate());
        assignment.setEditBeForSubmitMin(request.getEditBeForSubmitMin());
        assignment.setMaxFileSubmit(request.getMaxFileSubmit());
        assignment.setMaxFileSize(request.getMaxFileSize());
        assignment.setStatus(now.equals(request.getStartDate()) ? EAssignmentStatus.OPENING : EAssignmentStatus.PENDING);
        // Lấy file đính kèm của assignment
        MultipartFile[] attachFiles = request.getAttachFiles();
        List<AssignmentFile> existedAssignmentFiles = assignment.getAssignmentFiles();
        Map<String, AssignmentFile> assignmentMapByName = existedAssignmentFiles.stream().collect(Collectors.toMap(AssignmentFile::getName, Function.identity()));
        for (MultipartFile attachFile : attachFiles) {
            AssignmentFile newAssignmentFile = createAssignmentFile(attachFile, assignment);
            AssignmentFile existedAssignment = assignmentMapByName.get(newAssignmentFile.getName());
            if (existedAssignment != null) {
                if (request.getIsOverWriteAttachFile()) {
                    existedAssignmentFiles.remove(existedAssignment);
                    existedAssignmentFiles.add(newAssignmentFile);
                } else {
                    throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Không thể upload do trùng tên file, vui lòng đổi tên hoặc chọn ghi đè! ");
                }
            } else {
                existedAssignmentFiles.add(newAssignmentFile);
            }
        }
        return assignment;
    }

    @Override
    public ActivityDto getDetailActivity(Long id) {
        Activity activity = activityRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy hoạt động với id:" + id));
        Class clazz = activity.getClassSection().getClazz();
        User currentUser = SecurityUtil.getCurrentUser();

        if (SecurityUtil.isHasAnyRole(currentUser, EUserRole.MANAGER, EUserRole.ADMIN)) {
            return ConvertUtil.convertActivityToDto(activity);
        }

        ActivityDto activityDto = ConvertUtil.convertActivityToDto(activity);
        if (SecurityUtil.isHasAnyRole(currentUser, EUserRole.TEACHER) && Objects.equals(clazz.getSubCourse().getMentor().getId(), currentUser.getId())) {
            return activityDto;
        }

        boolean isStudentOfClass = clazz.getStudentClasses().stream().anyMatch(studentClass -> Objects.equals(studentClass.getStudent().getId(), currentUser.getId()));
        if (SecurityUtil.isHasAnyRole(currentUser, EUserRole.STUDENT) && isStudentOfClass) {
            return activityDto;
        } else {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage("Bạn không thể xem hoạt động của lớp này");
        }
    }
}
