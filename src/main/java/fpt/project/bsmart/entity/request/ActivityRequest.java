package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.constant.ECourseActivityType;

import java.util.List;

public class ActivityRequest {
    private String name;
    private ECourseActivityType type;
    private Boolean visible;
    private Long parentActivityId;
    private List<Long> authorizeClasses;
    private Long courseId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ECourseActivityType getType() {
        return type;
    }

    public void setType(ECourseActivityType type) {
        this.type = type;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Long getParentActivityId() {
        return parentActivityId;
    }

    public void setParentActivityId(Long parentActivityId) {
        this.parentActivityId = parentActivityId;
    }

    public List<Long> getAuthorizeClasses() {
        return authorizeClasses;
    }

    public void setAuthorizeClasses(List<Long> authorizeClasses) {
        this.authorizeClasses = authorizeClasses;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
