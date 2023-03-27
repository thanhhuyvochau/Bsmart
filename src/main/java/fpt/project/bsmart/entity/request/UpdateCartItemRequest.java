package fpt.project.bsmart.entity.request;


import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class UpdateCartItemRequest implements Serializable {
    @NotNull
    private Long cartItemId;
    @NotNull
    private Long subCourseId;

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Long getSubCourseId() {
        return subCourseId;
    }

    public void setSubCourseId(Long subCourseId) {
        this.subCourseId = subCourseId;
    }
}
