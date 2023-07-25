package fpt.project.bsmart.validator;

import fpt.project.bsmart.entity.AssignmentFile;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.constant.FileType;
import fpt.project.bsmart.util.SecurityUtil;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AssignmentFileValidator {
    public static boolean isOwnerOfFile(AssignmentFile assignmentFile) {
        FileType fileType = assignmentFile.getFileType();
        User user = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        switch (fileType) {
            case ATTACH:
                User creator = assignmentFile.getAssignment().getActivity().getCourse().getCreator();
                return Objects.equals(user.getId(), creator.getId());
            case SUBMIT:
                User student = assignmentFile.getAssignmentSubmition().getStudentClass().getStudent();
                return Objects.equals(user.getId(), student.getId());
            default:
                return false;
        }
    }

}
