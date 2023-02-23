package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "archetype")
public class Archetype extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "created_by_teacher_id")
    private Long createdByTeacherId;

    @OneToMany(mappedBy = "archetype", cascade = CascadeType.ALL)
    private List<ArchetypeTime> archetypeTimes;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getCreatedByTeacherId() {
        return createdByTeacherId;
    }

    public void setCreatedByTeacherId(Long createdByTeacherId) {
        this.createdByTeacherId = createdByTeacherId;
    }

    public List<ArchetypeTime> getArchetypeTimes() {
        return archetypeTimes;
    }

    public void setArchetypeTimes(List<ArchetypeTime> archetypeTimes) {
        this.archetypeTimes = archetypeTimes;
    }
}
