package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.ETransactionStatus;
import fpt.project.bsmart.entity.constant.ETransactionType;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "transaction")
public class Transaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "ipn_url")
    private String ipnUrl;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ETransactionStatus status;
    @Column(name = "redirect_url")
    private String redirectUrl;
    @Column(name = "type_balance")
    @Enumerated(EnumType.STRING)
    private ETransactionType type;
    @Column(name = "before_balance")
    private BigDecimal beforeBalance;
    @Column(name = "after_balance")
    private BigDecimal afterBalance;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getIpnUrl() {
        return ipnUrl;
    }

    public void setIpnUrl(String ipnUrl) {
        this.ipnUrl = ipnUrl;
    }

    public ETransactionStatus getStatus() {
        return status;
    }

    public void setStatus(ETransactionStatus status) {
        this.status = status;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
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

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public static Transaction build(BigDecimal amount, Wallet wallet, ETransactionType type) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setBeforeBalance(wallet.getBalance());
        if (type.equals(ETransactionType.DEPOSIT)) {
            transaction.setAfterBalance(wallet.getBalance().add(amount));
        } else if (type.equals(ETransactionType.WITHDRAW)) {
            transaction.setAfterBalance(wallet.getBalance().subtract(amount));
        }
        return transaction;
    }
}
