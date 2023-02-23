package fpt.project.bsmart.entity.dto;

import fpt.project.bsmart.entity.BaseEntity;
import fpt.project.bsmart.entity.EModuleType;


public class ModuleDto extends BaseEntity {
    private Long id;
    
    private String name;
    
    private String url;

    private EModuleType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public EModuleType getType() {
        return type;
    }

    public void setType(EModuleType type) {
        this.type = type;
    }
}
