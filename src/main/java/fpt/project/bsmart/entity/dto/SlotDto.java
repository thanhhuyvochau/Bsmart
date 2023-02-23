package fpt.project.bsmart.entity.dto;


import fpt.project.bsmart.entity.common.ESlotCode;

public class SlotDto {

    private Long id ;
    private ESlotCode code ;
    private String name  ;
    private String startTime  ;
    private String endTime  ;

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
