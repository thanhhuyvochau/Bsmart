package fpt.project.bsmart.entity.dto;


import fpt.project.bsmart.entity.common.EDayOfWeekCode;
import fpt.project.bsmart.entity.common.ESlotCode;

import java.time.Instant;

public class AttendanceDto {

    private Long id;
    private Boolean isPresent ;
    private Instant date;
    private int slotNumber ;
    private String archetypeCode ;
    private String archetypeName  ;

    private ESlotCode slotCode ;
    private String slotName  ;
    private String startTime  ;
    private String endTime  ;
    private Long timeTableId;
    private String dowName;
    private EDayOfWeekCode dowCode ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getPresent() {
        return isPresent;
    }

    public void setPresent(Boolean present) {
        isPresent = present;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Long getTimeTableId() {
        return timeTableId;
    }

    public void setTimeTableId(Long timeTableId) {
        this.timeTableId = timeTableId;
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

    public String getArchetypeName() {
        return archetypeName;
    }

    public void setArchetypeName(String archetypeName) {
        this.archetypeName = archetypeName;
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

    public String getDowName() {
        return dowName;
    }

    public void setDowName(String dowName) {
        this.dowName = dowName;
    }

    public EDayOfWeekCode getDowCode() {
        return dowCode;
    }

    public void setDowCode(EDayOfWeekCode dowCode) {
        this.dowCode = dowCode;
    }
}
