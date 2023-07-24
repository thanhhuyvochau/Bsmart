package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.constant.EComparison;
import fpt.project.bsmart.entity.constant.QuizSubmittionStatus;

import java.time.Instant;

public class QuizResultRequest {
    private QuizSubmittionStatus status;
    private EComparison comparison;
    private Float point;
    private Long classId;
    private Instant startDate;
    private Instant endDate;

    public QuizSubmittionStatus getStatus() {
        return status;
    }

    public void setStatus(QuizSubmittionStatus status) {
        this.status = status;
    }

    public EComparison getComparison() {
        return comparison;
    }

    public void setComparison(EComparison comparison) {
        this.comparison = comparison;
    }

    public Float getPoint() {
        return point;
    }

    public void setPoint(Float point) {
        this.point = point;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }
}
