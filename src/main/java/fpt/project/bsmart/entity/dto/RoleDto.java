package fpt.project.bsmart.entity.dto;


import fpt.project.bsmart.entity.constant.EUserRole;

public class RoleDto {

    private Long id;
    private String name;


    private EUserRole code;

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

    public EUserRole getCode() {
        return code;
    }

    public void setCode(EUserRole code) {
        this.code = code;
    }
}
