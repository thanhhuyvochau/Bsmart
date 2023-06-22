package fpt.project.bsmart.entity.dto;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.project.bsmart.config.json.View;

public class SimpleClassAnnouncementResponse {

    private Long id;
    private String title;
    @JsonView(View.Teacher.class)
    private boolean visible = false;


    // Constructors, getters, and setters

    public SimpleClassAnnouncementResponse() {
    }

// Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
