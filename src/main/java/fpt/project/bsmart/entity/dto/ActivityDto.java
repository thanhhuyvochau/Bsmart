package fpt.project.bsmart.entity.dto;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.project.bsmart.config.json.View;
import fpt.project.bsmart.entity.BaseEntity;
import fpt.project.bsmart.entity.constant.ECourseActivityType;

import java.util.ArrayList;
import java.util.List;

public class ActivityDto extends BaseEntity {
    private Long id;
    private String name;
    private ECourseActivityType type;
    @JsonView(View.Teacher.class)
    private Boolean visible;
    private Long parentActivityId;
    private List<ActivityDto> subActivities = new ArrayList<>();

    private boolean fixed = false;

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

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public ECourseActivityType getType() {
        return type;
    }

    public void setType(ECourseActivityType type) {
        this.type = type;
    }

    public List<ActivityDto> getSubActivities() {
        return subActivities;
    }

    public void setSubActivities(List<ActivityDto> subActivities) {
        this.subActivities = subActivities;
    }

    public Long getParentActivityId() {
        return parentActivityId;
    }

    public void setParentActivityId(Long parentActivityId) {
        this.parentActivityId = parentActivityId;
    }

    public boolean getFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }
}
