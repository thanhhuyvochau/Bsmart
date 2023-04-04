package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_detail")
public class OrderDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sub_course_id")
    private SubCourse subCourse;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    @Column(name = "original_price")
    private BigDecimal originalPrice;
    @Column(name = "final_price")
    private BigDecimal finalPrice;
    @Column(name = "referral_code")
    private String referralCode;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "orderDetail")
    private List<ReferralCode> referralCodes = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubCourse getSubCourse() {
        return subCourse;
    }

    public void setSubCourse(SubCourse subCourse) {
        this.subCourse = subCourse;
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

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }



    public List<ReferralCode> getReferralCodes() {
        return referralCodes;
    }

    public void setReferralCodes(List<ReferralCode> referralCodes) {
        this.referralCodes = referralCodes;
    }
}
