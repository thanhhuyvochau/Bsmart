package fpt.project.bsmart.entity.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ClassAnnouncementRequest {
    @NotNull
    @NotEmpty
    private String content;
    @NotNull
    @NotEmpty
    private String title;
    private Boolean visible = true;

    // Constructors, getters, and setters

    public ClassAnnouncementRequest() {
    }

    public ClassAnnouncementRequest(String content, String title, Boolean visible) {
        this.content = content;
        this.title = title;
        this.visible = visible;
    }

    // Getters and setters

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
