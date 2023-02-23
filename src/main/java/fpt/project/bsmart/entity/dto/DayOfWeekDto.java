package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.common.EDayOfWeekCode;

public class DayOfWeekDto {

    private Long id;
    private String name;
    private EDayOfWeekCode code ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EDayOfWeekCode getCode() {
        return code;
    }

    public void setCode(EDayOfWeekCode code) {
        this.code = code;
    }
}
