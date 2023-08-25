package fpt.project.bsmart.entity.response;

import fpt.project.bsmart.entity.constant.ECourseActivityType;

public class GetPointStudentClassResponse {
    private float point ;
    private Long activityId;
    private ECourseActivityType type;

    public float getPoint() {
        return point;
    }

    public void setPoint(float point) {
        this.point = point;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public ECourseActivityType getType() {
        return type;
    }

    public void setType(ECourseActivityType type) {
        this.type = type;
    }
}
