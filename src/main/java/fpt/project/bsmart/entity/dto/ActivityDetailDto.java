package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.config.json.View;
import fpt.project.bsmart.entity.BaseEntity;
import fpt.project.bsmart.entity.constant.ECourseActivityType;
import org.codehaus.jackson.map.annotate.JsonView;

import java.util.ArrayList;
import java.util.List;

public class ActivityDetailDto<T> extends BaseEntity {
    private Long id;
    private String name;
    private ECourseActivityType type;
    @JsonView(View.Teacher.class)
    private Boolean isVisible;
    private Long parentActivityId;
    private T detail;
    private List<ActivityDetailDto> subActivities = new ArrayList<>();

    public ActivityDetailDto() {
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

    public ECourseActivityType getType() {
        return type;
    }

    public void setType(ECourseActivityType type) {
        this.type = type;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }

    public T getDetail() {
        return detail;
    }

    public void setDetail(T detail) {
        this.detail = detail;
    }

    public List<ActivityDetailDto> getSubActivities() {
        return subActivities;
    }

    public void setSubActivities(List<ActivityDetailDto> subActivities) {
        this.subActivities = subActivities;
    }

    public Long getParentActivityId() {
        return parentActivityId;
    }

    public void setParentActivityId(Long parentActivityId) {
        this.parentActivityId = parentActivityId;
    }
}
