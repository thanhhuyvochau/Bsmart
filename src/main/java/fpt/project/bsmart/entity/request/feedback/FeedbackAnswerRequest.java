package fpt.project.bsmart.entity.request.feedback;

import java.util.List;

public class FeedbackAnswerRequest {
    private Long TemplateId;
    private List<String> answer;

    public Long getTemplateId() {
        return TemplateId;
    }

    public void setTemplateId(Long templateId) {
        TemplateId = templateId;
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }
}
