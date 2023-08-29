package fpt.project.bsmart.validator;

import fpt.project.bsmart.entity.Assignment;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

public class AssignmentValidator {

    private static final List<String> validFileTypes = Arrays.asList("docx", "doc", "xlsx", "xls", "csv", "pptx", "ppt", "pdf","zip","rar","png");

//    public static boolean isValidSubmitDate(Assignment assignment) {
//        Instant now = Instant.now();
//        Instant startDate = assignment.getStartDate();
//        Instant endDate = assignment.getEndDate();
//        return now.isAfter(startDate) && now.isBefore(endDate);
//    }

    public static boolean isValidNumberOfSubmitFile(Assignment assignment, List<MultipartFile> submitFiles) {
        Integer maxFileSubmit = assignment.getMaxFileSubmit();
        return maxFileSubmit >= submitFiles.size();
    }

    public static boolean isValidFileExtension(Assignment assignment, List<MultipartFile> submitFiles) {
        for (MultipartFile file : submitFiles) {
            if (!file.isEmpty()) {
                String[] fileParts = file.getOriginalFilename().split("\\.");
                String contentType = fileParts[fileParts.length - 1];
                if (!validFileTypes.contains(contentType)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isValidFileNumber(Integer maxFileSubmit, List<MultipartFile> submitFiles) {
        return maxFileSubmit >= submitFiles.size();
    }

    public static boolean isTotalSizeWithinLimit(Integer totalSizeLimit, List<MultipartFile> files) {
        long totalSize = 0;
        for (MultipartFile part : files) {
            totalSize += part.getSize();
        }
        return totalSize <= totalSizeLimit;
    }

    public static boolean isValidTimeToEdit(Integer editBeForSubmitMin, Instant nearestSubmitDate, Instant now) {
        Duration difference = Duration.between(nearestSubmitDate.truncatedTo(ChronoUnit.MINUTES), now.truncatedTo(ChronoUnit.MINUTES));
        long differenceMinutes = difference.toMinutes();
        return editBeForSubmitMin - differenceMinutes > 0;
    }
}
