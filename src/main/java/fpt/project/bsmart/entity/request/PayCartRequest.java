package fpt.project.bsmart.entity.request;


import fpt.project.bsmart.entity.constant.EPaymentType;

import java.io.Serializable;
import java.util.List;

public class PayCartRequest implements Serializable {

    private EPaymentType type;
    private List<PayCartItemRequest> payCartItemRequestList;

    public EPaymentType getType() {
        return type;
    }

    public void setType(EPaymentType type) {
        this.type = type;
    }

    public List<PayCartItemRequest> getPayCartItemRequestList() {
        return payCartItemRequestList;
    }

    public void setPayCartItemRequestList(List<PayCartItemRequest> payCartItemRequestList) {
        this.payCartItemRequestList = payCartItemRequestList;
    }
}
