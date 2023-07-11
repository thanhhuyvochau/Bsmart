package fpt.project.bsmart.entity.request.activity;

import fpt.project.bsmart.entity.request.ActivityRequest;

public class MentorCreateAnnouncementForClass extends ActivityRequest {
    private String content;
    private String title;
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
}
