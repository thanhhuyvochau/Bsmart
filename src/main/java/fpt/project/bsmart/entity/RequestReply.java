package fpt.project.bsmart.entity;


import javax.persistence.*;


@Entity
@Table(name = "request_reply")
public class RequestReply extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;


    @Column(name = "content")
    private String content;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;

    @ManyToOne
    @JoinColumn(name = "replier_id")
    private Account account;

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

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
