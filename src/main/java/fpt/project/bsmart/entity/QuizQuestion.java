package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.QuestionType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz_question")
public class QuizQuestion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
    @Column(name = "question")
    private String question;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private QuestionType type;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quizQuestion", orphanRemoval = true)
    private List<QuizAnswer> answers = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public List<QuizAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuizAnswer> answers) {
        this.answers = answers;
    }
}
