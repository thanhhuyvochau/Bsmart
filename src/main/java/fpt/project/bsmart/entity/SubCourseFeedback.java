package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.EFeedbackType;

import javax.persistence.*;
@Entity
@Table(name = "sub_course_feedback")
public class SubCourseFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @ManyToOne
//    @JoinColumn(name = "sub_course_id")
//    private SubCourse subCourse;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "feedback_answer")
    private FeedbackAnswer feedbackAnswer;
    @Column(name = "score")
    private Double score;
    @Column(name = "opinion")
    private String opinion;
    @Column(name = "feedback_type")
    @Enumerated(EnumType.STRING)
    private EFeedbackType feedbackType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public SubCourse getSubCourse() {
//        return subCourse;
//    }
//
//    public void setSubCourse(SubCourse subCourse) {
//        this.subCourse = subCourse;
//    }

    public FeedbackAnswer getFeedbackAnswer() {
        return feedbackAnswer;
    }

    public void setFeedbackAnswer(FeedbackAnswer feedbackAnswer) {
        this.feedbackAnswer = feedbackAnswer;
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
}
