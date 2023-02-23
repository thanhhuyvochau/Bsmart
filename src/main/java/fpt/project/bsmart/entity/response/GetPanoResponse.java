package fpt.project.bsmart.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.Instant;

public class GetPanoResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    @Schema(description = "Ngày phát hành")
    private Instant publishDate;

    @Schema(description = "Ngày hết hạn")
    private Instant expirationDate;

    private String status ;


    @Schema(description = "Đường dẫn liên kết trong pano")
    private String linkUrl;


//    private ResourceDto resource ;


    private Boolean isVisible;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

//    public ResourceDto getResource() {
//        return resource;
//    }
//
//    public void setResource(ResourceDto resource) {
//        this.resource = resource;
//    }

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }
}
