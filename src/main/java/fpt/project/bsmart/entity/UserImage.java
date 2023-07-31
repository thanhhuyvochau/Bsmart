package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.EImageType;

import javax.persistence.*;

@Entity
@Table(name = "user_image")
public class UserImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private EImageType type;
    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "status")
    private boolean status = false ;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "verified")
    private boolean verified = false ;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EImageType getType() {
        return type;
    }

    public void setType(EImageType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getStatus() {
        return status;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
