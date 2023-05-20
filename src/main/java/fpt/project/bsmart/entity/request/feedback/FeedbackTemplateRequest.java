package fpt.project.bsmart.entity.request.feedback;

import fpt.project.bsmart.entity.constant.EFeedbackType;
import fpt.project.bsmart.entity.constant.EUserRole;

import java.util.List;

public class FeedbackTemplateRequest {
    private String templateName;
    private List<Long> questionList;
    private EFeedbackType feedbackType;
    private EUserRole permission;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public List<Long> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Long> questionList) {
        this.questionList = questionList;
    }

    public EFeedbackType getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(EFeedbackType feedbackType) {
        this.feedbackType = feedbackType;
    }

    public EUserRole getPermission() {
        return permission;
    }

    public void setPermission(EUserRole permission) {
        this.permission = permission;
    }
}
