package fpt.project.bsmart.entity.dto;


public class SlotDowDto {

    private int slotNumber ;

//    private Instant date ;

    private long slotId ;

    private Long dayOfWeekId ;



    public long getSlotId() {
        return slotId;
    }

    public void setSlotId(long slotId) {
        this.slotId = slotId;
    }

    public Long getDayOfWeekId() {
        return dayOfWeekId;
    }

    public void setDayOfWeekId(Long dayOfWeekId) {
        this.dayOfWeekId = dayOfWeekId;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }

//    public Instant getDate() {
//        return date;
//    }
//
//    public void setDate(Instant date) {
//        this.date = date;
//    }
}
