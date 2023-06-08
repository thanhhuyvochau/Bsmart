package fpt.project.bsmart.entity.request.feedback;

import java.util.ArrayList;
import java.util.List;

public class SubCourseFeedbackRequest {
    private Long subCourseId;

    private String opinion;

    private List<FeedbackSubmitQuestion> submitQuestions = new ArrayList<>();

    public Long getSubCourseId() {
        return subCourseId;
    }

    public void setSubCourseId(Long subCourseId) {
        this.subCourseId = subCourseId;
    }


    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public List<FeedbackSubmitQuestion> getSubmitQuestions() {
        return submitQuestions;
    }

    public void setSubmitQuestions(List<FeedbackSubmitQuestion> submitQuestions) {
        this.submitQuestions = submitQuestions;
    }
}
