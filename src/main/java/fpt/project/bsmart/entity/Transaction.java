package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transaction")
public class Transaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_info")
    private String orderInfo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "vpn_command")
    private String vpnCommand;

    @Column(name = "is_success")
    private Boolean isSuccess;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private Class paymentClass;
    @Column(name = "transaction_no")
    private String transactionNo;

    @Column(name = "pay_date")
    private Instant payDate;

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public Class getPaymentClass() {
        return paymentClass;
    }

    public void setPaymentClass(Class paymentClass) {
        this.paymentClass = paymentClass;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getVpnCommand() {
        return vpnCommand;
    }

    public void setVpnCommand(String vpnCommand) {
        this.vpnCommand = vpnCommand;
    }

    public Instant getPayDate() {
        return payDate;
    }

    public void setPayDate(Instant payDate) {
        this.payDate = payDate;
    }
}
