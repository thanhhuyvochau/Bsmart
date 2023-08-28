package fpt.project.bsmart.entity.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class SubmitAssignmentRequest {
    private MultipartFile[] submittedFiles;
    private String note;
    private String password;

    public MultipartFile[] getSubmittedFiles() {
        return submittedFiles;
    }

    public void setSubmittedFiles(MultipartFile[] submittedFiles) {
        this.submittedFiles = submittedFiles;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
