package fpt.project.bsmart.entity;

import javax.persistence.*;

@Entity
@Table(name = "feedback_submit_answer")
public class FeedbackSubmitAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "submit_answer_id")
    private FeedbackSubmitQuestion submitQuestion;
    @ManyToOne
    @JoinColumn(name = "feedback_answer")
    private FeedbackAnswer answer;
    @Column(name = "submit_answer")
    private String submitAnswer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FeedbackSubmitQuestion getSubmitQuestion() {
        return submitQuestion;
    }

    public void setSubmitQuestion(FeedbackSubmitQuestion submitQuestion) {
        this.submitQuestion = submitQuestion;
    }

    public FeedbackAnswer getAnswer() {
        return answer;
    }

    public void setAnswer(FeedbackAnswer answer) {
        this.answer = answer;
    }

    public String getSubmitAnswer() {
        return submitAnswer;
    }

    public void setSubmitAnswer(String submitAnswer) {
        this.submitAnswer = submitAnswer;
    }
}
