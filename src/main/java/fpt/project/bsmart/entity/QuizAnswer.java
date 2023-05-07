package fpt.project.bsmart.entity;

import javax.persistence.*;

@Entity
@Table(name = "quiz_answer")
public class QuizAnswer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "answer")
    private String answer;
    @Column(name = "is_right")
    private Boolean isRight = false;
    @ManyToOne
    @JoinColumn(name = "quiz_question_id")
    private QuizQuestion quizQuestion;

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

    public Boolean getIsRight() {
        return isRight;
    }

    public void setIsRight(Boolean right) {
        isRight = right;
    }
}
