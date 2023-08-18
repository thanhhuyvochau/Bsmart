package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "config_referral_code")
public class ConfigReferralCode extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "usage_limit")
    private Integer usageLimit;
    @Column(name = "discount_percent")
    private Integer discountPercent;
    @Column(name = "expired_later_day")
    private Integer expiredLaterDay;
    @Column(name = "is_active")
    private boolean active = false;
    @OneToMany(mappedBy = "configReferralCode")
    private List<ReferralCode> referralCodeList = new ArrayList<>();

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

    public List<ReferralCode> getReferralCodeList() {
        return referralCodeList;
    }

    public void setReferralCodeList(List<ReferralCode> referralCodeList) {
        this.referralCodeList = referralCodeList;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
