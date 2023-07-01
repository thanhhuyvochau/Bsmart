package fpt.project.bsmart.entity;

import javax.persistence.*;

@Entity
@Table(name = "class_announcement")
public class ClassAnnouncement extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "content")
    @Lob
    private String content;
    @Column(name = "title")
    private String title;
    @Column(name = "visible")
    private Boolean visible = false;
    @OneToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class announcementClass;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Class getAnnouncementClass() {
        return announcementClass;
    }

    public void setAnnouncementClass(Class announcementClass) {
        this.announcementClass = announcementClass;
    }
}