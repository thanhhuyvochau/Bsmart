package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.common.EResourceType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.Instant;

public class PanoDto implements Serializable {
    private static final long serialVersionUID = 1L;


    @Schema(description = "Mã của pano")
    private String name;
    @Schema(description = "Ngày phát hành")
    private Instant publishDate;
    @Schema(description = "Ngày hết hạn")
    private Instant expirationDate;


    private EResourceType resourceType;


    private MultipartFile file ;

    @Schema(description = "Hiển thị")
    private Boolean isVisible;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Instant publishDate) {
        this.publishDate = publishDate;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public EResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(EResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }
}