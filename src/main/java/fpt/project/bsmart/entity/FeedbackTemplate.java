package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.EFeedbackType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "feedback_template")
public class FeedbackTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "template_name")
    private String templateName;
    @ManyToMany
    @JoinTable(name = "feedback_question_template", joinColumns = @JoinColumn(name = "feedback_template_id"), inverseJoinColumns = @JoinColumn(name = ("feedback_question_id")))
    private List<FeedbackQuestion> questions = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "permission")
    private Role permission;

    @Enumerated(EnumType.STRING)
    @Column(name = "feedback_type")
    private EFeedbackType feedbackType;

    @OneToMany(mappedBy = "feedbackTemplate", cascade = CascadeType.ALL)
    private List<SubCourse> subCourses = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public List<FeedbackQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<FeedbackQuestion> questions) {
        this.questions = questions;
    }

    public Role getPermission() {
        return permission;
    }

    public void setPermission(Role permission) {
        this.permission = permission;
    }

    public EFeedbackType getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(EFeedbackType feedbackType) {
        this.feedbackType = feedbackType;
    }

    public List<SubCourse> getSubCourses() {
        return subCourses;
    }

    public void setSubCourses(List<SubCourse> subCourses) {
        this.subCourses = subCourses;
    }
}
