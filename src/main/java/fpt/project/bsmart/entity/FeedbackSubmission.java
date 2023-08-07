package fpt.project.bsmart.entity;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "feedback_submission")
public class FeedbackSubmission extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "submit_by")
    private User submitBy;
    @ManyToOne
    @JoinColumn(name = "class")
    private Class clazz;
    @ManyToOne
    @JoinColumn(name = "template")
    private FeedbackTemplate template;
    @Positive
    @Max(5)
    @Min(1)
    @Column(name = "mentor_rate")
    private Integer mentorRate = 1;
    @Positive
    @Max(5)
    @Min(1)
    @Column(name = "course_rate")
    private Integer courseRate = 1;
    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedbackSubmitAnswer> answers = new ArrayList<>();
    @Column(name = "comment")
    private String comment;

    public Integer getCourseRate() {
        return courseRate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getMentorRate() {
        return mentorRate;
    }

    public void setMentorRate(Integer mentorRate) {
        this.mentorRate = mentorRate;
    }

    public void setCourseRate(Integer courseRate) {
        this.courseRate = courseRate;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
