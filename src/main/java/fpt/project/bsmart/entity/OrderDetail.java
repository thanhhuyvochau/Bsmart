package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_detail")
public class OrderDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class clazz;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    @Column(name = "original_price")
    private BigDecimal originalPrice;
    @Column(name = "final_price")
    private BigDecimal finalPrice;

    @OneToOne(mappedBy = "orderDetail", cascade = CascadeType.ALL)
    private ReferralCode referralCode;

    @ManyToOne
    @JoinColumn(name = "applied_referral_id")
    private ReferralCode appliedReferralCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public ReferralCode getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(ReferralCode referralCode) {
        this.referralCode = referralCode;
    }

    public ReferralCode getAppliedReferralCode() {
        return appliedReferralCode;
    }

    public void setAppliedReferralCode(ReferralCode appliedReferralCode) {
        this.appliedReferralCode = appliedReferralCode;
    }
}
