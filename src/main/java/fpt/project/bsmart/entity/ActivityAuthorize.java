package fpt.project.bsmart.entity;

import javax.persistence.*;

@Entity
@Table(name = "activity_authorize")
public class ActivityAuthorize extends BaseEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class authorizeClass;
    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Class getAuthorizeClass() {
        return authorizeClass;
    }

    public void setAuthorizeClass(Class authorizeClass) {
        this.authorizeClass = authorizeClass;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}