package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.common.ERequestStatus;

import java.io.Serializable;
import java.time.Instant;

public class RequestFormSearchRequest implements Serializable {
    private String q;

    private ERequestStatus status;

    private Long studentId ;
    private Instant dateFrom ;

    private Instant dateTo;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public ERequestStatus getStatus() {
        return status;
    }

    public void setStatus(ERequestStatus status) {
        this.status = status;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Instant getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Instant dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Instant getDateTo() {
        return dateTo;
    }

    public void setDateTo(Instant dateTo) {
        this.dateTo = dateTo;
    }
}
