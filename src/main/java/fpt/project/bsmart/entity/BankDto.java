package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;



public class BankDto extends BaseEntity {
    
    
    private Long id;
    
    private String code;
    
    private String name;

    private String shortName;
    
    private String logo;
    
    private String bin;
    
    private Boolean transferSupported = false;
    
    private Boolean lookupSupported = false;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public Boolean getTransferSupported() {
        return transferSupported;
    }

    public void setTransferSupported(Boolean transferSupported) {
        this.transferSupported = transferSupported;
    }

    public Boolean getLookupSupported() {
        return lookupSupported;
    }

    public void setLookupSupported(Boolean lookupSupported) {
        this.lookupSupported = lookupSupported;
    }
}
