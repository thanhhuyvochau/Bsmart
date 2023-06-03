package fpt.project.bsmart.entity.request.feedback;

import java.util.ArrayList;
import java.util.List;

public class SubCourseFeedbackRequest {
    private Long classID;

    private String opinion;

    private SubmitSubCourseFeedback submitSubCourseFeedback;

    public Long getClassID() {
        return classID;
    }

    public void setClassID(Long classID) {
        this.classID = classID;
    }


    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public SubmitSubCourseFeedback getSubmitSubCourseFeedback() {
        return submitSubCourseFeedback;
    }

    public void setSubmitSubCourseFeedback(SubmitSubCourseFeedback submitSubCourseFeedback) {
        this.submitSubCourseFeedback = submitSubCourseFeedback;
    }

    public class SubmitSubCourseFeedback{
        private Long templateId;
        private List<FeedbackSubmitQuestion> submitQuestions = new ArrayList<>();

        public Long getTemplateId() {
            return templateId;
        }

        public void setTemplateId(Long templateId) {
            this.templateId = templateId;
        }

        public List<FeedbackSubmitQuestion> getSubmitQuestions() {
            return submitQuestions;
        }

        public void setSubmitQuestions(List<FeedbackSubmitQuestion> submitQuestions) {
            this.submitQuestions = submitQuestions;
        }

    }

    public class FeedbackSubmitQuestion {
        private Long questionId;
        private SubmitFeedbackAnswer submitAnswers;

        public Long getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Long questionId) {
            this.questionId = questionId;
        }

        public SubmitFeedbackAnswer getSubmitAnswers() {
            return submitAnswers;
        }

        public void setSubmitAnswers(SubmitFeedbackAnswer submitAnswers) {
            this.submitAnswers = submitAnswers;
        }
    }

    public class SubmitFeedbackAnswer {
        public Long submitAnswerId;
        private String submitFilledAnswer;

        public Long getSubmitAnswerId() {
            return submitAnswerId;
        }

        public void setSubmitAnswerId(Long submitAnswerId) {
            this.submitAnswerId = submitAnswerId;
        }

        public String getSubmitFilledAnswer() {
            return submitFilledAnswer;
        }

        public void setSubmitFilledAnswer(String submitFilledAnswer) {
            this.submitFilledAnswer = submitFilledAnswer;
        }
    }
}
