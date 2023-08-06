package fpt.project.bsmart.payment;

import fpt.project.bsmart.entity.constant.EOrderStatus;
import fpt.project.bsmart.entity.constant.ETransactionStatus;

import java.math.BigDecimal;

public class PaymentResponse<T> {
    private Long orderId;
    private Long transactionId;
    private ETransactionStatus transactionStatus;
    private EOrderStatus orderStatus;
    private BigDecimal amount = BigDecimal.ZERO;
    private T metadata;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public ETransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(ETransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public EOrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(EOrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public T getMetadata() {
        return metadata;
    }

    public void setMetadata(T metadata) {
        this.metadata = metadata;
    }

}
