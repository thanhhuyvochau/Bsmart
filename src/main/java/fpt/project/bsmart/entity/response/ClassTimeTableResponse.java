package fpt.project.bsmart.entity.response;


import fpt.project.bsmart.entity.common.EDayOfWeekCode;
import fpt.project.bsmart.entity.common.ESlotCode;

import java.time.Instant;

public class ClassTimeTableResponse {

    private Long id ;
    private Instant date;
    private int slotNumber ;

    private String archetypeCode ;
    private String archetypeCName  ;

    private ESlotCode slotCode ;
    private String slotName  ;
    private String startTime  ;
    private String endTime  ;
    private String dayOfWeekName;
    private EDayOfWeekCode dayOfWeekCode ;

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

    public String getArchetypeCode() {
        return archetypeCode;
    }

    public void setArchetypeCode(String archetypeCode) {
        this.archetypeCode = archetypeCode;
    }

    public String getArchetypeCName() {
        return archetypeCName;
    }

    public void setArchetypeCName(String archetypeCName) {
        this.archetypeCName = archetypeCName;
    }

    public ESlotCode getSlotCode() {
        return slotCode;
    }

    public void setSlotCode(ESlotCode slotCode) {
        this.slotCode = slotCode;
    }

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
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

    public String getDayOfWeekName() {
        return dayOfWeekName;
    }

    public void setDayOfWeekName(String dayOfWeekName) {
        this.dayOfWeekName = dayOfWeekName;
    }

    public EDayOfWeekCode getDayOfWeekCode() {
        return dayOfWeekCode;
    }

    public void setDayOfWeekCode(EDayOfWeekCode dayOfWeekCode) {
        this.dayOfWeekCode = dayOfWeekCode;
    }
}
