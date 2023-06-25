package fpt.project.bsmart.entity.request;


import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

public class AssignmentRequest extends ActivityRequest {

    private String description;

    private Instant startDate;

    private Instant endDate;

    private Integer editBeForSubmitMin = 0;

    private Integer maxFileSubmit = 1;

    private Integer maxFileSize = 5; // đơn vị MB

    private MultipartFile[] attachFiles;

    private Boolean isOverWriteAttachFile = false;
    private Long passPoint = 5L;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Integer getEditBeForSubmitMin() {
        return editBeForSubmitMin;
    }

    public void setEditBeForSubmitMin(Integer editBeForSubmitMin) {
        this.editBeForSubmitMin = editBeForSubmitMin;
    }

    public Integer getMaxFileSubmit() {
        return maxFileSubmit;
    }

    public void setMaxFileSubmit(Integer maxFileSubmit) {
        this.maxFileSubmit = maxFileSubmit;
    }

    public Integer getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(Integer maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public MultipartFile[] getAttachFiles() {
        return attachFiles;
    }

    public void setAttachFiles(MultipartFile[] attachFiles) {
        this.attachFiles = attachFiles;
    }

    public Boolean getIsOverWriteAttachFile() {
        return isOverWriteAttachFile;
    }

    public void setIsOverWriteAttachFile(Boolean overWriteAttachFile) {
        isOverWriteAttachFile = overWriteAttachFile;
    }

    public Boolean getOverWriteAttachFile() {
        return isOverWriteAttachFile;
    }

    public void setOverWriteAttachFile(Boolean overWriteAttachFile) {
        isOverWriteAttachFile = overWriteAttachFile;
    }

    public Long getPassPoint() {
        return passPoint;
    }

    public void setPassPoint(Long passPoint) {
        this.passPoint = passPoint;
    }
}
