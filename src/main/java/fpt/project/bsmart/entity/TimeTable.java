package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "time_table")
public class TimeTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "date")
    private Instant date;
    @Column(name = "current_slot_nums")
    private Integer currentSlotNums = 0;
    @Column(name = "class_room")
    private String classRoom;
    @Column(name = "class_url")
    private String classURL;

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

    public Integer getCurrentSlotNums() {
        return currentSlotNums;
    }

    public void setCurrentSlotNums(Integer currentSlotNums) {
        this.currentSlotNums = currentSlotNums;
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
}
