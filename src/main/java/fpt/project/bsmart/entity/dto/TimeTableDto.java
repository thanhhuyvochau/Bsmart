package fpt.project.bsmart.entity.dto;


import java.time.Instant;

public class TimeTableDto {

    private Long id ;
    private Instant date;
    private int slotNumber ;


    private ArchetypeTimeDto archetypeTime ;

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

    public int getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }

    public ArchetypeTimeDto getArchetypeTime() {
        return archetypeTime;
    }

    public void setArchetypeTime(ArchetypeTimeDto archetypeTime) {
        this.archetypeTime = archetypeTime;
    }
}
