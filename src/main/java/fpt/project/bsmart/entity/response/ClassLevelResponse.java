package fpt.project.bsmart.entity.response;

import fpt.project.bsmart.entity.common.EClassLevel;


public class ClassLevelResponse {
    private Long id;
    private String name;

    private EClassLevel code;


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

    public EClassLevel getCode() {
        return code;
    }

    public void setCode(EClassLevel code) {
        this.code = code;
    }

}
