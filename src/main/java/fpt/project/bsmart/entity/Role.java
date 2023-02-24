package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.common.EAccountRole;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.List;

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
    private List<User> users;

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

    public List<User> getAccounts() {
        return users;
    }

    public void setAccounts(List<User> users) {
        this.users = users;
    }

    public Long getMoodleRoleId() {
        return moodleRoleId;
    }

    public void setMoodleRoleId(Long moodleRoleId) {
        this.moodleRoleId = moodleRoleId;
    }
}