package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.common.ESlotCode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "slot")
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    @Enumerated(EnumType.STRING)
    private ESlotCode code ;

    @Column(name = "start_time")
    private String startTime  ;

    @Column(name = "end_time")
    private String endTime  ;

    @OneToMany(mappedBy = "slot", cascade = CascadeType.ALL , fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ArchetypeTime> archetypeTimes;

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public ESlotCode getCode() {
        return code;
    }

    public void setCode(ESlotCode code) {
        this.code = code;
    }

    public List<ArchetypeTime> getArchetypeTimes() {
        return archetypeTimes;
    }

    public void setArchetypeTimes(List<ArchetypeTime> archetypeTimes) {
        this.archetypeTimes = archetypeTimes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
