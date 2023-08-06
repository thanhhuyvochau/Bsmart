package fpt.project.bsmart.entity.request;

import java.time.Instant;

public class TransactionRequest {
    private Instant startDate;
    private Instant endDate;
    private Long buyerId;
    private Long SellerId;
    private Long courseId;

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

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public Long getSellerId() {
        return SellerId;
    }

    public void setSellerId(Long sellerId) {
        SellerId = sellerId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
