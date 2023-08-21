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
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ETransactionStatus status;
    @Column(name = "type_balance")
    @Enumerated(EnumType.STRING)
    private ETransactionType type;
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
    @Column(name = "transaction_no")
    private String transactionNo;
    @Column(name = "vpn_command")
    private String vpnCommand;

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
        transaction.setReceivedBankAccount(receivedBankAccount);
        transaction.setBankAccountOwner(bankAccountOwner);
        transaction.setBank(bank);
        transaction.setStatus(ETransactionStatus.SUCCESS);
        transaction.setStatus(ETransactionStatus.WAITING);
        transaction.setWallet(wallet);
        return transaction;
    }
    @Column(name = "order_info")
    private String orderInfo;

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

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getVpnCommand() {
        return vpnCommand;
    }

    public void setVpnCommand(String vpnCommand) {
        this.vpnCommand = vpnCommand;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }
}
