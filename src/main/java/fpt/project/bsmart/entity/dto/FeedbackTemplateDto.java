package fpt.project.bsmart.entity.dto;

import java.util.ArrayList;
import java.util.List;

public class FeedbackTemplateDto {
    private Long id;
    private String templateName;
    private List<FeedbackQuestionDto> questions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public List<FeedbackQuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<FeedbackQuestionDto> questions) {
        this.questions = questions;
    }
}