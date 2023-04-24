package fpt.project.bsmart.entity;

import fpt.project.bsmart.entity.constant.EAssignmentStatus;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "assignment")
public class Assignment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "startDate")
    private Instant startDate;
    @Column(name = "endDate")
    private Instant endDate;
    @Column(name = "edit_before_submit_min")
    private Integer editBeForSubmitMin = 0;
    @Column(name = "max_file_submit")
    private Integer maxFileSubmit = 1;
    @Column(name = "max_file_size")
    private Integer maxFileSize;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EAssignmentStatus status;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "activity_id")
    private Activity activity;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Integer getEditBeForSubmitMin() {
        return editBeForSubmitMin;
    }

    public void setEditBeForSubmitMin(Integer editBeForSubmitMin) {
        this.editBeForSubmitMin = editBeForSubmitMin;
    }

    public Integer getMaxFileSubmit() {
        return maxFileSubmit;
    }

    public void setMaxFileSubmit(Integer maxFileSubmit) {
        this.maxFileSubmit = maxFileSubmit;
    }

    public Integer getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(Integer maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public EAssignmentStatus getStatus() {
        return status;
    }

    public void setStatus(EAssignmentStatus status) {
        this.status = status;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
