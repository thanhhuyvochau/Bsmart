package fpt.project.bsmart.entity.request;

public class UpdateSubCourseFeedbackTemplateRequest {
    private Long feedbackTemplateId;
    private Long subCourseId;

    public Long getFeedbackTemplateId() {
        return feedbackTemplateId;
    }

    public void setFeedbackTemplateId(Long feedbackTemplateId) {
        this.feedbackTemplateId = feedbackTemplateId;
    }

    public Long getSubCourseId() {
        return subCourseId;
    }

    public void setSubCourseId(Long subCourseId) {
        this.subCourseId = subCourseId;
    }
}
