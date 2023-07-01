package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.EImageType;

import javax.persistence.*;

@Entity
@Table(name = "class_image")
public class ClassImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private EImageType type;
    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "status")
    private boolean status;

    @OneToOne(mappedBy = "classImage")
    private Class aClass;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EImageType getType() {
        return type;
    }

    public void setType(EImageType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
