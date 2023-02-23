package fpt.project.bsmart.entity.request;

public class VpnPayRequest {
    private String vnp_OrderInfo;
    private String ordertype;
    private String amount;

    private Long classId;
    private String sessionId;

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getVnp_OrderInfo() {
        return vnp_OrderInfo;
    }

    public void setVnp_OrderInfo(String vnp_OrderInfo) {
        this.vnp_OrderInfo = vnp_OrderInfo;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}
