package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.EQuestionType;

import javax.persistence.*;

@Entity
@Table(name = "feedback_question")
public class FeedbackQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "question")
    private String question;
    @Column(name = "question_type")
    @Enumerated(EnumType.STRING)
    private EQuestionType questionType;
    @Column(name = "possible_answer")
    private String possibleAnswer;
    @Column(name = "possible_score")
    private String possibleScore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public EQuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(EQuestionType questionType) {
        this.questionType = questionType;
    }

    public String getPossibleAnswer() {
        return possibleAnswer;
    }

    public void setPossibleAnswer(String possibleAnswer) {
        this.possibleAnswer = possibleAnswer;
    }

    public String getPossibleScore() {
        return possibleScore;
    }

    public void setPossibleScore(String possibleScore) {
        this.possibleScore = possibleScore;
    }
}
