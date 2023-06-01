package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.EActivityType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_history")
public class ActivityHistory  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private EActivityType type;

    @Column(name = "activity_time", nullable = false)
    private LocalDateTime activityTime;

    @Column(name = "detail")
    private String detail;


    @JoinColumn(name = "activity_id")
    private Long activityId;


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
}
