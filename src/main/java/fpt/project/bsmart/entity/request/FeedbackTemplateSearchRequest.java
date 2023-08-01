package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.constant.EFeedbackType;

public class FeedbackTemplateSearchRequest {
    private EFeedbackType type;
    private String name;
    public EFeedbackType getType() {
        return type;
    }

    public void setType(EFeedbackType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
