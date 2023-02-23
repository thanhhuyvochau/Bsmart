package fpt.project.bsmart.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

public class CartCourseResponse {


    @Schema(description = "id cart")
    private Long cartId;


    @Schema(description = "List course trong cart")
    private List<CourseResponse> courses;

    @Schema(description = "Tổng tiền của cart ")
    private BigDecimal totalPrice;

    @Schema(description = "Tổng tiền của cart ")
    private int quantity;

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public List<CourseResponse> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseResponse> courses) {
        this.courses = courses;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
