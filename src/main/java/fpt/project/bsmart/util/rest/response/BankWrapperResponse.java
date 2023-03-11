package fpt.project.bsmart.util.rest.response;

import java.util.ArrayList;
import java.util.List;

public class BankWrapperResponse {
    private String code;
    private String desc;
    private List<BankResponse> data = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<BankResponse> getData() {
        return data;
    }

    public void setData(List<BankResponse> data) {
        this.data = data;
    }
}
