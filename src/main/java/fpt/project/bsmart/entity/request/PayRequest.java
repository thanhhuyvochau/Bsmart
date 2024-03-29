package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.constant.EPaymentType;

public class PayRequest {
    private Long clazzId;
    private String referalCode;
    private EPaymentType type = EPaymentType.BANKING;
    private boolean useWallet = false;

    public Long getClazzId() {
        return clazzId;
    }

    public void setClazzId(Long clazzId) {
        this.clazzId = clazzId;
    }

    public String getReferalCode() {
        return referalCode;
    }

    public void setReferalCode(String referalCode) {
        this.referalCode = referalCode;
    }

    public EPaymentType getType() {
        return type;
    }

    public void setType(EPaymentType type) {
        this.type = type;
    }

    public boolean isUseWallet() {
        return useWallet;
    }

    public void setUseWallet(boolean useWallet) {
        this.useWallet = useWallet;
    }
}
