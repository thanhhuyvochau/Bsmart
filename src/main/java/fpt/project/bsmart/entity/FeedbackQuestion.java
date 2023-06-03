package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.EQuestionType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedbackAnswer> answers = new ArrayList<>();
    @ManyToMany(mappedBy = "questions")
    private List<FeedbackTemplate> feedbackTemplates = new ArrayList<>();

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

    public List<FeedbackAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<FeedbackAnswer> answers) {
        this.answers.clear();
        if(answers != null){
            this.answers.addAll(answers);
        }
    }

    public List<FeedbackTemplate> getFeedbackTemplates() {
        return feedbackTemplates;
    }

    public void setFeedbackTemplates(List<FeedbackTemplate> feedbackTemplates) {
        this.feedbackTemplates = feedbackTemplates;
    }
}
