package fpt.project.bsmart.entity.response;

import fpt.project.bsmart.entity.constant.EActivityType;

import javax.persistence.*;
import java.time.LocalDateTime;


public class ActivityHistoryResponse {

    private Long id;

    private EActivityType type;


    private LocalDateTime activityTime;

    private String detail;

    private Long activityId;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EActivityType getType() {
        return type;
    }

    public void setType(EActivityType type) {
        this.type = type;
    }

    public LocalDateTime getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(LocalDateTime activityTime) {
        this.activityTime = activityTime;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
