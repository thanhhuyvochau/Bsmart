package fpt.project.bsmart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fpt.project.bsmart.entity.constant.EUserRole;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "role")
public class Role extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    @Enumerated(EnumType.STRING)
    private EUserRole code;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    List<User> users = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public EUserRole getCode() {
        return code;
    }

    public void setCode(EUserRole code) {
        this.code = code;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}