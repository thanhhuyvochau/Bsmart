package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.constant.ETransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;

public class WithDrawSearchRequest {
    private ETransactionStatus status;
    private Instant fromDate;
    private Instant toDate;
    private BigDecimal fromAmount;
    private BigDecimal toAmount;
    private String mentorName;

    public ETransactionStatus getStatus() {
        return status;
    }

    public void setStatus(ETransactionStatus status) {
        this.status = status;
    }

    public Instant getFromDate() {
        return fromDate;
    }

    public void setFromDate(Instant fromDate) {
        this.fromDate = fromDate;
    }

    public Instant getToDate() {
        return toDate;
    }

    public void setToDate(Instant toDate) {
        this.toDate = toDate;
    }

    public BigDecimal getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(BigDecimal fromAmount) {
        this.fromAmount = fromAmount;
    }

    public BigDecimal getToAmount() {
        return toAmount;
    }

    public void setToAmount(BigDecimal toAmount) {
        this.toAmount = toAmount;
    }

    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }
}
