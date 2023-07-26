package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.AssignmentFile;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.repository.AssignmentFileRepository;
import fpt.project.bsmart.service.IAssignmentFileService;
import fpt.project.bsmart.util.Constants;
import fpt.project.bsmart.util.MessageUtil;
import fpt.project.bsmart.validator.AssignmentFileValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AssignmentFileServiceImpl implements IAssignmentFileService {
    private final AssignmentFileRepository assignmentFileRepository;
    private final MessageUtil messageUtil;

    public AssignmentFileServiceImpl(AssignmentFileRepository assignmentFileRepository, MessageUtil messageUtil) {
        this.assignmentFileRepository = assignmentFileRepository;
        this.messageUtil = messageUtil;
    }

    @Override
    public boolean deleteAssignmentFile(long id) {
        AssignmentFile assignmentFile = assignmentFileRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND)
                        .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.ASSINGMENT_FILE_NOT_FOUNND_BY_ID)));
        if (!AssignmentFileValidator.isOwnerOfFile(assignmentFile)) {
            throw ApiException.create(HttpStatus.CONFLICT)
                    .withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.ASSIGNMENT_FILE_NOT_BELONG));
        }
        assignmentFileRepository.delete(assignmentFile);
        return true;
    }
}
