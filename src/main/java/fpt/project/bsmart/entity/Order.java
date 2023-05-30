package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.EOrderStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "total_price")
    private BigDecimal totalPrice;
    @Column(name = "center_bonus")
    private BigDecimal centerBonus;
    @Column(name = "student_bonus")
    private BigDecimal studentBonus;
    @Column(name = "refer_bonus")
    private BigDecimal referBonus;
    @Column(name = "total_bonus")
    private BigDecimal totalBonus;
    @Column(name = "refer_bonus_percent")
    private Float referBonusPercent;
    @Column(name = "student_bonus_percent")
    private Float studentBonusPercent;
    @Column(name = "center_bonus_percent")
    private Float centerBonusPercent;
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EOrderStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getCenterBonus() {
        return centerBonus;
    }

    public void setCenterBonus(BigDecimal centerBonus) {
        this.centerBonus = centerBonus;
    }

    public BigDecimal getStudentBonus() {
        return studentBonus;
    }

    public void setStudentBonus(BigDecimal studentBonus) {
        this.studentBonus = studentBonus;
    }

    public BigDecimal getReferBonus() {
        return referBonus;
    }

    public void setReferBonus(BigDecimal referBonus) {
        this.referBonus = referBonus;
    }

    public BigDecimal getTotalBonus() {
        return totalBonus;
    }

    public void setTotalBonus(BigDecimal totalBonus) {
        this.totalBonus = totalBonus;
    }

    public Float getReferBonusPercent() {
        return referBonusPercent;
    }

    public void setReferBonusPercent(Float referBonusPercent) {
        this.referBonusPercent = referBonusPercent;
    }

    public Float getStudentBonusPercent() {
        return studentBonusPercent;
    }

    public void setStudentBonusPercent(Float studentBonusPercent) {
        this.studentBonusPercent = studentBonusPercent;
    }

    public Float getCenterBonusPercent() {
        return centerBonusPercent;
    }

    public void setCenterBonusPercent(Float centerBonusPercent) {
        this.centerBonusPercent = centerBonusPercent;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public EOrderStatus getStatus() {
        return status;
    }

    public void setStatus(EOrderStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
