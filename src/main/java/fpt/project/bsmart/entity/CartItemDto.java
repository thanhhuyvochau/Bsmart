package fpt.project.bsmart.entity;

import java.math.BigDecimal;


public class CartItemDto extends BaseEntity {


    private Long id;


    private Cart cart;


    private SubCourse subCourse;

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

    public SubCourse getSubCourse() {
        return subCourse;
    }

    public void setSubCourse(SubCourse subCourse) {
        this.subCourse = subCourse;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
