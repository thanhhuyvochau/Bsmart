package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.QuizSubmittionStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz_submittiion")
public class QuizSubmittion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
    @Column(name = "correct_number")
    private Integer correctNumber = 0;
    @Column(name = "incorrect_number")
    private Integer incorrectNumber = 0;
    @Column(name = "point")
    private Float point;
    @Column(name = "status")
    private QuizSubmittionStatus status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quizSubmittion" )
    private List<QuizSubmitQuestion> submitQuestions = new ArrayList<>();
    @OneToOne
    @JoinColumn(name = "submitted_by")
    private User submittedBy;

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

    public Integer getCorrectNumber() {
        return correctNumber;
    }

    public void setCorrectNumber(Integer correctNumber) {
        this.correctNumber = correctNumber;
    }

    public Integer getIncorrectNumber() {
        return incorrectNumber;
    }

    public void setIncorrectNumber(Integer incorrectNumber) {
        this.incorrectNumber = incorrectNumber;
    }

    public Float getPoint() {
        return point;
    }

    public void setPoint(Float point) {
        this.point = point;
    }

    public QuizSubmittionStatus getStatus() {
        return status;
    }

    public void setStatus(QuizSubmittionStatus status) {
        this.status = status;
    }

    public List<QuizSubmitQuestion> getSubmitQuestions() {
        return submitQuestions;
    }

    public void setSubmitQuestions(List<QuizSubmitQuestion> submitQuestions) {
        this.submitQuestions = submitQuestions;
    }

    public User getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(User submittedBy) {
        this.submittedBy = submittedBy;
    }
}
