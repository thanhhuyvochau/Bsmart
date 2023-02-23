package fpt.project.bsmart.entity.dto;


import fpt.project.bsmart.entity.common.EClassLevel;

public class ClassTypeDto {

    private Long id;
    private EClassLevel code;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EClassLevel getCode() {
        return code;
    }

    public void setCode(EClassLevel code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
