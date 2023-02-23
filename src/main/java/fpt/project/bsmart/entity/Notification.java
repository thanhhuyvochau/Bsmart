package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "notification")
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "content")
    private String content;
    @Column(name = "is_view")
    private boolean isView;

    @JoinColumn(name = "sender_id")
    @ManyToOne
    private Account sender;
    @JoinColumn(name = "type_id")
    @ManyToOne
    private NotificationType type;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.PERSIST)
    private List<Notifier> notifiers = new ArrayList<>();
    @Column(name = "entity_id")
    private Long entityId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isView() {
        return isView;
    }

    public void setView(boolean view) {
        isView = view;
    }

    public Account getSender() {
        return sender;
    }

    public void setSender(Account sender) {
        this.sender = sender;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public List<Notifier> getNotifiers() {
        return notifiers;
    }

    public void setNotifiers(List<Notifier> notifiers) {
        this.notifiers = notifiers;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}
