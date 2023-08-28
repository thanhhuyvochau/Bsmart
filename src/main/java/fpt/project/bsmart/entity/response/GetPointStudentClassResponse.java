package fpt.project.bsmart.entity.response;

import fpt.project.bsmart.entity.constant.ECourseActivityType;

import java.time.Instant;

public class GetPointStudentClassResponse {
    private float point;
    private String activityName;
    private ECourseActivityType type;
    private Instant created;
    private Instant lastUpdated;

    public float getPoint() {
        return point;
    }

    public void setPoint(float point) {
        this.point = point;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public ECourseActivityType getType() {
        return type;
    }

    public void setType(ECourseActivityType type) {
        this.type = type;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
