package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "feedback_submit_question")
public class FeedbackSubmitQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "subcourse_feedback_id")
    private SubCourseFeedback subCourseFeedback;
    @ManyToOne
    @JoinColumn(name = "feedback_question_id")
    private FeedbackQuestion question;
    @OneToOne(mappedBy = "submitQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "submit_answer_id")
    private FeedbackSubmitAnswer submitAnswer;
    @Column(name = "filled_answer")
    private String filledAnswer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubCourseFeedback getSubCourseFeedback() {
        return subCourseFeedback;
    }

    public void setSubCourseFeedback(SubCourseFeedback subCourseFeedback) {
        this.subCourseFeedback = subCourseFeedback;
    }

    public FeedbackQuestion getQuestion() {
        return question;
    }

    public void setQuestion(FeedbackQuestion question) {
        this.question = question;
    }

    public FeedbackSubmitAnswer getSubmitAnswer() {
        return submitAnswer;
    }

    public void setSubmitAnswer(FeedbackSubmitAnswer submitAnswer) {
        this.submitAnswer = submitAnswer;
    }

    public String getFilledAnswer() {
        return filledAnswer;
    }

    public void setFilledAnswer(String filledAnswer) {
        this.filledAnswer = filledAnswer;
    }
}
