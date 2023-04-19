package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "report_feedback")
public class ReportFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "reported_user_id")
    private User reportedUser;
    @Column(name = "report_detail")
    private String reportDetail;
    @JoinColumn(name = "feedback_answer")
    @OneToOne
    private FeedbackAnswer feedbackAnswer;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "reportFeedback")
    private List<Image> evidences = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(User reportedUser) {
        this.reportedUser = reportedUser;
    }

    public String getReportDetail() {
        return reportDetail;
    }

    public void setReportDetail(String reportDetail) {
        this.reportDetail = reportDetail;
    }

    public FeedbackAnswer getFeedbackAnswer() {
        return feedbackAnswer;
    }

    public void setFeedbackAnswer(FeedbackAnswer feedbackAnswer) {
        this.feedbackAnswer = feedbackAnswer;
    }

    public List<Image> getEvidences() {
        return evidences;
    }

    public void setEvidences(List<Image> evidences) {
        this.evidences = evidences;
    }
}
