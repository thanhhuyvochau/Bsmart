package fpt.project.bsmart.entity.request;

public class VpnPayRequest {
    private Long clazzId;
    private String returnURL;


    public Long getClazzId() {
        return clazzId;
    }

    public void setClazzId(Long clazzId) {
        this.clazzId = clazzId;
    }

    public String getReturnURL() {
        return returnURL;
    }

    public void setReturnURL(String returnURL) {
        this.returnURL = returnURL;
    }
}
