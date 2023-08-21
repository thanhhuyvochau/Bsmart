package fpt.project.bsmart.entity;


import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "referral_code")
public class ReferralCode extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "discount_percent")
    private Integer discountPercent;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "usage_count")
    private Integer usageCount = 0;

    @Column(name = "expired_at")
    private Instant expiredAt;


    @OneToOne
    @JoinColumn(name = "order_detail_id", referencedColumnName = "id")
    private OrderDetail orderDetail;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "config_referral_id")
    private ConfigReferralCode configReferralCode;

    @OneToMany(mappedBy = "appliedReferralCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> appliedOrderDetails = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Integer discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Integer getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public Instant getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Instant expiredAt) {
        this.expiredAt = expiredAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public fpt.project.bsmart.entity.OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(fpt.project.bsmart.entity.OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    public ConfigReferralCode getConfigReferralCode() {
        return configReferralCode;
    }

    public void setConfigReferralCode(ConfigReferralCode configReferralCode) {
        this.configReferralCode = configReferralCode;
    }

    public List<OrderDetail> getAppliedOrderDetails() {
        return appliedOrderDetails;
    }

    public void setAppliedOrderDetails(List<OrderDetail> appliedOrderDetails) {
        this.appliedOrderDetails = appliedOrderDetails;
    }
}
