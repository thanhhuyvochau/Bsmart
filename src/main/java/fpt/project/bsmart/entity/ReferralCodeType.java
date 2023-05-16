package fpt.project.bsmart.entity;


import fpt.project.bsmart.entity.constant.EReferralType;

import javax.persistence.*;


@Entity
@Table(name = "referral_code_type")

public class ReferralCodeType extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "code")
    @Enumerated(EnumType.STRING)
    private EReferralType code;


    @Column(name = "discount")
    private Double discount ;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EReferralType getCode() {
        return code;
    }

    public void setCode(EReferralType code) {
        this.code = code;
    }



    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}
