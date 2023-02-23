package fpt.project.bsmart.entity;


import fpt.project.bsmart.entity.common.EFileType;

import javax.persistence.*;


@Entity
@Table(name = "file_attachment")
public class FileAttachment extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "file_type")
    @Enumerated(EnumType.STRING)
    private EFileType fileType ;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;


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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public EFileType getFileType() {
        return fileType;
    }

    public void setFileType(EFileType fileType) {
        this.fileType = fileType;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
