package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.common.EPageContent;
import io.swagger.v3.oas.annotations.media.Schema;

public class PageContentRequest {

    @Schema(readOnly = true)
    private Long id;
    private EPageContent type;

    private String content;

    private Boolean isVisible  ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EPageContent getType() {
        return type;
    }

    public void setType(EPageContent type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }
}
