package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.EActivityAction;
import fpt.project.bsmart.entity.constant.EActivityType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_history")
public class ActivityHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private EActivityType type;

    @Column(name = "action")
    @Enumerated(EnumType.STRING)
    private EActivityAction action;

    @Column(name = "activity_time", nullable = false)
    private LocalDateTime activityTime;

    @Column(name = "activity_name")
    private String activityName;

    @Column(name = "detail")
    private String detail;


    @JoinColumn(name = "activity_id")
    private Long activityId;

    @JoinColumn(name = "user_id")
    private Long userId;

    @JoinColumn(name = "count")
    private Integer count = 0;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EActivityType getType() {
        return type;
    }

    public EActivityAction getAction() {
        return action;
    }

    public void setAction(EActivityAction action) {
        this.action = action;
    }

    public void setType(EActivityType type) {
        this.type = type;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
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

    public LocalDateTime getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(LocalDateTime activityTime) {
        this.activityTime = activityTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
