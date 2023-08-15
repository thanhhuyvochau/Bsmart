package fpt.project.bsmart.entity.response;

import java.math.BigDecimal;

public class SystemRevenueResponse {
    private Integer month;
    private BigDecimal totalIncome = BigDecimal.ZERO;
    private BigDecimal revenue = BigDecimal.ZERO;
    private BigDecimal promotion = BigDecimal.ZERO;
    private BigDecimal mentorShare = BigDecimal.ZERO;

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
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

    public BigDecimal getMentorShare() {
        return mentorShare;
    }

    public void setMentorShare(BigDecimal mentorShare) {
        this.mentorShare = mentorShare;
    }
}
