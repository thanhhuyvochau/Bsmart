package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.Activity;

import javax.persistence.*;



public class ResourceDto {

    private Long id;
    
    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
