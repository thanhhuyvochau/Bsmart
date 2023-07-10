package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.config.json.View;
import fpt.project.bsmart.entity.BaseEntity;
import fpt.project.bsmart.entity.constant.ECourseActivityType;
import org.codehaus.jackson.map.annotate.JsonView;


public class ActivityDto<T> extends BaseEntity {
    private Long id;
    private String name;
    private ECourseActivityType type;
    @JsonView(View.Teacher.class)
    private Boolean isVisible;

    private T detail;

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
}
