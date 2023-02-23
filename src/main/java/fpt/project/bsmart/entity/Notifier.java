package fpt.project.bsmart.entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "notifier")
public class Notifier extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "notifier_id")
    @ManyToOne
    private Account notifier;
    @JoinColumn(name = "notification_id")
    @ManyToOne
    private Notification notification;

    @Column(name = "view_at")
    private Instant viewAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getNotifier() {
        return notifier;
    }

    public void setNotifier(Account notifier) {
        this.notifier = notifier;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public Instant getViewAt() {
        return viewAt;
    }

    public void setViewAt(Instant viewAt) {
        this.viewAt = viewAt;
    }
}
