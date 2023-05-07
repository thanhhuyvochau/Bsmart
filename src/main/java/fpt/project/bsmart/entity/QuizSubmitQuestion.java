package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz_submit_question")
public class QuizSubmitQuestion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "quiz_submittion_id")
    private QuizSubmittion quizSubmittion;
    @ManyToOne
    @JoinColumn(name = "quiz_question_id")
    private QuizQuestion quizQuestion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quizSubmitQuestion", orphanRemoval = true)
    private List<QuizSubmitAnswer> quizSubmitAnswers = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuizSubmittion getQuizSubmittion() {
        return quizSubmittion;
    }

    public void setQuizSubmittion(QuizSubmittion quizSubmittion) {
        this.quizSubmittion = quizSubmittion;
    }

    public QuizQuestion getQuizQuestion() {
        return quizQuestion;
    }

    public void setQuizQuestion(QuizQuestion quizQuestion) {
        this.quizQuestion = quizQuestion;
    }

    public List<QuizSubmitAnswer> getQuizSubmitAnswers() {
        return quizSubmitAnswers;
    }

    public void setQuizSubmitAnswers(List<QuizSubmitAnswer> quizSubmitAnswers) {
        this.quizSubmitAnswers = quizSubmitAnswers;
    }
}
