package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.common.EDayOfWeekCode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "day_of_week")
public class DayOfWeek extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    @Enumerated(EnumType.STRING)
    private EDayOfWeekCode code ;

    @OneToMany(mappedBy = "dayOfWeek",cascade = CascadeType.ALL , fetch = FetchType.LAZY, orphanRemoval = true)
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

    public EDayOfWeekCode getCode() {
        return code;
    }

    public void setCode(EDayOfWeekCode code) {
        this.code = code;
    }

    public List<ArchetypeTime> getArchetypeTimes() {
        return archetypeTimes;
    }

    public void setArchetypeTimes(List<ArchetypeTime> archetypeTimes) {
        this.archetypeTimes = archetypeTimes;
    }
}
