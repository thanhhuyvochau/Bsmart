package fpt.project.bsmart.entity.request.mentorprofile;

import fpt.project.bsmart.entity.constant.EImageType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;


public class ImageEditDto implements Serializable {

    private Long id;

    @Schema(description = "Tên ảnh đại diện")
    private String name;

    @Schema(description = "URL ảnh đại diện")
    private String url;

    private boolean status;

    private EImageType type;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public EImageType getType() {
        return type;
    }

    public void setType(EImageType type) {
        this.type = type;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
