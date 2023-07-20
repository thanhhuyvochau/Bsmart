package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.BaseEntity;


public class ActivityAuthorizeDto extends BaseEntity {
    private Long id;
    private SimpleClassDto authorizeClass;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SimpleClassDto getAuthorizeClass() {
        return authorizeClass;
    }

    public void setAuthorizeClass(SimpleClassDto authorizeClass) {
        this.authorizeClass = authorizeClass;
    }
}