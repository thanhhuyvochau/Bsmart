package fpt.project.bsmart.entity.response;

import java.math.BigDecimal;

public class WithDrawResponse {
    private Long id;
    private String userName;
    private String bankName;
    private String bankAccount;
    private Long bankNumber;
    private BigDecimal amount = BigDecimal.ZERO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Long getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(Long bankNumber) {
        this.bankNumber = bankNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
