package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.BaseEntity;
import fpt.project.bsmart.entity.constant.ETransactionStatus;
import fpt.project.bsmart.entity.constant.ETransactionType;

import java.math.BigDecimal;

public class TransactionDto extends BaseEntity {

    private Long id;

    private BigDecimal amount;


    private ETransactionStatus status;
    private String statusName;

    private ETransactionType type;
    private String typeName;

    private BigDecimal beforeBalance;

    private BigDecimal afterBalance;
    private String iconUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ETransactionStatus getStatus() {
        return status;
    }

    public void setStatus(ETransactionStatus status) {
        this.status = status;
    }

    public ETransactionType getType() {
        return type;
    }

    public void setType(ETransactionType type) {
        this.type = type;
    }

    public BigDecimal getBeforeBalance() {
        return beforeBalance;
    }

    public void setBeforeBalance(BigDecimal beforeBalance) {
        this.beforeBalance = beforeBalance;
    }

    public BigDecimal getAfterBalance() {
        return afterBalance;
    }

    public void setAfterBalance(BigDecimal afterBalance) {
        this.afterBalance = afterBalance;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
