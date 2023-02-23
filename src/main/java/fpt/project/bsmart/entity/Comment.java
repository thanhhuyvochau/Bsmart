package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment")
public class Comment  extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "comment_id_generator")
    @SequenceGenerator(name = "comment_id_generator", sequenceName = "comment_id_generator")
    private Long id;
    @Column(name = "content", columnDefinition = "LONGBLOB")
    private String content;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "parentComment")
    private List<Comment> subComments = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @JoinColumn(name = "account_id")
    @ManyToOne
    private Account account;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    @OneToMany(mappedBy = "comment")
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

    public List<Comment> getSubComments() {
        return subComments;
    }

    public void setSubComments(List<Comment> subComments) {
        this.subComments = subComments;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account student) {
        this.account = student;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }
}
