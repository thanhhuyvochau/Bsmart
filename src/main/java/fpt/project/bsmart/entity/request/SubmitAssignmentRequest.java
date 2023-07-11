package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.config.json.SecurityJsonViewControllerAdvice;
import fpt.project.bsmart.util.ResponseUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class SubmitAssignmentRequest {
    private List<MultipartFile> submittedFiles = new ArrayList<>();
    private String note;

    public List<MultipartFile> getSubmittedFiles() {
        return submittedFiles;
    }

    public void setSubmittedFiles(List<MultipartFile> submittedFiles) {
        this.submittedFiles = submittedFiles;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
