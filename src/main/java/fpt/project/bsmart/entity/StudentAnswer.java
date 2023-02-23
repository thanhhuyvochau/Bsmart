package fpt.project.bsmart.entity;

import javax.persistence.*;


@Entity
@Table(name = "student_answer")
public class StudentAnswer extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private SurveyQuestion surveyQuestion ;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Account student ;

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private Answer answer ;


    @Column(name = "essay_answer")
    private String openAnswer ;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SurveyQuestion getSurveyQuestion() {
        return surveyQuestion;
    }

    public void setSurveyQuestion(SurveyQuestion surveyQuestion) {
        this.surveyQuestion = surveyQuestion;
    }

    public Account getStudent() {
        return student;
    }

    public void setStudent(Account student) {
        this.student = student;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public String getOpenAnswer() {
        return openAnswer;
    }

    public void setOpenAnswer(String openAnswer) {
        this.openAnswer = openAnswer;
    }
}
