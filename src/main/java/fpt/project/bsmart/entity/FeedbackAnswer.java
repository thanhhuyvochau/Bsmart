package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.EFeedbackAnswerType;

import javax.persistence.*;

@Entity(name = "feedback_answer")
public class FeedbackAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "answer")
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question")
    private FeedbackQuestion question;

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

    public FeedbackQuestion getQuestion() {
        return question;
    }

    public void setQuestion(FeedbackQuestion question) {
        this.question = question;
    }

}
