package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "feedback_submission")
public class FeedbackSubmission extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "submit_by")
    private User submitBy;
    @ManyToOne
    @JoinColumn(name = "class")
    private Class clazz;
    @ManyToOne
    @JoinColumn(name = "template")
    private FeedbackTemplate template;
    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedbackSubmitAnswer> answers = new ArrayList<>();
    @Column(name = "rate")
    private Integer rate = 1;
    @Column(name = "comment")
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getSubmitBy() {
        return submitBy;
    }

    public void setSubmitBy(User submitBy) {
        this.submitBy = submitBy;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public FeedbackTemplate getTemplate() {
        return template;
    }

    public void setTemplate(FeedbackTemplate template) {
        this.template = template;
    }

    public List<FeedbackSubmitAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<FeedbackSubmitAnswer> answers) {
        this.answers.clear();
        if(answers != null){
            this.answers.addAll(answers);
        }
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
