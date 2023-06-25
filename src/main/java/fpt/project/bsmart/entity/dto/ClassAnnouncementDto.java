package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.config.json.View;
import org.codehaus.jackson.map.annotate.JsonView;

public class ClassAnnouncementDto {
    private Long id;
    private String content;
    private String title;
    @JsonView(View.Teacher.class)
    private Boolean visible;

    // Constructors, getters, and setters

    public ClassAnnouncementDto() {
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}
