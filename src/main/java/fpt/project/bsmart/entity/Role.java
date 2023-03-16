package fpt.project.bsmart.entity;


import fpt.project.bsmart.entity.constant.EUserRole;

import javax.persistence.*;


@Entity
@Table(name = "role")
public class Role extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    @Enumerated(EnumType.STRING)
    private EUserRole code;

//    @JsonIgnore
//    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
//    List<User> users = new ArrayList<>();


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


}