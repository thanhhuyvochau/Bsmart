package fpt.project.bsmart.entity.request;


import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class AssignmentRequest extends ActivityRequest {

    private String description;


    private Integer editBeForSubmitMin = 0;

    private Integer maxFileSubmit = 1;

    private Integer maxFileSize = 5; // đơn vị MB

    private List<MultipartFile> attachFiles = new ArrayList<>();
    private Long passPoint = 5L;
    private String password;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<MultipartFile> getAttachFiles() {
        return attachFiles;
    }

    public void setAttachFiles(List<MultipartFile> attachFiles) {
        this.attachFiles = attachFiles;
    }

    public Long getPassPoint() {
        return passPoint;
    }

    public void setPassPoint(Long passPoint) {
        this.passPoint = passPoint;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
