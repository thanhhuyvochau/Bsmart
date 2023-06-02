package fpt.project.bsmart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "Notification")

public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "vi_title")
    private String viTitle;

    @Column(name = "vi_content")
    private String viContent;
    @Column(name = "data")
    private String data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column(name = "is_read")
    private Boolean isRead = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getViTitle() {
        return viTitle;
    }

    public void setViTitle(String viTitle) {
        this.viTitle = viTitle;
    }


    public String getViContent() {
        return viContent;
    }

    public void setViContent(String viContent) {
        this.viContent = viContent;
    }

    public String getData() {
        return data;
    }



    public void setData(String data) {
        this.data = data;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }
}
