package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "question_id_generator")
    @SequenceGenerator(name = "question_id_generator", sequenceName = "question_id_generator")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "content", columnDefinition = "LONGBLOB")
    private String content;
    @Column(name = "is_closed")
    private Boolean isClosed = false;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Account student;

    @ManyToOne
    @JoinColumn(name = "forum_id")
    private Forum forum;
    @OneToMany(mappedBy = "question")
    private List<Comment> comments = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "forum_lesson_id")
    private ForumLesson forumLesson;

    @OneToMany(mappedBy = "question")
    private List<Vote> votes = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getClosed() {
        return isClosed;
    }

    public void setClosed(Boolean closed) {
        isClosed = closed;
    }

    public Account getStudent() {
        return student;
    }

    public void setStudent(Account student) {
        this.student = student;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public ForumLesson getForumLesson() {
        return forumLesson;
    }

    public void setForumLesson(ForumLesson forumLesson) {
        this.forumLesson = forumLesson;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
