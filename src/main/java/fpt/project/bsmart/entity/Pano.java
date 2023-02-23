package fpt.project.bsmart.entity;


import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "pano")

public class Pano extends BaseEntity  {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pano_id_generator")
    @SequenceGenerator(name = "pano_id_generator", sequenceName = "pano_id_generator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "publish_date")
    private Instant publishDate;

    @Column(name = "expiration_date")
    private Instant expirationDate;



    @Column(name = "link_url")
    private String linkUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_Id")
    private Resource resource;



    @Column(name = "is_visible")
    private Boolean isVisible = false;

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

    public Instant getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Instant publishDate) {
        this.publishDate = publishDate;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }
}
