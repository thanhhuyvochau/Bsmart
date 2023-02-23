package fpt.project.bsmart.entity;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "voice")
public class Voice extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "voice")
    private List<AccountDetail> accountDetails = new ArrayList<>();

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

    public List<AccountDetail> getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(List<AccountDetail> accountDetails) {
        this.accountDetails = accountDetails;
    }
}
