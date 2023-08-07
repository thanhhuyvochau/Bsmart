package fpt.project.bsmart.entity.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentSubmitFeedbackRequest {
    private Integer courseRate = 1;
    private Integer mentorRate = 1;
    private String comment;
    private List<SubmittedAnswer> submittedAnswers = new ArrayList<>();

    public Integer getCourseRate() {
        return courseRate;
    }

    public void setCourseRate(Integer courseRate) {
        this.courseRate = courseRate;
    }

    public Integer getMentorRate() {
        return mentorRate;
    }

    public void setMentorRate(Integer mentorRate) {
        this.mentorRate = mentorRate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<SubmittedAnswer> getSubmittedAnswers() {
        return submittedAnswers;
    }

    public void setSubmittedAnswers(List<SubmittedAnswer> submittedAnswers) {
        this.submittedAnswers = submittedAnswers;
    }

    public static class  SubmittedAnswer{
        private Long questionId ;
        private Long answerId ;

        public Long getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Long questionId) {
            this.questionId = questionId;
        }

        public Long getAnswerId() {
            return answerId;
        }

        public void setAnswerId(Long answerId) {
            this.answerId = answerId;
        }
    }



}
