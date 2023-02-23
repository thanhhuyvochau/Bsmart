package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.common.EClassStatus;
import fpt.project.bsmart.entity.common.EClassType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ClassSearchRequest implements Serializable {
    private String q;

    private EClassStatus status;
    private Instant dateFrom;

    private EClassType classType ;
    private Instant dateTo;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<Long> subjectIds = new ArrayList<>();
    private Long subjectId ;

    private Long teacherId;

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public EClassType getClassType() {
        return classType;
    }

    public void setClassType(EClassType classType) {
        this.classType = classType;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public EClassStatus getStatus() {
        return status;
    }

    public void setStatus(EClassStatus status) {
        this.status = status;
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

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public List<Long> getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(List<Long> subjectIds) {
        this.subjectIds = subjectIds;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }
}
