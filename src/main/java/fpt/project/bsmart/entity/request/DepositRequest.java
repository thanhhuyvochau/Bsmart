package fpt.project.bsmart.entity.request;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class DepositRequest {
    @NotNull
    private BigDecimal amount = BigDecimal.ZERO;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
