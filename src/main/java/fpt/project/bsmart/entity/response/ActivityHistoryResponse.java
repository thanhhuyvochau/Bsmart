package fpt.project.bsmart.entity.response;

import fpt.project.bsmart.entity.constant.EActivityAction;
import fpt.project.bsmart.entity.constant.EActivityType;

import java.time.LocalDateTime;


public class ActivityHistoryResponse {

    private Long id;

    private EActivityType type;

    private EActivityAction action;


    private LocalDateTime activityTime;

    private String detail;

    private Long activityId;

    private String activityName ;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public EActivityAction getAction() {
        return action;
    }

    public void setAction(EActivityAction action) {
        this.action = action;
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
