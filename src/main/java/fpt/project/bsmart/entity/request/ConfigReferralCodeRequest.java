package fpt.project.bsmart.entity.request;

import javax.validation.constraints.NotNull;

public class ConfigReferralCodeRequest {
    @NotNull
    private Integer usageLimit;
    @NotNull
    private Integer discountPercent;
    @NotNull
    private Integer expiredLaterDay;

    public Integer getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Integer discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Integer getExpiredLaterDay() {
        return expiredLaterDay;
    }

    public void setExpiredLaterDay(Integer expiredLaterDay) {
        this.expiredLaterDay = expiredLaterDay;
    }
}
