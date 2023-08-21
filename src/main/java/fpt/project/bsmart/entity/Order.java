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
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Order() {
    }

    public Order(BigDecimal totalPrice, EOrderStatus status, List<OrderDetail> orderDetails, User user) {
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderDetails = orderDetails;
        this.user = user;
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
            return new Order(totalPrice, status, orderDetails, user);
        }
    }


}
