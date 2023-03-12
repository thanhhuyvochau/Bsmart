package fpt.project.bsmart.util.rest.response;

public class BankResponse {
    private int id;
    private String name;
    private String code;
    private String bin;
    private String shortName;
    private String logo;
    private Integer transferSupported;
    private Integer lookupSupported;
    private String short_name;
    private int support;
    private int isTransfer;
    private String swift_code;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Integer getTransferSupported() {
        return transferSupported;
    }

    public void setTransferSupported(Integer transferSupported) {
        this.transferSupported = transferSupported;
    }

    public Integer getLookupSupported() {
        return lookupSupported;
    }

    public void setLookupSupported(Integer lookupSupported) {
        this.lookupSupported = lookupSupported;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public int getSupport() {
        return support;
    }

    public void setSupport(int support) {
        this.support = support;
    }

    public int getIsTransfer() {
        return isTransfer;
    }

    public void setIsTransfer(int isTransfer) {
        this.isTransfer = isTransfer;
    }

    public String getSwift_code() {
        return swift_code;
    }

    public void setSwift_code(String swift_code) {
        this.swift_code = swift_code;
    }
}
