package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.BaseEntity;
import fpt.project.bsmart.entity.constant.EAssignmentStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class SimpleAssignmentDto extends BaseEntity {
    private Long id;
    private String description;
    private Instant startDate;
    private Instant endDate;
    private EAssignmentStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public EAssignmentStatus getStatus() {
        return status;
    }

    public void setStatus(EAssignmentStatus status) {
        this.status = status;
    }
}
