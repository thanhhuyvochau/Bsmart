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
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EOrderStatus status;
    @Column(name = "center_bonus")
    private BigDecimal centerBonus = BigDecimal.ZERO;
    @Column(name = "student_bonus")
    private BigDecimal studentBonus = BigDecimal.ZERO;
    @Column(name = "refer_bonus")
    private BigDecimal referBonus = BigDecimal.ZERO;
    @Column(name = "total_bonus")
    private BigDecimal totalBonus = BigDecimal.ZERO;
    @Column(name = "refer_bonus_percent")
    private Float referBonusPercent;
    @Column(name = "student_bonus_percent")
    private Float studentBonusPercent;
    @Column(name = "center_bonus_percent")
    private Float centerBonusPercent;


    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Order(BigDecimal totalPrice, BigDecimal centerBonus, BigDecimal studentBonus, BigDecimal referBonus, BigDecimal totalBonus, Float referBonusPercent, Float studentBonusPercent, Float centerBonusPercent, List<OrderDetail> orderDetails, EOrderStatus status, User user) {
        this.totalPrice = totalPrice;
        this.centerBonus = centerBonus;
        this.studentBonus = studentBonus;
        this.referBonus = referBonus;
        this.totalBonus = totalBonus;
        this.referBonusPercent = referBonusPercent;
        this.studentBonusPercent = studentBonusPercent;
        this.centerBonusPercent = centerBonusPercent;
        this.orderDetails = orderDetails;
        this.status = status;
        this.user = user;
    }

    public Order() {
    }

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

    public static class Builder {

        private BigDecimal totalPrice;
        private BigDecimal centerBonus;
        private BigDecimal studentBonus;
        private BigDecimal referBonus;
        private BigDecimal totalBonus;
        private Float referBonusPercent;
        private Float studentBonusPercent;
        private Float centerBonusPercent;
        private List<OrderDetail> orderDetails = new ArrayList<>();
        private EOrderStatus status;

        private User user;

        public static Builder builder() {
            return new Builder();
        }

        public Builder setTotalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public Builder setCenterBonus(BigDecimal centerBonus) {
            this.centerBonus = centerBonus;
            return this;
        }

        public Builder setStudentBonus(BigDecimal studentBonus) {
            this.studentBonus = studentBonus;
            return this;
        }

        public Builder setReferBonus(BigDecimal referBonus) {
            this.referBonus = referBonus;
            return this;
        }

        public Builder setTotalBonus(BigDecimal totalBonus) {
            this.totalBonus = totalBonus;
            return this;
        }

        public Builder setReferBonusPercent(Float referBonusPercent) {
            this.referBonusPercent = referBonusPercent;
            return this;
        }

        public Builder setStudentBonusPercent(Float studentBonusPercent) {
            this.studentBonusPercent = studentBonusPercent;
            return this;
        }

        public Builder setCenterBonusPercent(Float centerBonusPercent) {
            this.centerBonusPercent = centerBonusPercent;
            return this;
        }

        public Builder setOrderDetails(List<OrderDetail> orderDetails) {
            this.orderDetails = orderDetails;
            return this;
        }

        public Builder setStatus(EOrderStatus status) {
            this.status = status;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public Order build() {
            return new Order(totalPrice, centerBonus, studentBonus, referBonus, totalBonus, referBonusPercent, studentBonusPercent, centerBonusPercent, orderDetails, status, user);
        }
    }


}
