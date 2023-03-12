package fpt.project.bsmart.entity.request;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class WithdrawRequest {
    @NotNull
    private BigDecimal amount;
    @NotNull
    private Long bankId;
    @NotNull
    private Long bankAccount;
    @NotNull
    private String bankAccountOwner;
    @NotNull
    private String note;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public Long getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(Long bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankAccountOwner() {
        return bankAccountOwner;
    }

    public void setBankAccountOwner(String bankAccountOwner) {
        this.bankAccountOwner = bankAccountOwner;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
