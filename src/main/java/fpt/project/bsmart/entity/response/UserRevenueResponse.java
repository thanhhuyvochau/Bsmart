package fpt.project.bsmart.entity.response;

import java.math.BigDecimal;

public class UserRevenueResponse {
    private Long userId;
    private Integer numOfCourse = 0;
    private BigDecimal systemIncome = BigDecimal.ZERO;
    private BigDecimal revenue = BigDecimal.ZERO;
    private BigDecimal promotion = BigDecimal.ZERO;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getNumOfCourse() {
        return numOfCourse;
    }

    public void setNumOfCourse(Integer numOfCourse) {
        this.numOfCourse = numOfCourse;
    }

    public BigDecimal getSystemIncome() {
        return systemIncome;
    }

    public void setSystemIncome(BigDecimal systemIncome) {
        this.systemIncome = systemIncome;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public BigDecimal getPromotion() {
        return promotion;
    }

    public void setPromotion(BigDecimal promotion) {
        this.promotion = promotion;
    }
}
