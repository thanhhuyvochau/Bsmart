package fpt.project.bsmart.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;


public class ImageDto implements Serializable {
    @Schema(description = "ID ảnh đại diện", readOnly = true)
    private Long id;

    @Schema(description = "Tên ảnh đại diện")
    private String name;

    @Schema(description = "URL ảnh đại diện")
    private String url;



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


}
