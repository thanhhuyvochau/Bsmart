package fpt.project.bsmart.entity.response;

import java.time.Instant;

public class RevenueClassResponse {
    private Long classId;
    private Long revenue ;

    private Boolean isSuccess;

    private Instant payDate;

    private String orderInfo;

    private String transactionNo;

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getRevenue() {
        return revenue;
    }

    public void setRevenue(Long revenue) {
        this.revenue = revenue;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public Instant getPayDate() {
        return payDate;
    }

    public void setPayDate(Instant payDate) {
        this.payDate = payDate;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }
}
