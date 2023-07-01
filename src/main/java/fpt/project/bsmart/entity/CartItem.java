package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_item")
public class CartItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

//    @ManyToOne
//    @JoinColumn(name = "sub_course_id")
//    private SubCourse subCourse;
    @Column(name = "price")
    private BigDecimal price = BigDecimal.ZERO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

//    public SubCourse getSubCourse() {
//        return subCourse;
//    }
//
//    public void setSubCourse(SubCourse subCourse) {
//        this.subCourse = subCourse;
//    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
