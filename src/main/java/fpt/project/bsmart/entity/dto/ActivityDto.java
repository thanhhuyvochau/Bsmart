package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.BaseEntity;


public class ActivityDto<T> extends BaseEntity {
    private Long id;
    private String name;
    private ActivityTypeDto type;
    private Boolean isVisible;

    private T activityDetail;

    public ActivityDto() {
    }

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

    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Boolean visible) {
        isVisible = visible;
    }

    public ActivityTypeDto getType() {
        return type;
    }

    public void setType(ActivityTypeDto type) {
        this.type = type;
    }

    public T getActivityDetail() {
        return activityDetail;
    }

    public void setActivityDetail(T activityDetail) {
        this.activityDetail = activityDetail;
    }
}
