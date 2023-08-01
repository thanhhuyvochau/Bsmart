package fpt.project.bsmart.entity.request;


import java.io.Serializable;

public class PayCourseRequest implements Serializable {

    private Long cartItemId;
    private String referralCode;

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }
}
