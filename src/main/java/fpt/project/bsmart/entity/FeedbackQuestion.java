package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.EFeedbackAnswerType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "feedback_question")
public class FeedbackQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "question")
    private String question;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private EFeedbackAnswerType questionType;
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedbackAnswer> answers = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "template")
    private FeedbackTemplate template;
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

    public List<FeedbackAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<FeedbackAnswer> answers) {
        this.answers = answers;
    }

    public FeedbackTemplate getTemplate() {
        return template;
    }

    public void setTemplate(FeedbackTemplate template) {
        this.template = template;
    }

    public EFeedbackAnswerType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(EFeedbackAnswerType questionType) {
        this.questionType = questionType;
    }
}
