package fpt.project.bsmart.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class CartResponse {

    @Schema(description = "id cart")
    private Long cartId;

    @Schema(description = "thông tin học sinh ")
    private AccountResponse student  ;

    @Schema(description = "Têncủa course")
    private List<CartItemTopicResponse> cartItemTopic;


    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public AccountResponse getStudent() {
        return student;
    }

    public void setStudent(AccountResponse student) {
        this.student = student;
    }

    public List<CartItemTopicResponse> getCartItemTopic() {
        return cartItemTopic;
    }

    public void setCartItemTopic(List<CartItemTopicResponse> cartItemTopic) {
        this.cartItemTopic = cartItemTopic;
    }
}
