package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.EFeedbackType;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sub_course_feedback")
public class SubCourseFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sub_course_id")
    private SubCourse subCourse;
    @Column(name = "feedback_name")
    private String feedbackName;
    @OneToMany(mappedBy = "feedbackSubmission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedbackSubmitAnswer> submitAnswers = new ArrayList<>();
    @Column(name = "score")
    private Double score;
    @Column(name = "opinion")
    private String opinion;
    @Column(name = "feedback_type")
    @Enumerated(EnumType.STRING)
    private EFeedbackType feedbackType;
    @ManyToOne
    @JoinColumn(name = "submit_by")
    private User submitBy;
    @Column(name = "submit_date")
    private Instant submitDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubCourse getSubCourse() {
        return subCourse;
    }

    public void setSubCourse(SubCourse subCourse) {
        this.subCourse = subCourse;
    }

    public String getFeedbackName() {
        return feedbackName;
    }

    public void setFeedbackName(String feedbackName) {
        this.feedbackName = feedbackName;
    }

    public List<FeedbackSubmitAnswer> getSubmitAnswers() {
        return submitAnswers;
    }

    public void setSubmitAnswers(List<FeedbackSubmitAnswer> submitAnswers) {
        this.submitAnswers.clear();
        if(submitAnswers !=null) {
            this.submitAnswers.addAll(submitAnswers);
        }
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public EFeedbackType getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(EFeedbackType feedbackType) {
        this.feedbackType = feedbackType;
    }

    public User getSubmitBy() {
        return submitBy;
    }

    public void setSubmitBy(User submitBy) {
        this.submitBy = submitBy;
    }

    public Instant getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Instant submitDate) {
        this.submitDate = submitDate;
    }
}
