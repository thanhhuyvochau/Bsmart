package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bank")
public class Bank extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;

    private String shortName;
    @Column(name = "logo")
    private String logo;
    @Column(name = "bin")
    private String bin;
    @Column(name = "transfer_supported")
    private Integer transferSupported = 0;
    @Column(name = "lookup_support")
    private Integer lookupSupported = 0;

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
}
