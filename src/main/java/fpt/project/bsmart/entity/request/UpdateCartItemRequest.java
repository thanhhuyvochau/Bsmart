package fpt.project.bsmart.entity.request;


import java.io.Serializable;

public class UpdateCartItemRequest implements Serializable {

    private Long cartItemId;
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
