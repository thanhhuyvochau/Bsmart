package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "forum_lesson")
public class ForumLesson  extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "forum_lesson_sequence_generator")
    @SequenceGenerator(name = "forum_lesson_sequence_generator", sequenceName = "forum_lesson_sequence_generator")
    private Long id;
    @Column(name = "lesson_name")
    private String lessonName;
    @ManyToOne
    @JoinColumn(name = "forum_id")
    private Forum forum;

    @OneToOne
    @JoinColumn(name = "section_id")
    private Section section;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "forumLesson",fetch = FetchType.LAZY)
    private List<Question> questions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }
}
