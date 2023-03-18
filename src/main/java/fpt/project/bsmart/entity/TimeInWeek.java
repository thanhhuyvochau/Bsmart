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
    @JoinColumn(name = "sub_course_id")
    private SubCourse subCourse;
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

    public SubCourse getSubCourse() {
        return subCourse;
    }

    public void setSubCourse(SubCourse subCourse) {
        this.subCourse = subCourse;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }
}
