package fpt.project.bsmart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fpt.project.bsmart.entity.dto.ResponseMessage;

import javax.persistence.*;
import java.security.InvalidParameterException;

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

    private Notification() {
    }

    private Notification(String viTitle, String viContent, String data, User user, Boolean isRead) {
        this.viTitle = viTitle;
        this.viContent = viContent;
        this.data = data;
        this.user = user;
        this.isRead = isRead;
    }

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

    public static class Builder {
        private String viTitle;
        private String viContent;
        private String data;
        private User user;
        private Boolean isRead = false;

        public Builder viTitle(String viTitle) {
            this.viTitle = viTitle;
            return this;
        }

        public Builder viContent(String viContent) {
            this.viContent = viContent;
            return this;
        }

        public Builder data(String data) {
            this.data = data;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Notification build() {
            if (viTitle.isEmpty()) {
                throw new InvalidParameterException("Notification must has title");
            } else if (viContent.isEmpty()) {
                throw new InvalidParameterException("Notification must has content");
            }
            Notification notification = new Notification(viTitle, viContent, data, user, isRead);
            return notification;
        }

        public ResponseMessage buildAsResponseMessage() {
            return new ResponseMessage(this.viTitle, this.viContent, this.data);
        }

        public static Builder getBuilder() {
            return new Builder();
        }
    }
}
