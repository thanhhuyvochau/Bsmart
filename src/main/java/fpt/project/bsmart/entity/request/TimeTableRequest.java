package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.dto.SlotDowDto;

import java.io.Serializable;
import java.util.List;


public class TimeTableRequest implements Serializable {



    private String archetypeName  ;

//    private String archetypeCode  ;


    private List<SlotDowDto> slotDow ;





    public String getArchetypeName() {
        return archetypeName;
    }

    public void setArchetypeName(String archetypeName) {
        this.archetypeName = archetypeName;
    }

//    public String getArchetypeCode() {
//        return archetypeCode;
//    }
//
//    public void setArchetypeCode(String archetypeCode) {
//        this.archetypeCode = archetypeCode;
//    }



    public List<SlotDowDto> getSlotDow() {
        return slotDow;
    }

    public void setSlotDow(List<SlotDowDto> slotDow) {
        this.slotDow = slotDow;
    }
}
