package fpt.project.bsmart.entity;

import javax.persistence.*;

@Entity
@Table(name = "feedback_submit_answer")
public class FeedbackSubmitAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "feedback_submission")
    private SubCourseFeedback feedbackSubmission;
    @ManyToOne
    @JoinColumn(name = "feedback_question")
    private FeedbackQuestion question;
    @ManyToOne
    @JoinColumn(name = "submit_answer")
    private FeedbackAnswer submitAnswer;
    @Column(name = "filled_answer")
    private String filledAnswer;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubCourseFeedback getFeedbackSubmission() {
        return feedbackSubmission;
    }

    public void setFeedbackSubmission(SubCourseFeedback feedbackSubmission) {
        this.feedbackSubmission = feedbackSubmission;
    }

    public FeedbackQuestion getQuestion() {
        return question;
    }

    public void setQuestion(FeedbackQuestion question) {
        this.question = question;
    }

    public FeedbackAnswer getSubmitAnswer() {
        return submitAnswer;
    }

    public void setSubmitAnswer(FeedbackAnswer submitAnswer) {
        this.submitAnswer = submitAnswer;
    }

    public String getFilledAnswer() {
        return filledAnswer;
    }

    public void setFilledAnswer(String filledAnswer) {
        this.filledAnswer = filledAnswer;
    }


}
