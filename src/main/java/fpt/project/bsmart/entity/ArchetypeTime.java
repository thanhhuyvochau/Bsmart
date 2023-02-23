package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "archetype_time")
public class ArchetypeTime extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "archetype_id")
    private Archetype archetype;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dow_id")
    private DayOfWeek dayOfWeek;



    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "slot_id")
    private Slot slot;

    @OneToMany(mappedBy = "archetypeTime",cascade = CascadeType.ALL , fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TimeTable> timeTables;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Archetype getArchetype() {
        return archetype;
    }

    public void setArchetype(Archetype archetype) {
        this.archetype = archetype;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public List<TimeTable> getTimeTables() {
        return timeTables;
    }

    public void setTimeTables(List<TimeTable> timeTables) {
        this.timeTables = timeTables;
    }
}
