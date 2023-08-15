package fpt.project.bsmart.entity.response;

import java.math.BigDecimal;

public class UserRevenueResponse {
    private Long userId;
    private Integer numOfCourse = 0;
    private BigDecimal income = BigDecimal.ZERO;
    private BigDecimal revenue = BigDecimal.ZERO;

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

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }
}
