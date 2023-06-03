package fpt.project.bsmart.entity;

import javax.persistence.*;

@Entity
@Table(name = "feedback_answer")
public class FeedbackAnswer extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "answer")
    private String answer;

    @Column(name = "score")
    private Long score;

    @ManyToOne
    @JoinColumn(name = "feedback_question_id")
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

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public FeedbackQuestion getQuestion() {
        return question;
    }

    public void setQuestion(FeedbackQuestion question) {
        this.question = question;
    }
}
