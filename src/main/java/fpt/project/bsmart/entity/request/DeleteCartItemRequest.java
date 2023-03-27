package fpt.project.bsmart.entity.request;


import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class DeleteCartItemRequest implements Serializable {
    @NotNull
    private Long cartItemId;

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }
}
