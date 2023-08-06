package fpt.project.bsmart.entity;

import javax.persistence.*;

@Entity(name = "feedback_submit_answer")
public class FeedbackSubmitAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "submission")
    private FeedbackSubmission submission;
    @ManyToOne
    @JoinColumn(name = "answer")
    private FeedbackAnswer answer;

    @Column(name = "answerEssay")
    private String answerEssay;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FeedbackSubmission getSubmission() {
        return submission;
    }

    public void setSubmission(FeedbackSubmission submission) {
        this.submission = submission;
    }

    public FeedbackAnswer getAnswer() {
        return answer;
    }

    public void setAnswer(FeedbackAnswer answer) {
        this.answer = answer;
    }

    public String getAnswerEssay() {
        return answerEssay;
    }

    public void setAnswerEssay(String answerEssay) {
        this.answerEssay = answerEssay;
    }
}
