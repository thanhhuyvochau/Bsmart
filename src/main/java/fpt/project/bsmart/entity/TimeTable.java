package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "time_table")
public class TimeTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "date")
    private Instant date;
    @Column(name = "current_slot_num")
    private Integer currentSlotNum = 0;
    @Column(name = "class_room")
    private String classRoom;
    @Column(name = "class_url")
    private String classURL;
    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;
    @ManyToOne
    @JoinColumn(name = "clazz_id")
    private Class clazz;
    @OneToMany(mappedBy = "timeTable", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendances= new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }


    public Integer getCurrentSlotNum() {
        return currentSlotNum;
    }

    public void setCurrentSlotNum(Integer currentSlotNum) {
        this.currentSlotNum = currentSlotNum;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getClassURL() {
        return classURL;
    }

    public void setClassURL(String classURL) {
        this.classURL = classURL;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }
}
