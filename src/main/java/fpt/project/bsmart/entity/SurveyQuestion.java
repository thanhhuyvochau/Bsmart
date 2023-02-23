package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.common.EQuestionType;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "survey_question")
public class SurveyQuestion extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "question")
    private String question ;

    @Column(name = "is_visible")
    private boolean isVisible ;

    @Column(name = "question_type")
    @Enumerated(EnumType.STRING)
    private EQuestionType questionType ;

    @OneToMany(mappedBy = "surveyQuestion", cascade = CascadeType.ALL)
    private List<Answer> answers ;

    @OneToMany(mappedBy = "surveyQuestion", cascade = CascadeType.ALL)
    private List<StudentAnswer > studentAnswers;


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

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public EQuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(EQuestionType questionType) {
        this.questionType = questionType;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<StudentAnswer> getStudentAnswers() {
        return studentAnswers;
    }

    public void setStudentAnswers(List<StudentAnswer> studentAnswers) {
        this.studentAnswers = studentAnswers;
    }
}
