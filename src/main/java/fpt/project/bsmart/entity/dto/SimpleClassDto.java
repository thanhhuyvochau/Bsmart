package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.constant.ETypeLearn;

import java.math.BigDecimal;
import java.time.Instant;


public class SimpleClassDto {
    private Long id;
    private String code;
    private ETypeLearn typeLearn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ETypeLearn getTypeLearn() {
        return typeLearn;
    }

    public void setTypeLearn(ETypeLearn typeLearn) {
        this.typeLearn = typeLearn;
    }
}