package fpt.project.bsmart.entity.response;

import fpt.project.bsmart.entity.BaseEntity;

public class ConfigReferralCodeResponse extends BaseEntity {
    private Long id;
    private Integer usageLimit;
    private Integer discountPercent;
    private Integer expiredLaterDay;
    private boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
