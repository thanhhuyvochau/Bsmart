package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.ENotificationEntity;
import fpt.project.bsmart.entity.constant.ENotificationType;

import javax.persistence.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "Notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "vi_title")
    private String viTitle;

    @Column(name = "vi_content")
    private String viContent;
    @Column(name = "type")
    private ENotificationType type;
    @Column(name = "entity")
    private ENotificationEntity entity;
    @Column(name = "entity_id")
    private Long entityId;
    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Notifier> notifiers = new ArrayList<>();

    public Notification(String viTitle, String viContent, ENotificationType type, ENotificationEntity entity, Long entityId, List<Notifier> notifiers) {
        this.viTitle = viTitle;
        this.viContent = viContent;
        this.type = type;
        this.entity = entity;
        this.entityId = entityId;
        this.notifiers = notifiers;
    }

    public Notification() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getViTitle() {
        return viTitle;
    }

    public void setViTitle(String viTitle) {
        this.viTitle = viTitle;
    }

    public String getViContent() {
        return viContent;
    }

    public void setViContent(String viContent) {
        this.viContent = viContent;
    }

    public ENotificationType getType() {
        return type;
    }

    public void setType(ENotificationType type) {
        this.type = type;
    }

    public ENotificationEntity getEntity() {
        return entity;
    }

    public void setEntity(ENotificationEntity entity) {
        this.entity = entity;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public List<Notifier> getNotifiers() {
        return notifiers;
    }

    public void setNotifiers(List<Notifier> notifiers) {
        this.notifiers = notifiers;
    }

    public static NotificationBuilder getBuilder() {
        return new NotificationBuilder();
    }

    public static class NotificationBuilder {
        private String viTitle;
        private String viContent;
        private ENotificationType type;
        private ENotificationEntity entity;
        private Long entityId;
        private List<User> users = new ArrayList<>();

        public NotificationBuilder viTitle(String viTitle) {
            this.viTitle = viTitle;
            return this;
        }

        public NotificationBuilder viContent(String viContent) {
            this.viContent = viContent;
            return this;
        }

        public NotificationBuilder type(ENotificationType type) {
            this.type = type;
            return this;
        }

        public NotificationBuilder entity(ENotificationEntity entity) {
            this.entity = entity;
            return this;
        }

        public NotificationBuilder entityId(Long entityId) {
            this.entityId = entityId;
            return this;
        }

        public NotificationBuilder notifiers(User... users) {
            this.users = Arrays.asList(users);
            return this;
        }

        public Notification build() {
            if (viTitle.isEmpty()) {
                throw new InvalidParameterException("Notification must has title");
            } else if (viContent.isEmpty()) {
                throw new InvalidParameterException("Notification must has content");
            }
            Notification notification = new Notification();
            notification.setViTitle(viTitle);
            notification.setViContent(viContent);
            notification.setType(type);
            notification.setEntity(entity);
            notification.setEntityId(entityId);
            notification.setNotifiers(users.stream().map(user -> {
                Notifier notifier = new Notifier();
                notifier.setNotification(notification);
                notifier.setUser(user);
                return notifier;
            }).collect(Collectors.toList()));
            return notification;
        }
    }
}
