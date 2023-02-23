package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "notification_type")
public class NotificationType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String title;
    @Column(name = "template")
    private String template;
    @Column(name = "entity")
    private String entity;
    @Column(name = "url")
    private String url;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationType that = (NotificationType) o;
        return Objects.equals(code, that.code) && Objects.equals(title, that.title) && Objects.equals(template, that.template) && Objects.equals(entity, that.entity) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, title, template, entity, url);
    }
}
