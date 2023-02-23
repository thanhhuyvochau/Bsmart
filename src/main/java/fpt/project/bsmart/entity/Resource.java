package fpt.project.bsmart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fpt.project.bsmart.entity.common.EResourceType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "resource")
public class Resource extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "name")
    private String name;
    @Column(name = "resource_type")
    @Enumerated(EnumType.STRING)
    private EResourceType resourceType;


    @OneToMany(mappedBy = "resource")
    List<Account> accounts;

    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL)
    private List<Course> course ;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "account_detail_id")
    private AccountDetail accountDetail ;

    @JsonIgnore
    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL)
    private List<Pano> panos = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public EResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(EResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Course> getCourse() {
        return course;
    }

    public void setCourse(List<Course> course) {
        this.course = course;
    }

    public AccountDetail getAccountDetail() {
        return accountDetail;
    }

    public void setAccountDetail(AccountDetail accountDetail) {
        this.accountDetail = accountDetail;
    }

    public List<Pano> getPanos() {
        return panos;
    }

    public void setPanos(List<Pano> panos) {
        this.panos = panos;
    }
}
