package fpt.project.bsmart.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;
import fpt.project.bsmart.entity.common.EAccountRole;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    @Enumerated(EnumType.STRING)
    private EAccountRole code;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Account> accounts ;

    @Column(name = "moodle_role_id")
    private Long moodleRoleId;
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

    public EAccountRole getCode() {
        return code;
    }

    public void setCode(EAccountRole code) {
        this.code = code;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Long getMoodleRoleId() {
        return moodleRoleId;
    }

    public void setMoodleRoleId(Long moodleRoleId) {
        this.moodleRoleId = moodleRoleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(name, role.name) && code == role.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code);
    }
}