package fpt.project.bsmart.entity;

import javax.persistence.*;

@Entity
@Table(name = "quiz_submit_answer")
public class QuizSubmitAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "quiz_submit_question_id")
    private QuizSubmitQuestion quizSubmitQuestion;
    @ManyToOne
    @JoinColumn(name = "quiz_answer_id")
    private QuizAnswer quizAnswer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuizSubmitQuestion getQuizSubmitQuestion() {
        return quizSubmitQuestion;
    }

    public void setQuizSubmitQuestion(QuizSubmitQuestion quizSubmitQuestion) {
        this.quizSubmitQuestion = quizSubmitQuestion;
    }

    public QuizAnswer getQuizAnswer() {
        return quizAnswer;
    }

    public void setQuizAnswer(QuizAnswer quizAnswer) {
        this.quizAnswer = quizAnswer;
    }

}
