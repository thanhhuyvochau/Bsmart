package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.VoteNumberReponse;
import fpt.project.bsmart.entity.response.AccountSimpleResponse;
import fpt.project.bsmart.entity.response.SubjectSimpleResponse;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class QuestionDto {


    private Long id;
    private String title;
    private String content;
    private Boolean isClosed;
    private AccountSimpleResponse user;
    private SubjectSimpleResponse subject;
    private VoteNumberReponse voteNumberReponse;
    private List<CommentDto> comments = new ArrayList<>();
    private Integer userState = 0;
    private Instant created;
    private Instant lastModified;

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

    public AccountSimpleResponse getUser() {
        return user;
    }

    public void setUser(AccountSimpleResponse user) {
        this.user = user;
    }

    public SubjectSimpleResponse getSubject() {
        return subject;
    }

    public void setSubject(SubjectSimpleResponse subject) {
        this.subject = subject;
    }

    public VoteNumberReponse getVoteNumberReponse() {
        return voteNumberReponse;
    }

    public void setVoteNumberReponse(VoteNumberReponse voteNumberReponse) {
        this.voteNumberReponse = voteNumberReponse;
    }

    public Integer getUserState() {
        return userState;
    }

    public void setUserState(Integer userState) {
        this.userState = userState;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastModified() {
        return lastModified;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
