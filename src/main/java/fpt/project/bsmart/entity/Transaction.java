package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.EPaymentType;
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
    @ManyToOne(cascade = CascadeType.ALL)
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
    @Column(name = "receiver_bank_account")
    private Long receivedBankAccount;
    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;
    @Column(name = "note")
    private String note;

    @Column(name = "bank_account_owner")
    private String bankAccountOwner;
    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private EPaymentType paymentType = EPaymentType.OTHER;

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

    public static Transaction build(BigDecimal amount, Long receivedBankAccount, String bankAccountOwner, Bank bank, Wallet wallet, ETransactionType type) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setBeforeBalance(wallet.getBalance());
        transaction.setReceivedBankAccount(receivedBankAccount);
        transaction.setBankAccountOwner(bankAccountOwner);
        transaction.setBank(bank);
        transaction.setStatus(ETransactionStatus.SUCCESS);
        if (type.equals(ETransactionType.DEPOSIT)) {
            transaction.setAfterBalance(wallet.getBalance().add(amount));
        } else if (type.equals(ETransactionType.WITHDRAW)) {
            transaction.setAfterBalance(wallet.getBalance().subtract(amount));
            transaction.setWallet(wallet);
        }
        return transaction;
    }

    public Long getReceivedBankAccount() {
        return receivedBankAccount;
    }

    public void setReceivedBankAccount(Long receivedBankAccount) {
        this.receivedBankAccount = receivedBankAccount;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getBankAccountOwner() {
        return bankAccountOwner;
    }

    public void setBankAccountOwner(String bankAccountOwner) {
        this.bankAccountOwner = bankAccountOwner;
    }

    public EPaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(EPaymentType paymentType) {
        this.paymentType = paymentType;
    }
}
