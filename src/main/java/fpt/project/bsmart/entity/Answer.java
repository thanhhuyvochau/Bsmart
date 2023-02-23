package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "answer")
public class Answer extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

   @ManyToOne
   @JoinColumn(name = "question_id")
   private SurveyQuestion surveyQuestion ;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL)
    private List<StudentAnswer > studentAnswers;

   private boolean isVisible ;

   private String answer ;


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

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<StudentAnswer> getStudentAnswers() {
        return studentAnswers;
    }

    public void setStudentAnswers(List<StudentAnswer> studentAnswers) {
        this.studentAnswers = studentAnswers;
    }
}
