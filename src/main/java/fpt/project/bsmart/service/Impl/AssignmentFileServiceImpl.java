package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Activity;
import fpt.project.bsmart.entity.Assignment;
import fpt.project.bsmart.entity.AssignmentFile;
import fpt.project.bsmart.entity.AssignmentSubmition;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.ECourseClassStatus;
import fpt.project.bsmart.entity.constant.FileType;
import fpt.project.bsmart.repository.AssignmentFileRepository;
import fpt.project.bsmart.service.IAssignmentFileService;
import fpt.project.bsmart.util.Constants;
import fpt.project.bsmart.util.MessageUtil;
import fpt.project.bsmart.validator.AssignmentFileValidator;
import fpt.project.bsmart.validator.AssignmentValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AssignmentFileServiceImpl implements IAssignmentFileService {
    private final AssignmentFileRepository assignmentFileRepository;
    private final MessageUtil messageUtil;

    public AssignmentFileServiceImpl(AssignmentFileRepository assignmentFileRepository, MessageUtil messageUtil) {
        this.assignmentFileRepository = assignmentFileRepository;
        this.messageUtil = messageUtil;
    }

    @Override
    public boolean deleteAssignmentFile(List<Long> ids) {
        List<AssignmentFile> assignmentFiles = assignmentFileRepository.findAllById(ids);
        if (assignmentFiles.size() != ids.size()) {
            throw ApiException.create(HttpStatus.NOT_FOUND)
                    .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.ASSINGMENT_FILE_NOT_FOUNND_BY_ID));
        }

        for (AssignmentFile assignmentFile : assignmentFiles) {
            if (!AssignmentFileValidator.isOwnerOfFile(assignmentFile)) {
                throw ApiException.create(HttpStatus.CONFLICT)
                        .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.ASSIGNMENT_FILE_NOT_BELONG));
            }
            AssignmentSubmition assignmentSubmition = assignmentFile.getAssignmentSubmition();
            FileType fileType = assignmentFile.getFileType();
            Assignment assignment = assignmentSubmition.getAssignment();

            if (fileType.equals(FileType.ATTACH)) {
                Activity activity = assignment.getActivity();
                ECourseClassStatus status = activity.getCourse().getStatus();
                if (activity.getFixed() && (!status.equals(ECourseClassStatus.REQUESTING) && !status.equals(ECourseClassStatus.EDITREQUEST))) {
                    throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không thể thay đổi nội dung vì trạng thái khóa học không hợp lệ");
                }
            }
            if (fileType.equals(FileType.SUBMIT)) {
                if (!AssignmentValidator.isValidTimeToEdit(assignment.getEditBeForSubmitMin(), assignmentSubmition.getCreated(), Instant.now())) {
                    throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không thể thay đổi bài nộp vì đã vượt qua thời gian cho phép");
                }
            }
        }
        assignmentFileRepository.deleteAllById(ids);
        return true;
    }
}
