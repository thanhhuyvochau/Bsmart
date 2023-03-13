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

    @Schema(description = "Số thứ tự")
    private Integer numericalOrder;

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

    public Integer getNumericalOrder() {
        return numericalOrder;
    }

    public void setNumericalOrder(Integer numericalOrder) {
        this.numericalOrder = numericalOrder;
    }
}
