package fpt.project.bsmart.entity.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class CartResponse {

    private Long id;

    private Integer totalItem = 0;

    private BigDecimal totalPrice = BigDecimal.ZERO;

    private List<CourseCartResponse> cartItems = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(Integer totalItem) {
        this.totalItem = totalItem;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<CourseCartResponse> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CourseCartResponse> cartItems) {
        this.cartItems = cartItems;
    }
}
