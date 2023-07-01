package fpt.project.bsmart.entity;

import javax.persistence.*;

@Entity
@Table(name = "time_in_week")
public class TimeInWeek {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "dow_id")
    private DayOfWeek dayOfWeek;
    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class clazz;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }
}
