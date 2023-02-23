package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.common.EClassLevel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "class_level")
public class ClassLevel  extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "code")
    @Enumerated(EnumType.STRING)
    private EClassLevel code;


    @OneToMany(mappedBy = "classLevel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountDetailClassLevel> accountDetailClassLevels;

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

    public EClassLevel getCode() {
        return code;
    }

    public void setCode(EClassLevel code) {
        this.code = code;
    }

    public List<AccountDetailClassLevel> getAccountDetailClassLevels() {
        return accountDetailClassLevels;
    }

    public void setAccountDetailClassLevels(List<AccountDetailClassLevel> accountDetailClassLevels) {
        this.accountDetailClassLevels = accountDetailClassLevels;
    }
}
