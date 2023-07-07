package fpt.project.bsmart.validator;

import fpt.project.bsmart.entity.Assignment;
import fpt.project.bsmart.entity.AssignmentFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class AssignmentValidator {

    private static final List<String> validFileTypes = Arrays.asList("docx", "doc", "xlsx", "xls", "csv", "pptx", "ppt");

    public static boolean isValidSubmitDate(Assignment assignment) {
        Instant now = Instant.now();
        Instant startDate = assignment.getStartDate();
        Instant endDate = assignment.getEndDate();
        return now.isAfter(startDate) && now.isBefore(endDate);
    }

    public static boolean isValidNumberOfSubmitFile(Assignment assignment, List<MultipartFile> submitFiles) {
        Integer maxFileSubmit = assignment.getMaxFileSubmit();
        return maxFileSubmit > submitFiles.size();
    }

    public static boolean isValidAllowTimeToEditSubmit(Assignment assignment, AssignmentFile assignmentFile) {
        return false;
    }

    public static boolean isValidFileExtension(Assignment assignment, List<MultipartFile> submitFiles) {
        for (MultipartFile file : submitFiles) {
            if (!file.isEmpty()) {
                if (!validFileTypes.contains(file.getContentType())) {
                    return false;
                }
            }
        }
        return true;
    }
}