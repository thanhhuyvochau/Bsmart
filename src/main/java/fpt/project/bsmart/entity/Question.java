package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.QuestionType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private User mentor;
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    @Column(name = "question")
    private String question;
    @Column(name = "question_type")
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;
    @Column(name = "number_used")
    private Integer numberUsed = 0;
    @Column(name = "is_shared")
    private Boolean isShared = false;
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getMentor() {
        return mentor;
    }

    public void setMentor(User mentor) {
        this.mentor = mentor;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public Integer getNumberUsed() {
        return numberUsed;
    }

    public void setNumberUsed(Integer numberUsed) {
        this.numberUsed = numberUsed;
    }

    public Boolean getIsShared() {
        return isShared;
    }

    public void setIsShared(Boolean shared) {
        isShared = shared;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
