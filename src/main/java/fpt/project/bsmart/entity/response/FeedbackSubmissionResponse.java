package fpt.project.bsmart.entity.response;

import fpt.project.bsmart.entity.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

public class FeedbackSubmissionResponse {
    private Long id;
    private UserDto submitBy;
    private Integer mentorRate;
    private Integer courseRate;
    private String comment;
    private List<FeedbackSubmitQuestion> questions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getSubmitBy() {
        return submitBy;
    }

    public void setSubmitBy(UserDto submitBy) {
        this.submitBy = submitBy;
    }

    public Integer getMentorRate() {
        return mentorRate;
    }

    public void setMentorRate(Integer mentorRate) {
        this.mentorRate = mentorRate;
    }

    public Integer getCourseRate() {
        return courseRate;
    }

    public void setCourseRate(Integer courseRate) {
        this.courseRate = courseRate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<FeedbackSubmitQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<FeedbackSubmitQuestion> questions) {
        this.questions = questions;
    }

    public static class FeedbackSubmitQuestion{
        private Long id;
        private String question;
        private List<FeedbackSubmitAnswer> answers = new ArrayList<>();

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public List<FeedbackSubmitAnswer> getAnswers() {
            return answers;
        }

        public void setAnswers(List<FeedbackSubmitAnswer> answers) {
            this.answers = answers;
        }
    }

    public static class FeedbackSubmitAnswer{
        private Long id;
        private String answer;
        private Boolean isChosen;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public Boolean getIsChosen() {
            return isChosen;
        }

        public void setIsChosen(Boolean chosen) {
            isChosen = chosen;
        }
    }
}
