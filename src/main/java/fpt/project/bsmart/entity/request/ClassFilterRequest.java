package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.constant.ECourseStatus;

import java.time.Instant;

public class ClassFilterRequest {
    private String q;
    private Instant startDate;
    private Instant endDate;
    private ECourseStatus status;
    // Student = 1 | Teacher = 2
    private int asRole = 0;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
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

    public int getAsRole() {
        return asRole;
    }

    public void setAsRole(int asRole) {
        this.asRole = asRole;
    }

    public ECourseStatus getStatus() {
        return status;
    }

    public void setStatus(ECourseStatus status) {
        this.status = status;
    }
}
