package fpt.project.bsmart.entity.response;

public class VnPayResponse {
    private String paymentUrl;

    public VnPayResponse(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }
}
