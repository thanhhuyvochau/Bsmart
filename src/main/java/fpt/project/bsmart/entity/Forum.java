package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.common.EForumType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "forum")
public class Forum  extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "forum_sequence_generator")
    @SequenceGenerator(name = "forum_sequence_generator", sequenceName = "forum_sequence_generator")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private String code;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private EForumType type;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private Class clazz;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "forum")
    private List<ForumLesson> forumLessons = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "forum")
    private List<Question> questions = new ArrayList<>();

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public EForumType getType() {
        return type;
    }

    public void setType(EForumType type) {
        this.type = type;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Class getaClazz() {
        return clazz;
    }

    public void setaClazz(Class aClass) {
        this.clazz = aClass;
    }

    public List<ForumLesson> getForumLessons() {
        return forumLessons;
    }

    public void setForumLessons(List<ForumLesson> forumLessons) {
        this.forumLessons = forumLessons;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
