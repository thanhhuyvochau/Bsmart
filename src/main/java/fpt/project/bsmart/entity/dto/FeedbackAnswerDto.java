package fpt.project.bsmart.entity.dto;

import java.util.HashMap;
import java.util.List;

public class FeedbackAnswerDto {
    private Long id;
    private FeedbackTemplateDto feedbackTemplate;
    private List<String> answers;
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FeedbackTemplateDto getFeedbackTemplate() {
        return feedbackTemplate;
    }

    public void setFeedbackTemplate(FeedbackTemplateDto feedbackTemplate) {
        this.feedbackTemplate = feedbackTemplate;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
