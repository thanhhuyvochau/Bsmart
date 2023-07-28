package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.EFeedbackType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "feedback_template")
public class FeedbackTemplate extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private EFeedbackType type;
    @Column(name = "is_default")
    private Boolean isDefault = false;
    @Column(name = "is_fixed")
    private Boolean isFixed = false;
    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedbackQuestion> questions = new ArrayList<>();

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

    public EFeedbackType getType() {
        return type;
    }

    public void setType(EFeedbackType type) {
        this.type = type;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Boolean getIsFixed() {
        return isFixed;
    }

    public void setIsFixed(Boolean fixed) {
        isFixed = fixed;
    }

    public List<FeedbackQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<FeedbackQuestion> questions) {
        this.questions.clear();
        if(questions != null){
            this.questions = questions;
        }
    }
}
